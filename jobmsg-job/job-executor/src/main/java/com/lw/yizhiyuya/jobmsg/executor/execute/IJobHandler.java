package com.lw.yizhiyuya.jobmsg.executor.execute;

import com.yizhiyuya.jobmsg.job.common.model.TriggerParam;

/**
 * 任务处理接口
 * @author lw
 * @create 2022-11-05-21:27
 */
public interface IJobHandler {

    void execute(TriggerParam triggerParam);
}
