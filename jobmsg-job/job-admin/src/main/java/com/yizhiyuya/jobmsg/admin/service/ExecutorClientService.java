package com.yizhiyuya.jobmsg.admin.service;

import com.yizhiyuya.jobmsg.admin.common.model.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lw
 * @create 2022-10-31-20:19
 */

@FeignClient(value = "jobmsg-job-executor")
public interface ExecutorClientService {
    @PostMapping("/first")
    public CommonResult<String> first(@RequestParam("param") String param);
}
