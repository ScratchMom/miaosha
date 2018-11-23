package com.imooc.miaosha.cotroller;

import com.alibaba.fastjson.JSON;
import com.imooc.miaosha.Result.CodeMsg;
import com.imooc.miaosha.Result.Result;
import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsDetailVo;
import com.imooc.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    /**
     * 秒杀
     * @param model
     * @param user
     * @param request
     * @param response
     * @return
     */

    /**
     *
     * GET POST区别
     * GET幂等 代表从服务端获取数据，无论触发多少次不会对服务端数据产生影响  <<<---反例--->>> <a href=""></a>   事故错误
     * post 对服务的提交数据，对服务端数据的变化产生影响
     */
    @RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> miaosha(Model model,
                                         MiaoshaUser user,
                                         @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user",user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        // 判断库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if (stock <= 0) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        // 判断是否已经秒杀到了 防止一个人秒杀多个商品
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if (miaoshaOrder != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        // 减库存，下订单，写入秒杀订单 (原子操作，事物)
        OrderInfo orderInfo = miaoshaService.miaosha(user,goodsVo);
        return Result.success(orderInfo);

    }
}
