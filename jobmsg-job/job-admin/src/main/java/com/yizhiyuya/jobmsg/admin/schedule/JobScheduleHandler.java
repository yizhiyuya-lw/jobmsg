package com.yizhiyuya.jobmsg.admin.schedule;

import com.yizhiyuya.jobmsg.admin.mapper.JobInfoMapper;
import com.yizhiyuya.jobmsg.admin.util.SpringBeanFactoryUtil;
import com.yizhiyuya.jobmsg.job.common.exception.JobCoreException;
import com.yizhiyuya.jobmsg.job.common.model.JobInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 任务调度处理器
 * 用于获取当前时刻需要处理的任务，派发到TriggerHandler触发处理器
 * @author lw
 * @create 2022-11-02-21:26
 */
@Slf4j
public class JobScheduleHandler {

    private static final int PRE_MILLIS = 5000;

    private JobScheduleHandler() {}
    public static JobScheduleHandler getInstance() {
        return new JobScheduleHandler();
    }

    private JobInfoMapper jobInfoMapper;

    /**
     * 扫描线程运行标志
     */
    private volatile boolean scanJobThreadRunningFlag = true;

    /**
     * 循环检查任务线程运行标志
     */
    private volatile boolean ringJobThreadRunningFlag = true;

    /**
     * key为将要执行的任务的时间秒数
     * value 为需要执行的任务列表
     */
    private Map<Integer, List<JobInfo>> ringJobMap = new ConcurrentHashMap<>();

