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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

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
        if (user == null || goodsId < 0) {
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.uuuid() + "salt");
        redisService.set(MiaoKey.getMiaoshPath, "" + user.getId() + "_" + goodsId, str);
        return str;
    }

    public BufferedImage createMiaoshaVerifyCode(MiaoshaUser user, long goodsId) {
        if (user == null || goodsId < 0) {
            return null;
        }

        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }

    public static void main(String[] args) {
        System.out.println(calc("1-8+9"));
    }

    /**
     * script引擎
     * @param exp
     * @return
     */
    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[] {'+', '-', '*'};
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    public boolean checkVerifyCode(MiaoshaUser user, long goodsId, int verifyCode) {
        if (user == null || goodsId < 0) {
            return false;
        }
        Integer oldCode =  redisService.get(MiaoKey.getMiaoshaVerifyCode, user.getId()+","+goodsId,Integer.class);
        if (oldCode == null || oldCode - verifyCode !=0) {
            return false;
        }
        redisService.delete(MiaoKey.getMiaoshaVerifyCode, user.getId()+","+goodsId);
        return true;
    }
}
