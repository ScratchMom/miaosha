package com.imooc.miaosha.redis;

/**
 * @author yifan
 * @date 2018/7/19 下午3:21
 */
public class OrderKey extends BasePrefix {

    public OrderKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKey getMiaoshOrderByUidGid = new OrderKey(-1,"moug");
}
