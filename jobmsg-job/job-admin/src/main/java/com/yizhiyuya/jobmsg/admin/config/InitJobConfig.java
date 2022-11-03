package com.yizhiyuya.jobmsg.admin.config;

import com.yizhiyuya.jobmsg.admin.schedule.JobScheduleHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author lw
 * @create 2022-11-03-22:54
 */
@Component
@Slf4j
public class InitJobConfig implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        JobScheduleHandler.getInstance().start();
        log.info("任务启动。。。");
    }
}
