package com.lw.yizhiyuya.jobmsg.executor.Controller;

import com.lw.yizhiyuya.jobmsg.executor.handler.JobExecuteHandler;
import com.yizhiyuya.jobmsg.common.model.CommonResult;
import com.yizhiyuya.jobmsg.job.common.model.TriggerParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lw
 * @create 2022-10-31-20:21
 */
@RestController
public class ExecutorController {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorController.class);

    @PostMapping("/first")
    public CommonResult<String> first(@RequestParam("param") String param) {
        logger.info("get param: {}", param);
        return new CommonResult<>(200, "success", param);
    }

    @PostMapping("/execute")
    public CommonResult<String> execute(@RequestBody TriggerParam triggerParam) {
        try {
            JobExecuteHandler.getInstance().pushJobTrigger(triggerParam);
            return CommonResult.successResult();
        } catch (Exception e) {
            return CommonResult.errorResult(e.getMessage());
        }

    }
}
