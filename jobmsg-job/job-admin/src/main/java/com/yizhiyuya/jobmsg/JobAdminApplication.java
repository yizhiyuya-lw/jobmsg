package com.yizhiyuya.jobmsg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lw
 * @create 2022-10-30-22:58
 */
@SpringBootApplication
@EnableDiscoveryClient
public class JobAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobAdminApplication.class, args);
    }
}
