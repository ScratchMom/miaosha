package com.imooc.miaosha.service;

import com.imooc.miaosha.domain.MiaoshaOrder;
import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.domain.OrderInfo;
import com.imooc.miaosha.domain.User;
import com.imooc.miaosha.redis.MiaoKey;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.util.MD5Util;
import com.imooc.miaosha.util.UUIDUtil;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author langwang
 * @date 2018/8/12 下午8:56
 * @Description: TODO
 */
@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goodsVo) {

        // 减库存
        boolean success = goodsService.reduceStock(goodsVo);
        if (success) {
            // 下订单，写入秒杀订单
            OrderInfo orderInfo = orderService.createOrder(user,goodsVo);
            return orderInfo;
        } else {
            setGoodsOver(goodsVo.getId());
        }
        return null;
    }

    private void setGoodsOver(Long id) {
        redisService.set(MiaoKey.isGoodsOver,"" + id ,true);
    }

    private boolean getGoodsOver(Long id) {
        return redisService.exists(MiaoKey.isGoodsOver,"" + id);
    }


    /**
     *
     * @param userId
     * @param goodsId
     * @return orderId 成功,0 排队中,-1 失败
     */
    public long getMiaoshaResult(Long userId, long goodsId) {

        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(userId,goodsId);
        if (miaoshaOrder !=null) {
            return miaoshaOrder.getOrderId();
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public boolean checkPath(MiaoshaUser user, long goodsId, String path) {
        if (user == null || path == null) {
            return false;
        }
        String oldPath = redisService.get(MiaoKey.getMiaoshPath, "" + user.getId() + "_" + goodsId,String.class);
        return path.equals(oldPath);
    }


    public String createMiaoshaPath(MiaoshaUser user, long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuuid() + "salt");
        redisService.set(MiaoKey.getMiaoshPath, "" + user.getId() + "_" + goodsId, str);
        return str;
    }
}
