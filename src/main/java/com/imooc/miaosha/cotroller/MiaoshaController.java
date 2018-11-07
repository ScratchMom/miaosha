package com.imooc.miaosha.cotroller;

import com.imooc.miaosha.Result.CodeMsg;
import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author langwang
 * @date 2018/8/12 下午8:43
 * @Description: TODO
 */

@Controller
@RequestMapping("/api/miaosha")
public class MiaoshaController {
    private static Logger logger = LoggerFactory.getLogger(MiaoshaController.class);

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RequestMapping("/do_miaosha")
    public String list(Model model, MiaoshaUser user,
                       @RequestParam("goodsId") String goodId){
        logger.info("goodId == " + goodId);
        model.addAttribute("user",user);
        if (user == null) {
            return "login";
        }

        GoodsVo goodsVo = goodsService.getGoodsDetail(Long.parseLong(goodId));
        int stock = goodsVo.getStockCount();
        // 库存不足
        if (stock <= 0) {
            model.addAttribute("errorMsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            return "miaosha_fail";
        }

        // 判断是否已经秒杀到了 防止一个人秒杀多个商品
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),Long.parseLong(goodId));
        if (miaoshaOrder != null) {
            model.addAttribute("errorMsg", CodeMsg.REPEATE_MIAOSHA.getMsg());
            return "miaosha_fail";
        }
        // 减库存，下订单，写入秒杀订单 (原子操作，事物)
        OrderInfo orderInfo = miaoshaService.miaosha(user,goodsVo);
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods", goodsVo);

        return "order_detail";
    }

}
