package com.imooc.miaosha.redis;

import com.alibaba.fastjson.JSON;
import com.imooc.miaosha.domain.MiaoshaUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author yifan
 * @date 2018/7/19 下午12:25
 */
@Service
public class RedisService { // 用RedisService提供redis服务

    Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    JedisPool jedisPool;


    /**
     * 获取redis
     * @param keyPrefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T>T get(KeyPrefix keyPrefix, String key,Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();    // jedisPool是一个连接池，用完一定要释放掉
            // 真正的key
            String realKey = keyPrefix.getPrefix() + key;
            String str = jedis.get(realKey);
//            logger.info("realkey :{}",realKey);
//            logger.info("要被转换的字符串str :{}",str);
//            logger.info("clazz :{}" + clazz.getName());
            T t = stringToBean(str,clazz);
            return t;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 存入redis
     * @param keyPrefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix keyPrefix, String key,T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();    // jedisPool是一个连接池，用完一定要释放掉
            String str = beanToString(value);
            if (str == null || str.length() <= 0) {
                return false;
            }
            String realKey = keyPrefix.getPrefix() + key; // 真正的key
            int seconds = keyPrefix.expireSeconds();      // 过期时间
            if (seconds == -1) {
                jedis.set(realKey,str);
            } else {
                jedis.setex(realKey,seconds,str);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 删除
     * @param prefix
     * @param key
     * @return
     */
    public boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            Long ret = jedis.del(realKey);
            return ret > 0;
        } finally  {
            returnToPool(jedis);
        }

    }

    /**
     * 判断一个key是否存在
     * @param keyPrefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean exists(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();    // jedisPool是一个连接池，用完一定要释放掉
            String realKey = keyPrefix.getPrefix() + key; // 真正的key
            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }


    /**
     * 进行加1操作
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();    // jedisPool是一个连接池，用完一定要释放掉
            String realKey = prefix.getPrefix() + key; // 真正的key
            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 进行减1操作
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();    // jedisPool是一个连接池，用完一定要释放掉
            String realKey = prefix.getPrefix() + key; // 真正的key
            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }



    public static <T> String beanToString (T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    @SuppressWarnings("unchecked")
    public static  <T>T stringToBean (String str, Class<T> clazz) {
        if (str == null || str.length() <=0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
    }

    public void  returnToPool (Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


}
