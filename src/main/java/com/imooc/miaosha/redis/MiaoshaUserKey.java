package com.imooc.miaosha.redis;

/**
 * @author yifan
 * @date 2018/7/21 下午7:48
 */
public class MiaoshaUserKey extends BasePrefix {

    private static final int TOKEN_EXPIRE = 3600*24*2;

    public MiaoshaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE,"tk");
}
