package com.yizhiyuya.jobmsg.job.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务执行trigger数据封装实体类，用于服务调用
 *
 * @author lw
 * @create 2022-11-01-21:46
 */
@Data
@NoArgsConstructor
public class TriggerParam {

    private Integer jobId; /*任务id*/
    private Long triggerTime; /*当前任务执行时间毫秒值*/
    private Long logId; /*日志id，用于回调*/
    private int executorType; /*任务类型*/
    private String executorParam; /*任务执行参数*/
    private Integer executorTimeout; /*任务执行超时时间*/
    private Integer executorRetryCount; /*任务执行失败重试次数*/

}
