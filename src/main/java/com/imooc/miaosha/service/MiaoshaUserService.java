package com.imooc.miaosha.service;

import com.alibaba.fastjson.JSON;
import com.imooc.miaosha.Result.CodeMsg;
import com.imooc.miaosha.dao.MiaoshaUserDao;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.exception.GlobalException;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yifan
 * @date 2018/7/20 上午11:48
 */
@Service
public class MiaoshaUserService {

    private static Logger logger = LoggerFactory.getLogger(MiaoshaUserService.class);
    public static final String COOKIE_NAME_TOKEN = "token";

    @Autowired
    MiaoshaUserDao miaoshaUserDao;
    @Autowired
    RedisService redisService;

    /**
     * 对象缓存
     * @param id
     * @return
     */
    public MiaoshaUser getById(long id) {
        // 取缓存
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById,""+id,MiaoshaUser.class);
        if (user != null) {
            return user;
        }
        // 取数据库
        user = miaoshaUserDao.getById(id);
        if (user != null) {
            redisService.set(MiaoshaUserKey.getById,"" + id,user);
        }
        return user;
    }

    public boolean updatePassword (String token, long id, String passwordNew) {
        // 取user
        MiaoshaUser user = getById(id);
        if (user == null) {
            throw new  GlobalException(CodeMsg.MOBIEL_NOT_EXIST);
        }
        // 更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(passwordNew,user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);

        // 处理缓存
        redisService.delete(MiaoshaUserKey.getById,""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token,token,user);
        return true;
    }

    public String login (HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
//            return CodeMsg.SERVER_ERROR;
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();

        // 判断手机号是否存在
        MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile));
        if (miaoshaUser == null) {
//            return CodeMsg.MOBIEL_NOT_EXIST;
            throw new GlobalException(CodeMsg.MOBIEL_NOT_EXIST);

        }

        // 验证密码
        String dbPass = miaoshaUser.getPassword();
        String saltDb = miaoshaUser.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass,saltDb);

        if (!calcPass.equals(dbPass)) {
//            return CodeMsg.PASSWORD_ERROR;
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);

        }

        // 生成cookie
        String token = UUIDUtil.uuuid();
        addCookie(miaoshaUser, token, response);
        return token;
    }

    public MiaoshaUser getByToken (String token, HttpServletResponse response) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token,token,MiaoshaUser.class);

        if (user != null) {
            // 延长有效期
            addCookie(user,token, response);
        }
//        logger.info("token === " + token);
//        logger.info("user === " + JSON.toJSONString(user));
        return user;
    }

    private void addCookie (MiaoshaUser miaoshaUser, String token, HttpServletResponse response) {

        redisService.set(MiaoshaUserKey.token,token,miaoshaUser);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN,token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/ ");
        response.addCookie(cookie);
    }
}




















