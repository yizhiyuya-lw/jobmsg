package com.yizhiyuya.jobmsg.admin.trigger;

import com.yizhiyuya.jobmsg.admin.enums.JobRunStatusEnum;
import com.yizhiyuya.jobmsg.admin.mapper.JobLogMapper;
import com.yizhiyuya.jobmsg.admin.service.ExecutorClientService;
import com.yizhiyuya.jobmsg.admin.util.SpringBeanFactoryUtil;
import com.yizhiyuya.jobmsg.common.constant.ResponseConstant;
import com.yizhiyuya.jobmsg.common.model.CommonResult;
import com.yizhiyuya.jobmsg.job.common.model.JobInfo;
import com.yizhiyuya.jobmsg.job.common.model.JobLog;
import com.yizhiyuya.jobmsg.job.common.model.TriggerParam;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.*;

/**
 * 触发器处理器
 *   采用线程池处理，接收需要执行的job，创建trigger，然后请求执行服务进行任务处理
 * @author lw
 * @create 2022-11-02-21:27
 */
@Slf4j
public class TriggerHandler {

    private ExecutorService triggerThreadPool = null;

    private TriggerHandler() {

    }

    private static TriggerHandler instance = null;

    public static TriggerHandler getInstance() {
        if (instance == null) {
            instance = new TriggerHandler();
            instance.initTriggerThreadPool();
        }
        return instance;
    }

    /**
     * 初始化执行线程池
     */
    private void initTriggerThreadPool() {
        if (triggerThreadPool == null) {
            triggerThreadPool = new ThreadPoolExecutor(
                    6,
                    8,
                    60L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(1000),
                    new ThreadFactory() {
                        @Override
                        public Thread newThread(Runnable r) {
                            Thread thread = new Thread(r, "triggerHandler-threadPool-" + r.hashCode());
                            return thread;
                        }
                    },
                    new ThreadPoolExecutor.AbortPolicy());
        }
    }

    /**
     * 创建触发器，交由线程池执行
     * @param jobInfo
     */
    public void addTrigger(JobInfo jobInfo) {
        this.triggerThreadPool.execute(() -> {
            //添加运行日志--TRIGGER
            JobLog jobLog = new JobLog();
            jobLog.setJobId(jobInfo.getId());
            jobLog.setRunStatus(JobRunStatusEnum.TRIGGER.getValue());
            jobLog.setExecutorType(jobInfo.getExecutorType());
            jobLog.setExecutorParam(jobInfo.getExecutorParam());
            jobLog.setTriggerTime(new Date());
            JobLogMapper jobLogMapper = SpringBeanFactoryUtil.getBeanByType(JobLogMapper.class);
            jobLogMapper.insertJobLog(jobLog);
            //向服务端发起请求
            TriggerParam triggerParam = new TriggerParam();
            triggerParam.setJobId(jobInfo.getId());
            triggerParam.setTriggerTime(jobInfo.getTriggerNextTime());
            triggerParam.setJobId(jobLog.getId());
            triggerParam.setExecutorType(jobInfo.getExecutorType());
            triggerParam.setExecutorParam(jobInfo.getExecutorParam());
            triggerParam.setExecutorTimeout(jobInfo.getExecutorTimeout());
            triggerParam.setExecutorRetryCount(jobInfo.getExecutorRetryCount());
            ExecutorClientService executorClientService = SpringBeanFactoryUtil.getBeanByType(ExecutorClientService.class);
            CommonResult<String> result = executorClientService.execute(triggerParam);
            if (ResponseConstant.SUCCESS_CODE == result.getCode()) { //请求成功
                log.info("请求任务执行服务成功，任务正在执行中...{}", jobInfo);
                jobLog.setRunStatus(JobRunStatusEnum.RUNNING.getValue());
            } else {
                log.info("请求任务执行服务失败...{}", jobInfo);
                jobLog.setRunStatus(JobRunStatusEnum.FAILED.getValue());
            }
            jobLog.setTriggerTime(new Date());
            jobLog.setTriggerCode(result.getCode());
            jobLog.setTriggerMsg(result.getMessage());
            jobLogMapper.updateJobLog(jobLog);
        });
    }
}
