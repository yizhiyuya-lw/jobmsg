package com.lw.yizhiyuya.jobmsg.executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lw
 * @create 2022-10-31-20:20
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class JobExecutorApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobExecutorApplication.class, args);
    }
}
