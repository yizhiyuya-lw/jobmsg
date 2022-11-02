package com.yizhiyuya.jobmsg.job.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author lw
 * @create 2022-11-01-22:48
 */
@Data
@NoArgsConstructor
public class JobLog {

    private Integer id;
    private Integer jobId;
    private String runStatus;
    private String executorType;
    private String executorParam;
    private Date triggerTime;
    private Integer triggerCode;
    private String triggerMsg;
    private Date handleTime;
    private Integer handleCode;
    private String handleMsg;
    private Integer alarmStatus;
    private Integer retryCount;
}
