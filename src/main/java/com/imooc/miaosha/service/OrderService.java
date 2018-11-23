package com.imooc.miaosha.service;

import com.imooc.miaosha.dao.OrderDao;
import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.redis.OrderKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author langwang
 * @date 2018/8/12 下午8:56
 * @Description: TODO
 */
@Service
public class OrderService {
    Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    OrderDao orderDAO;


    @Autowired
    RedisService redisService;

    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(Long userId, long goodId) {
//       return orderDAO.getMiaoshaOrderByUserIdGoodsId(userId,goodId);
       return redisService.get(OrderKey.getMiaoshOrderByUidGid,"" + userId + "_" +goodId,MiaoshaOrder.class);
    }

    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVo goodsVo) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsId(goodsVo.getId());
        orderInfo.setGoodsName(goodsVo.getGoodsName());
        orderInfo.setGoodsPrice(goodsVo.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDAO.insert(orderInfo);

        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goodsVo.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        orderDAO.insertMiaoshaOrder(miaoshaOrder);
        logger.info("用户 :{},插入order_info的id :{}",new Object[]{user.getId(),orderInfo.getId()});

        redisService.set(OrderKey.getMiaoshOrderByUidGid,""+user.getId()+"_"+goodsVo.getId(),miaoshaOrder);
        return orderInfo;
    }

    public long insert (OrderInfo orderInfo) {
        return orderDAO.insert(orderInfo);
    }

    public OrderInfo getOrderById(long orderId) {
        return orderDAO.getOderById(orderId);
    }
}
