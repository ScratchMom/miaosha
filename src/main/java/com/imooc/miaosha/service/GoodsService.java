package com.imooc.miaosha.service;

import com.imooc.miaosha.dao.GoodsDao;
import com.imooc.miaosha.domain.MiaoshaGoods;
import com.imooc.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yifan
 * @date 2018/7/23 上午9:32
 */
@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo () {
        List<GoodsVo> goodsVos = goodsDao.listGoodsVo();
        if (goodsVos == null) {
            return null;
        }
        return goodsVos;
    }

    public GoodsVo getGoodsDetail (long goodsId) {
        return goodsDao.getGoodsDetail(goodsId);
    }

    public void reduceStock(GoodsVo goodsVo) {
        MiaoshaGoods g = new MiaoshaGoods();
        g.setGoodsId(goodsVo.getId());
        goodsDao.reduceStock(g);
    }
}
