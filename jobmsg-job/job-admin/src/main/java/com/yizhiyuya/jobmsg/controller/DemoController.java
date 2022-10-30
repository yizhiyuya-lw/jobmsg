package com.yizhiyuya.jobmsg.controller;

import com.yizhiyuya.jobmsg.common.model.CommonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lw
 * @create 2022-10-30-23:10
 */
@RestController
public class DemoController {

    @GetMapping("/test")
    public CommonResult<String> test() {
        return new CommonResult<>(200, "success", "data");
    }
}
