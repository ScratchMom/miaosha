package com.imooc.miaosha.redis;

/**
 * @author yifan
 * @date 2018/7/21 下午7:48
 */
public class MiaoKey extends BasePrefix {


    public MiaoKey(String prefix) {
        super(prefix);
    }

    public MiaoKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoKey isGoodsOver = new MiaoKey("go");
    public static MiaoKey getMiaoshPath = new MiaoKey(60,"go");
    public static MiaoKey getMiaoshaVerifyCode = new MiaoKey(60,"go");


}
