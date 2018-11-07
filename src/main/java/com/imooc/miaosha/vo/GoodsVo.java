package com.imooc.miaosha.vo;

import com.imooc.miaosha.domain.Goods;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yifan
 * @date 2018/7/23 上午9:19
 */
public class GoodsVo extends Goods {

    /** 库存数量*/
    private Integer stockCount;

    /** 秒杀开始时间*/
    private Date startDate;

    /** 秒杀结束时间*/
    private Date endDate;

    /** 秒杀价*/
    private BigDecimal miaoshaPrice;

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getMiaoshaPrice() {
        return miaoshaPrice;
    }

    public void setMiaoshaPrice(BigDecimal miaoshaPrice) {
        this.miaoshaPrice = miaoshaPrice;
    }
}
