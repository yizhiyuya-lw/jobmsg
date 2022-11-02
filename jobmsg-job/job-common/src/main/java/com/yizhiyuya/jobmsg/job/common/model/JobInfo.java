package com.yizhiyuya.jobmsg.job.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author lw
 * @create 2022-11-01-20:54
 */
@Data
@NoArgsConstructor
public class JobInfo {
    private Integer id;
    private String jobDesc;
    private Date addTime;
    private Date updateTime;
    private String userId;
    private String alarmType;
    private String alarmConf;
    private String scheduleType;
    private String scheduleConf;
    private String expiredPolicy;
    private String executorType;
    private String executorParam;
    private Integer executorTimeout;
    private Integer executorRetryCount;
    private String triggerStatus;
    private Long triggerLastTime;
    private Long triggerNextTime;
}
