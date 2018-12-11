package com.imooc.miaosha.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.service.GoodsService;
import com.imooc.miaosha.service.MiaoshaService;
import com.imooc.miaosha.service.OrderService;
import com.imooc.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author laowang
 * @date 2018/12/4 7:50 PM
 * @Description:
 */

@Service
public class MQReceiver {
    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    RedisService redisService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receiveMiaoshaMsg(String message) {

        logger.info("receive miaosha message :{}",message);
        MiaoshaMessage miaoshaMessage = JSON.parseObject(message,MiaoshaMessage.class);
        long goodsId = miaoshaMessage.getGoodsId();
        MiaoshaUser user = miaoshaMessage.getMiaoshaUser();

        // 判断库存
        GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goodsVo.getStockCount();
        if (stock <= 0) {
            return;
        }
        // 判断是否已经秒杀到了 防止一个人秒杀多个商品
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodsId);
        if (miaoshaOrder != null) {
            return ;
        }
        // 减库存，下订单，写入秒杀订单 (原子操作，事物)
        miaoshaService.miaosha(user,goodsVo);

    }


//    @RabbitListener(queues = MQConfig.QUEUE)
//    public void receive(String message) {
//        logger.info("receive message :{}",message);
//    }
    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveQueue1(String message) {
        logger.info("receive topic.queue1 message :{}",message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveQueue2(String message) {
        logger.info("receive topic.queue2 message :{}",message);
    }
    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
    public void heardersQueue(byte[] message) {
        logger.info("receive header.queue message :{}",new String(message));
    }
}
