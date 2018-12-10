package com.imooc.miaosha.config;

import com.imooc.miaosha.access.UserContext;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author yifan
 * @date 2018/7/21 下午10:11
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    private static Logger logger = LoggerFactory.getLogger(UserArgumentResolver.class);

    @Autowired
    MiaoshaUserService miaoshaUserService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz = parameter.getParameterType();
        logger.info("clazz name === " + clazz.getSimpleName());
        return MiaoshaUser.class == clazz;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        return UserContext.getUser();
//        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
//        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
//
//        String paramTooken = request.getParameter(MiaoshaUserService.COOKIE_NAME_TOKEN);
//        String cookieTooken = getCookieValue(request,MiaoshaUserService.COOKIE_NAME_TOKEN);
//
//        if (StringUtils.isEmpty(cookieTooken) && StringUtils.isEmpty(paramTooken)) {
//            logger.info("token 传入失败");
//            return null;
//        }
//        String token = StringUtils.isEmpty(paramTooken) ? cookieTooken : paramTooken;
//        return miaoshaUserService.getByToken(token,response);
    }
//    private String getCookieValue (HttpServletRequest request, String cookieName) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies == null || cookies.length <= 0) {
//            return null;
//        }
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals(cookieName)) {
//                logger.info("cookie value === " + cookie.getValue());
//                return cookie.getValue();
//            }
//        }
//        return null;
//    }
}
