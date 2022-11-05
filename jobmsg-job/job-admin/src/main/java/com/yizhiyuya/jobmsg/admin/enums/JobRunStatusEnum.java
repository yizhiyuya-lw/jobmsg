package com.yizhiyuya.jobmsg.admin.enums;

/**
 * @author lw
 * @create 2022-11-05-15:50
 */
public enum JobRunStatusEnum {
    TRIGGER("TRIGGER"),
    RUNNING("RUNNING"),
    FAILED("FAILED"),
    SUCCESS("SUCCESS");

    private String value;

    private JobRunStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static JobRunStatusEnum match(String value, JobRunStatusEnum defaultRunStatus) {
        for (JobRunStatusEnum runStatusEnum : JobRunStatusEnum.values()) {
            if (runStatusEnum.getValue().equals(value)) {
                return runStatusEnum;
            }
        }
        return defaultRunStatus;
    }
}
