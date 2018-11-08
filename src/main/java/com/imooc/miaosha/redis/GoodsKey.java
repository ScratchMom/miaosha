package com.imooc.miaosha.redis;

/**
 * @author yifan
 * @date 2018/7/19 下午3:21
 */
public class GoodsKey extends BasePrefix {

    private GoodsKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }
    public static GoodsKey getGoodsList = new GoodsKey(60,"gl");
    public static GoodsKey getGoodsDetail = new GoodsKey(60,"gd");

}
