package com.imooc.miaosha.vo;

import com.imooc.miaosha.domain.MiaoshaUser;
import lombok.Data;

/**
 * @author laowang
 * @date 2018/11/16 3:38 PM
 * @Description:
 */
@Data
public class GoodsDetailVo {
    private int miaoshaStatus;
    private int remainSeconds;
    private GoodsVo goodsVo;
    private MiaoshaUser user;
}
