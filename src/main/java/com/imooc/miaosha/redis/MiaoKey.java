package com.imooc.miaosha.redis;

/**
 * @author yifan
 * @date 2018/7/21 下午7:48
 */
public class MiaoKey extends BasePrefix {

    private static final int TOKEN_EXPIRE = 3600*24*2;

    public MiaoKey(String prefix) {
        super(prefix);
    }

    public static MiaoKey isGoodsOver = new MiaoKey("go");

}
