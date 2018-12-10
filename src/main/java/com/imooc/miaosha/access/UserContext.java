package com.imooc.miaosha.access;

import com.imooc.miaosha.domain.MiaoshaUser;

/**
 * @author laowang
 * @date 2018/12/6 7:04 PM
 * @Description:
 */
public class UserContext {

    private static ThreadLocal<MiaoshaUser> userHolder = new ThreadLocal<MiaoshaUser>();

    public static void setUser(MiaoshaUser user) {
        userHolder.set(user);
    }

    public static MiaoshaUser getUser() {
        return userHolder.get();
    }

}

