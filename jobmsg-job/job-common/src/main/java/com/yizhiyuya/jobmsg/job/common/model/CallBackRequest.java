package com.yizhiyuya.jobmsg.job.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author lw
 * @create 2022-11-05-22:38
 */
@Data
@NoArgsConstructor
public class CallBackRequest {

    private Integer jobId;
    private Integer logId;
    private Date executeDate;
    private boolean success;
    private Integer executeCode;
    private String executeMsg;
    private Integer executorTimeout;
    private Integer executorRetryCount;
}
