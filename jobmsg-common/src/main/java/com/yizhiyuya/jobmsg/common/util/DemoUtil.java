package com.yizhiyuya.jobmsg.common.util;

import cn.hutool.core.util.IdUtil;

/**
 * 测试代码
 * @author lw
 * @create 2022-10-30-22:45
 */
public class DemoUtil {

    public static void main(String[] args) {
        String uuid = IdUtil.fastSimpleUUID();
        System.out.println(uuid);
    }
}
