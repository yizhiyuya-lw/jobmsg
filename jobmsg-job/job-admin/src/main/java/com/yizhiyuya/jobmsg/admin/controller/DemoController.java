package com.yizhiyuya.jobmsg.admin.controller;

import com.yizhiyuya.jobmsg.admin.common.model.CommonResult;
import com.yizhiyuya.jobmsg.admin.service.ExecutorClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lw
 * @create 2022-10-30-23:10
 */
@RestController
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private ExecutorClientService executorClientService;

    @GetMapping("/test")
    public CommonResult<String> test() {
        return new CommonResult<>(200, "success", "data");
    }

    @GetMapping("/consume/first")
    public CommonResult<String> ConsumeFirst(@RequestParam("param") String param) {
        CommonResult<String> result = executorClientService.first(param);
        logger.info("get result: {}", result);
        return result;
    }
}
