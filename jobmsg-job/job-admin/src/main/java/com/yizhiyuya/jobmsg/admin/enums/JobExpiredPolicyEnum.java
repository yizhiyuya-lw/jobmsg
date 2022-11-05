package com.yizhiyuya.jobmsg.admin.enums;

/**
 * @author lw
 * @create 2022-11-05-15:16
 */
public enum JobExpiredPolicyEnum {
    SKIP("SKIP"),
    EXECUTE_ONCE("EXECUTE_ONCE");

    private String value;

    JobExpiredPolicyEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static JobExpiredPolicyEnum match(String value, JobExpiredPolicyEnum defaultPolicy) {
        for (JobExpiredPolicyEnum policyEnum : JobExpiredPolicyEnum.values()) {
            if (policyEnum.getValue().equals(value)) {
                return policyEnum;
            }
        }
        return defaultPolicy;
    }
}
