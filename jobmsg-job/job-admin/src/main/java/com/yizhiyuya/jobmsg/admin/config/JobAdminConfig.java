package com.yizhiyuya.jobmsg.admin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lw
 * @create 2022-11-01-23:00
 */
@Configuration
@MapperScan(basePackages = {"com.yizhiyuya.jobmsg.admin.mapper"})
public class JobAdminConfig {
}
