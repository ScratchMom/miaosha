package com.imooc.miaosha.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


/**
 * @author laowang
 * @date 2018/12/4 7:50 PM
 * @Description:
 */
@Configuration
public class MQConfig {
    public static final String QUEUE = "queue";
    public static final String MIAOSHA_QUEUE = "miaosha.queue";


    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String HEADERS_QUEUE = "headers.queue";

    public static final String TOPIC_EXCHANGE = "topic.exchange";
    public static final String FANOUT_EXCHANGE = "fanout.exchange";
    public static final String HEADERS_EXCHANGE = "headers.exchange";



    @Bean
    public Queue queue () {
        return new Queue(MIAOSHA_QUEUE,true);
    }


    @Bean
    public Queue queue1 () {
        return new Queue(TOPIC_QUEUE1,true);
    }

    @Bean
    public Queue queue2 () {
        return new Queue(TOPIC_QUEUE2,true);
    }

    /**
     * topic 模式 交换机Exchange
     * 先把消息放到exchange，然后exchange再把消息放到queue中（通过）
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(queue1()).to(topicExchange()).with("topic.key1");
    }

    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(queue2()).to(topicExchange()).with("topic.#");
    }

    /**
     * Fanout 模式 交换机Exchange
     *
     * 广播，都能收到
     *
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder.bind(queue1()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBinding2() {
        return BindingBuilder.bind(queue2()).to(fanoutExchange());
    }

    /**
     * Header 模式 交换机Exchange
     *
     *
     */

    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGE);
    }
    @Bean
    public Queue headersQueue () {
        return new Queue(HEADERS_QUEUE,true);
    }
    @Bean
    public Binding headersBinding() {
        Map<String,Object> map  = new HashMap<>();
        map.put("header1","value1");
        map.put("header2","value2");
        return BindingBuilder.bind(headersQueue()).to(headersExchange()).whereAll(map).match();
    }
}
