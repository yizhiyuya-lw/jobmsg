package com.yizhiyuya.jobmsg.admin.mapper;

import com.yizhiyuya.jobmsg.job.common.model.JobLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lw
 * @create 2022-11-01-22:52
 */
@Mapper
public interface JobLogMapper {

    int insertJobLog(JobLog jobLog);

    int updateJobLog(JobLog jobLog);

    int updateJobResult(JobLog jobLog);
}
