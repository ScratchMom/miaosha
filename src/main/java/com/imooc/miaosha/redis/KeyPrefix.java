package com.imooc.miaosha.redis;

/**
 * @author yifan
 * @date 2018/7/19 下午3:13
 */
public interface KeyPrefix {    // 接口 -- 抽象类 -- 实现类

    public int expireSeconds(); // 有效期
    public String getPrefix();
}
