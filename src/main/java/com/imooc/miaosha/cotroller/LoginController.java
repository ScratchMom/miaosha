package com.imooc.miaosha.cotroller;

import com.imooc.miaosha.Result.Result;
import com.imooc.miaosha.service.MiaoshaUserService;
import com.imooc.miaosha.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author yifan
 * @date 2018/7/19 下午9:46
 */
@Controller
@RequestMapping("/api/login")
public class LoginController {
    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    MiaoshaUserService miaoshaUserService;

    /**
     * 跳转到login页面
     * @return
     */
    @RequestMapping("/do_login")
    @ResponseBody
    public Result<String> doLogin(@Validated LoginVo loginVo, HttpServletResponse response){

        logger.info("loginVo === " + loginVo.toString());
        // 登录
        String token = miaoshaUserService.login(response, loginVo);
        return Result.success(token);
    }

    @RequestMapping("/to_login")
    public String toLogin(LoginVo loginVo) {
        return "login";
    }

}
