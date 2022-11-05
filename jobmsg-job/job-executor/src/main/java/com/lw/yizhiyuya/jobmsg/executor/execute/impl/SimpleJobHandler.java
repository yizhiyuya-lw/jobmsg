package com.lw.yizhiyuya.jobmsg.executor.execute.impl;

import com.lw.yizhiyuya.jobmsg.executor.execute.IJobHandler;
import com.yizhiyuya.jobmsg.job.common.model.TriggerParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lw
 * @create 2022-11-05-21:39
 */
@Slf4j
public class SimpleJobHandler implements IJobHandler {

    @Override
    public void execute(TriggerParam triggerParam) {
        log.info("执行任务: {}", triggerParam);
    }
}
