package com.yizhiyuya.jobmsg.admin.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @author lw
 * @create 2022-10-30-22:47
 */
@ToString
@Data
@AllArgsConstructor
public class CommonResult<T> {

    private int code;
    private String message;
    private T data;
}
