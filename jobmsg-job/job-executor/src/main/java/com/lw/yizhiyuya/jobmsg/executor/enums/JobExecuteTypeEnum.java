package com.lw.yizhiyuya.jobmsg.executor.enums;

/**
 * @author lw
 * @create 2022-11-05-22:17
 */
public enum JobExecuteTypeEnum {

    SIMPLE("SIMPLE");

    private String value;
    private JobExecuteTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static JobExecuteTypeEnum match(String value, JobExecuteTypeEnum defaultType) {
        for (JobExecuteTypeEnum typeEnum : JobExecuteTypeEnum.values()) {
            if (typeEnum.getValue().equals(value)) {
                return typeEnum;
            }
        }
        return defaultType;
    }
}
