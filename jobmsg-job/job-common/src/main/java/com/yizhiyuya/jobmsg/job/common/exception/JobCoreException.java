package com.yizhiyuya.jobmsg.job.common.exception;

/**
 * @author lw
 * @create 2022-11-02-22:06
 */
public class JobCoreException extends Exception {
    public JobCoreException() {
    }

    public JobCoreException(String message) {
        super(message);
    }

    public JobCoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public JobCoreException(Throwable cause) {
        super(cause);
    }

    public JobCoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
