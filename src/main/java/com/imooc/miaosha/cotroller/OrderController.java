package com.imooc.miaosha.cotroller;

import com.imooc.miaosha.Result.CodeMsg;
import com.imooc.miaosha.Result.Result;
import com.imooc.miaosha.domain.Goods;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import com.imooc.miaosha.vo.OrderDetailVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author laowang
 * @date 2018/11/21 7:36 PM
 * @Description:    订单
 */
@Controller
@RequestMapping("api/order")
public class OrderController {
    Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    RedisService redisService;
    @Autowired
    OrderService orderService;
    @Autowired
    GoodsService goodsService;


//    @NeedLogin 拦截器
    /**
     * 自定义NeedLogin注解
     * https://blog.csdn.net/qq_35348457/article/details/80534682
     */
    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVO> info(Model model, MiaoshaUser user, @RequestParam("orderId") long orderId) {

        logger.info("获取订单详情");
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if (orderInfo == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = orderInfo.getGoodsId();
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);

        OrderDetailVO detail = new OrderDetailVO();
        detail.setGoodsVo(goodsVo);
        detail.setOrderInfo(orderInfo);
        return Result.success(detail);
    }

}
