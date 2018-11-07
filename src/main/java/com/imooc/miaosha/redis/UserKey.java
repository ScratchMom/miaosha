package com.imooc.miaosha.redis;

/**
 * @author yifan
 * @date 2018/7/19 下午3:21
 */
public class UserKey extends BasePrefix {



    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");

}
