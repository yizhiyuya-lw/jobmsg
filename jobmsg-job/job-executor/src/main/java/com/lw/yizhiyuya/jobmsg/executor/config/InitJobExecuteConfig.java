package com.lw.yizhiyuya.jobmsg.executor.config;

import com.lw.yizhiyuya.jobmsg.executor.handler.JobExecuteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author lw
 * @create 2022-11-05-22:54
 */
@Component
@Slf4j
public class InitJobExecuteConfig implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        JobExecuteHandler.getInstance().init();
        log.info("任务执行服务启动完成...");
    }
}
