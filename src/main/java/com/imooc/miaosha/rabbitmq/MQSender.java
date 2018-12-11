package com.imooc.miaosha.rabbitmq;

import com.imooc.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author laowang
 * @date 2018/12/4 7:49 PM
 * @Description:
 */
@Service
public class MQSender {
    private static Logger logger = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    RabbitTemplate rabbitTemplate;



    public void sendMiaoshMessage(MiaoshaMessage miaoshaMessage) {
        String msg = RedisService.beanToString(miaoshaMessage);
        logger.info("send miaoshaMessage  : {}", msg);
        rabbitTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
    }



    /**
     * direct 模式 交换机Exchange
     *
     * @param message
     */
//    public void send(Object message) {
//
//        String msg = RedisService.beanToString(message);
//        logger.info("send message : {}", msg);
//        rabbitTemplate.convertAndSend(MQConfig.QUEUE, msg);
//    }

    /**
     * topic 模式 交换机Exchange
     *
     * @param message
     */
    public void sendTopic(Object message) {

        String msg = RedisService.beanToString(message);
        logger.info("send topic message : {}", msg);
        rabbitTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", message +"1");
        rabbitTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", message + "2");
    }

    /**
     * Fanout 模式 交换机Exchange
     *
     * @param message
     */
    public void sendFanout(Object message) {

        String msg = RedisService.beanToString(message);
        logger.info("send message : {}", msg);
        rabbitTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"", msg);

    }

    /**
     * headers 模式 交换机Exchange
     *
     * @param message
     */
    public void sendHeaders(Object message) {

        String msg = RedisService.beanToString(message);
        logger.info("send message : {}", msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("header1","value1");
        properties.setHeader("header2","value2");
        Message obj = new Message(msg.getBytes(),properties);
        rabbitTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE,"", obj);

    }
}
