package com.imooc.miaosha.vo;

import com.imooc.miaosha.domain.OrderInfo;
import lombok.Data;

/**
 * @author laowang
 * @date 2018/11/21 7:36 PM
 * @Description:
 */

@Data
public class OrderDetailVO {

    private GoodsVo goodsVo;
    private OrderInfo orderInfo;

}
