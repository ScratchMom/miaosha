package com.imooc.miaosha.redis;

/**
 * @author yifan
 * @date 2018/7/21 下午7:48
 */
public class AccessKey extends BasePrefix {


    public AccessKey(String prefix) {
        super(prefix);
    }

    public AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }


    public static AccessKey access = new AccessKey(5,"access");


    public static AccessKey withExpire (int expire) {
        return new AccessKey(expire,"access");
    }



}