    public void start() {

        preparedJobSchedule();

        //启动一个线程，用于扫描数据库将要执行的任务
        Thread scanJobThread = new Thread(new Runnable() {
            @Override
            public void run() {

                //尽量保持整数秒
                try {
                    TimeUnit.MILLISECONDS.sleep(PRE_MILLIS - System.currentTimeMillis()%1000);
                } catch (InterruptedException e) {
                    log.info("开启任务扫描线程失败: {}", e.getMessage(), e);
                    scanJobThreadRunningFlag = false;
                }

                while (scanJobThreadRunningFlag) {
                    long currentTimeMillis = System.currentTimeMillis();
                    List<JobInfo> jobInfoList = jobInfoMapper.selectAllWaitExecuteJobInfo(currentTimeMillis + PRE_MILLIS);
                    if (!CollectionUtils.isEmpty(jobInfoList)) {
                        //遍历所有任务
                        for (JobInfo jobInfo : jobInfoList) {
                            Long triggerNextTime = jobInfo.getTriggerNextTime();
                            //如果触发时间小于当前时间超过5秒，根据过期策略处理
                            if (triggerNextTime < currentTimeMillis - PRE_MILLIS) {
                                dealExpireJob(jobInfo);
                                //updateJobNextTriggerTime(jobInfo, getJobNextTriggerTime(jobInfo));
                            } else if (triggerNextTime < currentTimeMillis && triggerNextTime > currentTimeMillis - PRE_MILLIS) { //执行时间小于当前时间5秒之内
                                //立即执行一次，并更新下一次执行时间
                                createTriggerAndExecute(jobInfo);
                                //updateJobNextTriggerTime(jobInfo, getJobNextTriggerTime(jobInfo));
                            } else { //将要在5秒之内执行的任务
                                //获取秒数
                                int ringSecond = (int)(triggerNextTime / 1000 % 60);
                                putJobToRingMap(ringSecond, jobInfo);
                                //updateJobNextTriggerTime(jobInfo, getJobNextTriggerTime(jobInfo));
                            }
                            try {
                                updateJobNextTriggerTime(jobInfo, getJobNextTriggerTime(jobInfo));
                            } catch (JobCoreException e) {
                                log.info("任务id: {} 更新下一次执行时间异常: {}", e.getMessage(), e);
                                // todo 发送告警消息
                            }
                        }
                    } else { //没有任务，休眠5秒继续执行
                        try {
                            TimeUnit.MILLISECONDS.sleep(PRE_MILLIS - System.currentTimeMillis()%1000);
                        } catch (InterruptedException e) {
                            log.info("任务扫描线程执行失败: {}", e.getMessage(), e);
                            scanJobThreadRunningFlag = false;
                        }
                    }
                    //扫描结束，如果是1秒之内完成，则需等待
                    int consMillis = (int)(System.currentTimeMillis() - currentTimeMillis);
                    if (consMillis < 1000) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
                        } catch (InterruptedException e) {
                            log.info("任务扫描线程执行失败: {}", e.getMessage(), e);
                            scanJobThreadRunningFlag = false;
                        }
                    }
                }
                //结束循环，意味着停止了扫描线程
                log.info("任务扫描线程结束...");
            }
        });
        scanJobThread.setName("scan-job-thread");
        scanJobThread.setDaemon(true);
        scanJobThread.start();

        //启动一个线程，根据当前时间秒数，获取需要执行的任务，交由触发器处理器处理
        Thread ringJobThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (ringJobThreadRunningFlag) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
                    } catch (InterruptedException e) {
                        log.info("循环任务检查线程执行失败: {}", e.getMessage(), e);
                        ringJobThreadRunningFlag = false;
                    }
                    //获取当前时间秒
                    int currSecond = Calendar.getInstance().get(Calendar.SECOND);
                    List<JobInfo> allNeedRunJobList = new ArrayList<>();
                    //获取容器中当前秒及上一秒要执行的任务（补偿机制，防止过秒丢失任务）
                    for (int i = 0; i < 2; i++) {
                        List<JobInfo> ringJobInfoList = ringJobMap.remove((currSecond + 60 - i) % 60);
                        if (null != ringJobInfoList) {
                            allNeedRunJobList.addAll(ringJobInfoList);
                        }
                    }
                    if (allNeedRunJobList.size() > 0) {
                        for (JobInfo jobInfo : allNeedRunJobList) {
                            createTriggerAndExecute(jobInfo);
                        }
                        allNeedRunJobList.clear();
                    }
                }
                log.info("循环任务执行线程结束...");
            }
        });
        ringJobThread.setName("ring-job-thread");
        ringJobThread.setDaemon(true);
        ringJobThread.start();
    }

    /**
     * 任务调度处理器预准备工作
     * 一些初始化预备工作
     */
    private void preparedJobSchedule() {
        this.jobInfoMapper = SpringBeanFactoryUtil.getBeanByType(JobInfoMapper.class);
    }

    /**
     * 将即将要执行的任务提交到等待执行容器中
     * @param ringSecond
     * @param jobInfo
     */
    private void putJobToRingMap(int ringSecond, JobInfo jobInfo) {
        List<JobInfo> jobInfoList = ringJobMap.get(ringSecond);
        if (null == jobInfoList) {
            jobInfoList = new ArrayList<>();
            jobInfoList.add(jobInfo);
            ringJobMap.put(ringSecond, jobInfoList);
        } else {
            jobInfoList.add(jobInfo);
        }
    }

    /**
     * 创建任务触发器，并交由触发器处理器执行任务
     * @param jobInfo
     */
    private void createTriggerAndExecute(JobInfo jobInfo) {
        log.info("当前正在执行的任务: {}", jobInfo);
    }

    /**
     * 处理过期任务
     * @param jobInfo
     */
    private void dealExpireJob(JobInfo jobInfo) {

    }

    /**
     * 获取job下一次执行时间
     * @param jobInfo
     * @return
     */
    private long getJobNextTriggerTime(JobInfo jobInfo) throws JobCoreException {
        try {
            CronExpression cronExpression = new CronExpression(jobInfo.getScheduleConf());
            Date nextValidTimeAfter = cronExpression.getNextValidTimeAfter(new Date(jobInfo.getTriggerNextTime()));
            return nextValidTimeAfter.getTime();
        } catch (ParseException e) {
            throw new JobCoreException("cron表达式解析异常", e);
        }
    }

    /**
     * 更新job下一次执行时间
     * @param jobInfo
     * @param nextTriggerTime
     */
    private void updateJobNextTriggerTime(JobInfo jobInfo, long nextTriggerTime) {
        jobInfoMapper.updateNextTriggerTime(jobInfo.getId(), nextTriggerTime);
    }
}
