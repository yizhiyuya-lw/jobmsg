package com.lw.yizhiyuya.jobmsg.executor.handler;

import com.lw.yizhiyuya.jobmsg.executor.execute.IJobHandler;
import com.yizhiyuya.jobmsg.common.constant.ResponseConstant;
import com.yizhiyuya.jobmsg.job.common.model.CallBackRequest;
import com.yizhiyuya.jobmsg.job.common.model.TriggerParam;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author lw
 * @create 2022-11-05-22:27
 */
@Slf4j
public class JobExecuteThread extends Thread {

    private TriggerParam triggerParam;
    private IJobHandler jobHandler;

    public JobExecuteThread(IJobHandler jobHandler, TriggerParam triggerParam) {
        this.jobHandler = jobHandler;
        this.triggerParam = triggerParam;
    }

    @Override
    public void run() {
        CallBackRequest callBackRequest = new CallBackRequest();
        callBackRequest.setJobId(triggerParam.getJobId());
        callBackRequest.setLogId(triggerParam.getLogId());
        callBackRequest.setSuccess(true);
        callBackRequest.setExecuteCode(ResponseConstant.SUCCESS_CODE);
        callBackRequest.setExecuteMsg("执行成功");
        try {
            jobHandler.execute(triggerParam);
        } catch (Exception e) {
            log.info("任务执行异常", e);
            callBackRequest.setSuccess(false);
            callBackRequest.setExecuteCode(ResponseConstant.ERROR_CODE);
            callBackRequest.setExecuteMsg("执行失败：" + e.getMessage());
        }
        callBackRequest.setExecuteDate(new Date());
        JobExecuteHandler.getInstance().pushCallBack(callBackRequest);
    }
}
