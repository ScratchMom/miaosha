package com.imooc.miaosha.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author yifan
 * @date 2018/7/19 下午2:48
 */
@Service
public class RedisPoolFactory {

    @Autowired
    RedisConfig redisConfig;

    @Bean   // 将这个bean注入到/加载到 Spring容器里
    public JedisPool jedisPoolFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);

        JedisPool jedisPool = new JedisPool(poolConfig,redisConfig.getHost(),
                redisConfig.getPort(),redisConfig.getTimeout()*1000,
                redisConfig.getPassword(),0);
        return jedisPool;
    }
}
