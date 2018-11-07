package com.imooc.miaosha.util;

import java.util.UUID;

/**
 * @author yifan
 * @date 2018/7/21 下午7:40
 */
public class UUIDUtil {
    public static String uuuid() {
        return UUID.randomUUID().toString().replace("-","");
    }
}
