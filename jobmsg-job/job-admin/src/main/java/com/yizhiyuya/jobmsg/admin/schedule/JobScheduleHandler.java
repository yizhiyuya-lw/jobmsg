package com.yizhiyuya.jobmsg.admin.schedule;

import com.yizhiyuya.jobmsg.admin.mapper.JobInfoMapper;
import com.yizhiyuya.jobmsg.admin.util.SpringBeanFactoryUtil;
import com.yizhiyuya.jobmsg.job.common.model.JobInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.List;

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

    public void start() {

        preparedJobSchedule();

        //启动一个线程，用于扫描数据库将要执行的任务
        Thread scanJobThread = new Thread(new Runnable() {
            @Override
            public void run() {

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
                                updateJobNextTriggerTime(jobInfo, getJobNextTriggerTime(jobInfo));
                            } else if (triggerNextTime < currentTimeMillis && triggerNextTime > currentTimeMillis - PRE_MILLIS) { //执行时间小于当前时间5秒之内
                                //立即执行一次，并更新下一次执行时间
                                createTriggerAndExecute(jobInfo);
                                updateJobNextTriggerTime(jobInfo, getJobNextTriggerTime(jobInfo));
                            } else { //还没到执行时间的任务
                                //todo
                            }
                        }
                    }
                }
            }
        });
        scanJobThread.setName("scan-job-thread");
        scanJobThread.setDaemon(true);
        scanJobThread.start();



        //启动一个线程，根据当前时间秒数，获取需要执行的任务，交由触发器处理器处理

    }

    /**
     * 任务调度处理器预准备工作
     * 一些初始化预备工作
     */
    private void preparedJobSchedule() {
        this.jobInfoMapper = SpringBeanFactoryUtil.getBeanByType(JobInfoMapper.class);
    }

    /**
     * 创建任务触发器，并交由触发器处理器执行任务
     * @param jobInfo
     */
    private void createTriggerAndExecute(JobInfo jobInfo) {

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
    private long getJobNextTriggerTime(JobInfo jobInfo) {
        return 1;
    }

    /**
     * 更新job下一次执行时间
     * @param jobInfo
     * @param nextTriggerTime
     */
    private void updateJobNextTriggerTime(JobInfo jobInfo, long nextTriggerTime) {

    }
}
