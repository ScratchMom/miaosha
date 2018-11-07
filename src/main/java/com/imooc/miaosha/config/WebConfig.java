package com.imooc.miaosha.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author yifan
 * @date 2018/7/21 下午10:00
 */
@Configuration  // 是一个配置
public class WebConfig extends WebMvcConfigurerAdapter {    // 自定参数解析器

    private static Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Autowired
    UserArgumentResolver userArgumentResolver;

    /**
     * 添加解析器来支持自定义控制器方法参数类型。
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        logger.info(" 自定参数解析器");
        argumentResolvers.add(userArgumentResolver);
    }
}
