package com.lw.yizhiyuya.jobmsg.executor.service;

import com.yizhiyuya.jobmsg.common.model.CommonResult;
import com.yizhiyuya.jobmsg.job.common.model.CallBackRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lw
 * @create 2022-11-05-21:51
 */
@FeignClient("jobmsg-job-admin")
public interface JobAdminClientService {

    @PostMapping("/callback")
    public CommonResult<String> callback(@RequestBody CallBackRequest callBackRequest);
}
