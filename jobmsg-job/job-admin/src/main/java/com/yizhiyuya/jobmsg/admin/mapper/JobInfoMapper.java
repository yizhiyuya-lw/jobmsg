package com.yizhiyuya.jobmsg.admin.mapper;

import com.yizhiyuya.jobmsg.job.common.model.JobInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lw
 * @create 2022-11-01-22:52
 */
@Mapper
public interface JobInfoMapper {

    int insert(JobInfo jobInfo);

    int update(JobInfo jobInfo);

    int updateNextTriggerTime(@Param("jobId") Integer jobId, @Param("nextTriggerTime") long nextTriggerTime);

    int deleteById(@Param("jobId") Integer jobId);

    JobInfo selectById(@Param("jobId") Integer jobId);

    List<JobInfo> selectAllWaitExecuteJobInfo(@Param("nextTriggerTime") long nextTriggerTime);
}
