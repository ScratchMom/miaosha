package com.imooc.miaosha.cotroller;

import com.alibaba.fastjson.JSON;
import com.imooc.miaosha.Result.CodeMsg;
import com.imooc.miaosha.Result.Result;
import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.rabbitmq.MiaoshaMessage;
import com.imooc.miaosha.redis.GoodsKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsDetailVo;
import com.imooc.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author langwang
 * @date 2018/8/12 下午8:43
 * @Description: TODO
 */

@Controller
@RequestMapping("/api/miaosha")
public class MiaoshaController implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(MiaoshaController.class);

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender sender;

    private Map<Long,Boolean> localOverMap = new HashMap<Long, Boolean>();


    /**
     * 系统初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        List<GoodsVo> goodsVos = goodsService.listGoodsVo();
        if (goodsVos == null) {
            return;
        }
        for (GoodsVo goodsVo : goodsVos) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock,"" + goodsVo.getId(),goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(),false);
        }
    }

    /**
     * 秒杀
     * @param model
     * @param user
     * @param request
     * @param response
     * @return
     */

    /**
     * qps 1755
     *
     * GET POST区别
     * GET幂等 代表从服务端获取数据，无论触发多少次不会对服务端数据产生影响  <<<---反例--->>> <a href=""></a>   事故错误
     * post 对服务的提交数据，对服务端数据的变化产生影响
     */
    @RequestMapping(value = "/do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model,
                                         MiaoshaUser user,
                                         @RequestParam("goodsId") long goodsId) {
        model.addAttribute("user",user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        // 内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }


        // 预减库存
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock,"" + goodsId);

        if (stock < 0) {
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        // 判断是否已经秒杀到了 防止一个人秒杀多个商品
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if (miaoshaOrder != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

//        入队
        MiaoshaMessage miaoshaMessage = new MiaoshaMessage();
        miaoshaMessage.setGoodsId(goodsId);
        miaoshaMessage.setMiaoshaUser(user);
        sender.sendMiaoshMessage(miaoshaMessage);
        return Result.success(0);

//        // 判断库存
//        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
//        int stock = goodsVo.getStockCount();
//        if (stock <= 0) {
//            return Result.error(CodeMsg.MIAO_SHA_OVER);
//        }
//
//        // 判断是否已经秒杀到了 防止一个人秒杀多个商品
//        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
//        if (miaoshaOrder != null) {
//            return Result.error(CodeMsg.REPEATE_MIAOSHA);
//        }
//        // 减库存，下订单，写入秒杀订单 (原子操作，事物)
//        OrderInfo orderInfo = miaoshaService.miaosha(user,goodsVo);
//        return Result.success(orderInfo);

    }

    /**
     * 轮询
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model,
                          MiaoshaUser user,
                          @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        /**
         * orderId 成功
         * 0 排队中
         * -1 失败
         */
        Long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }


}
