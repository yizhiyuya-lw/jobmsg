package com.yizhiyuya.jobmsg.admin.controller;

import com.yizhiyuya.jobmsg.admin.enums.JobRunStatusEnum;
import com.yizhiyuya.jobmsg.admin.mapper.JobLogMapper;
import com.yizhiyuya.jobmsg.common.model.CommonResult;
import com.yizhiyuya.jobmsg.job.common.model.CallBackRequest;
import com.yizhiyuya.jobmsg.job.common.model.JobLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lw
 * @create 2022-11-05-22:56
 */
@RestController
@Slf4j
public class CallBackController {

    @Autowired
    private JobLogMapper jobLogMapper;

    @PostMapping("/callback")
    public CommonResult<String> callback(@RequestBody CallBackRequest callBackRequest) {
        log.info("任务{} 执行callback", callBackRequest);
        JobLog jobLog = new JobLog();
        jobLog.setId(callBackRequest.getLogId());
        jobLog.setHandleTime(callBackRequest.getExecuteDate());
        jobLog.setHandleCode(callBackRequest.getExecuteCode());
        jobLog.setHandleMsg(callBackRequest.getExecuteMsg());
        if (callBackRequest.isSuccess()) {
            jobLog.setRunStatus(JobRunStatusEnum.SUCCESS.getValue());
        } else {
            jobLog.setRunStatus(JobRunStatusEnum.FAILED.getValue());
        }
        jobLogMapper.updateJobResult(jobLog);
        return CommonResult.successResult();
    }
}
