package com.imooc.miaosha.cotroller;

import com.imooc.miaosha.Result.CodeMsg;
import com.imooc.miaosha.Result.Result;
import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author langwang
 * @date 2018/8/23 下午5:19
 * @Description: TODO
 */
@Controller
@RequestMapping("/api/user")
public class UserControl {
    private static Logger logger = LoggerFactory.getLogger(UserControl.class);

    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> list(Model model, MiaoshaUser user){
        logger.info("测试热部署");
        return Result.success(user);
    }

}
