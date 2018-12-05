package com.imooc.miaosha.rabbitmq;

import com.imooc.miaosha.domain.MiaoshaUser;
import lombok.Data;

/**
 * @author laowang
 * @date 2018/12/5 4:35 PM
 * @Description:
 */
@Data
public class MiaoshaMessage {

    private MiaoshaUser miaoshaUser;
    private long goodsId;
}
