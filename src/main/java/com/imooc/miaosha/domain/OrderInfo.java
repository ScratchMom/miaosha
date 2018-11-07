package com.imooc.miaosha.domain;

import java.math.BigDecimal;
import java.util.Date;

public class OrderInfo {
    /** */
    private Long id;

    /** 用户id*/
    private Long userId;

    /** 商品id*/
    private Long goodsId;

    /** 收货地址id*/
    private Long deliveryAddrId;

    /** 冗余过来的商品名称*/
    private String goodsName;

    /** 下单的商品数量*/
    private Integer goodsCount;

    /** 实际下单价格*/
    private BigDecimal goodsPrice;

    /** 下单渠道，1pc，2Android，3iOS*/
    private Integer orderChannel;

    /** 订单状态：0，未支付；1，已支付；2，已发货；3，已收货*/
    private Integer status;

    /** 下单时间*/
    private Date createDate;

    /** 支付时间*/
    private Date payDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getDeliveryAddrId() {
        return deliveryAddrId;
    }

    public void setDeliveryAddrId(Long deliveryAddrId) {
        this.deliveryAddrId = deliveryAddrId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getOrderChannel() {
        return orderChannel;
    }

    public void setOrderChannel(Integer orderChannel) {
        this.orderChannel = orderChannel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
}