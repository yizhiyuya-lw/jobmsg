package com.yizhiyuya.jobmsg.admin.service;

import com.yizhiyuya.jobmsg.common.model.CommonResult;
import com.yizhiyuya.jobmsg.job.common.model.TriggerParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lw
 * @create 2022-10-31-20:19
 */

@FeignClient(value = "jobmsg-job-executor")
public interface ExecutorClientService {
    @PostMapping("/first")
    public CommonResult<String> first(@RequestParam("param") String param);

    @PostMapping("/execute")
    public CommonResult<String> execute(@RequestBody TriggerParam triggerParam);
}
