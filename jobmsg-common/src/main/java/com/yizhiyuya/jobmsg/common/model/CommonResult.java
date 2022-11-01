package com.yizhiyuya.jobmsg.common.model;

import com.yizhiyuya.jobmsg.common.constant.ResponseConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lw
 * @create 2022-10-30-22:47
 */
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {

    private int code;
    private String message;
    private T data;

    public static CommonResult successResult() {
        return new CommonResult(ResponseConstant.SUCCESS_CODE, ResponseConstant.SUCCESS_MESSAGE, null);
    }

    public static <T> CommonResult successResult(T data) {
        return new CommonResult(ResponseConstant.SUCCESS_CODE, ResponseConstant.SUCCESS_MESSAGE, data);
    }

    public static CommonResult errorResult(String message) {
        return new CommonResult(ResponseConstant.ERROR_CODE, message, null);
    }
}
