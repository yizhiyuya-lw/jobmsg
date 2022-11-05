package com.lw.yizhiyuya.jobmsg.executor.handler;

import com.lw.yizhiyuya.jobmsg.executor.enums.JobExecuteTypeEnum;
import com.lw.yizhiyuya.jobmsg.executor.execute.IJobHandler;
import com.lw.yizhiyuya.jobmsg.executor.execute.impl.SimpleJobHandler;
import com.lw.yizhiyuya.jobmsg.executor.service.JobAdminClientService;
import com.lw.yizhiyuya.jobmsg.executor.util.SpringBeanFactoryUtil;
import com.yizhiyuya.jobmsg.common.constant.ResponseConstant;
import com.yizhiyuya.jobmsg.common.model.CommonResult;
import com.yizhiyuya.jobmsg.job.common.exception.JobCoreException;
import com.yizhiyuya.jobmsg.job.common.model.CallBackRequest;
import com.yizhiyuya.jobmsg.job.common.model.TriggerParam;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 任务接收处理，回调处理器
 * @author lw
 * @create 2022-11-05-21:49
 */
@Slf4j
public class JobExecuteHandler {

    private static JobExecuteHandler instance = new JobExecuteHandler();

    private JobExecuteHandler() { }

    public static JobExecuteHandler getInstance() {
        return instance;
    }

    private JobAdminClientService jobAdminClientService = SpringBeanFactoryUtil.getBeanByType(JobAdminClientService.class);

    /**
     * 任务执行结束，需要回调的任务队列
     */
    private LinkedBlockingQueue<CallBackRequest> callBackQueue = new LinkedBlockingQueue<>(2000);

    /**
     * 接收管理端发送的任务执行请求
     */
    private LinkedBlockingQueue<TriggerParam> jobExecuteQueue = new LinkedBlockingQueue<>(2000);

    private Map<String, IJobHandler> jobHandlerCache = new ConcurrentHashMap<>();

    public void pushJobTrigger(TriggerParam triggerParam) throws JobCoreException {
        try {
            jobExecuteQueue.offer(triggerParam, 3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.info("请求执行任务失败：{}", triggerParam, e);
            throw new JobCoreException("任务队列已满，请稍后重试!");
        }
    }

    public void init() {
        Thread jobHandleThread = new Thread(() -> {
            while (true) {
                try {
                    TriggerParam triggerParam = jobExecuteQueue.take();
                    if (null == triggerParam) {
                        log.info("无法获取到有效任务");
                        continue;
                    }
                    IJobHandler jobHandler = getJobHandler(triggerParam.getExecutorType());
                    new JobExecuteThread(jobHandler, triggerParam).start();
                } catch (InterruptedException e) {
                    log.info("获取执行任务异常：", e);
                }
            }
        }, "job-handle-thread");
        jobHandleThread.setDaemon(true);
        jobHandleThread.start();

        Thread jobCallBackThread = new Thread(() -> {
            while (true) {
                try {
                    CallBackRequest callBackRequest = callBackQueue.take();
                    CommonResult<String> result = jobAdminClientService.callback(callBackRequest);
                    if (ResponseConstant.SUCCESS_CODE != result.getCode()) {
                        log.info("任务id{},日志id{}, 回调失败", callBackRequest.getJobId(), callBackRequest.getLogId());
                    }
                } catch (InterruptedException e) {
                    log.info("获取回调结果异常, ", e);
                }
            }
        }, "job-callback-thread");
        jobCallBackThread.setDaemon(true);
        jobCallBackThread.start();
    }

    public IJobHandler getJobHandler(String typeName) {
        IJobHandler jobHandler = jobHandlerCache.get(typeName);
        if (null == jobHandler) {
            if (JobExecuteTypeEnum.SIMPLE == JobExecuteTypeEnum.match(typeName, JobExecuteTypeEnum.SIMPLE)) {
                jobHandler = new SimpleJobHandler();
                jobHandlerCache.put(typeName, jobHandler);
            }
        }
        return jobHandler;
    }

    public void pushCallBack(CallBackRequest callBackRequest) {
        callBackQueue.offer(callBackRequest);
    }
}
