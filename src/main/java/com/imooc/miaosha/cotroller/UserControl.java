package com.imooc.miaosha.cotroller;

import com.imooc.miaosha.Result.CodeMsg;
import com.imooc.miaosha.Result.Result;
import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author langwang
 * @date 2018/8/23 下午5:19
 * @Description: TODO
 */
@Controller
@RequestMapping("/api/user")
public class UserControl {
    private static Logger logger = LoggerFactory.getLogger(UserControl.class);


    @Autowired
    OrderService orderService;

    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> list(Model model, MiaoshaUser user){
        logger.info("测试热部署");
        return Result.success(user);
    }

    @RequestMapping("/test/inser/order")
    @ResponseBody
    public Result test(){
        Long a = 9L;

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsId(12L);
        orderInfo.setGoodsName("testName");
        orderInfo.setGoodsPrice(BigDecimal.valueOf(8));
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(Long.valueOf("3456789"));

        orderService.insert(orderInfo);
        logger.info("return id : {}",orderInfo.getId());
        return Result.success("success");
    }

}
