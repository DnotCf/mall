package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

@Slf4j
public class RedisClientUtil {


    public static String set(String key, String value) {
        ShardedJedis jedis = null;
        String res = null;
        try {
            jedis = RedisPool.getResource();
            res = jedis.set(key, value);
        } catch (Exception e) {
            log.error("redis set key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return res;
        }
        RedisPool.returnResource(jedis);
        return res;
    }

    public static String get(String key) {
        ShardedJedis jedis = null;
        String res = null;

        try {
            jedis = RedisPool.getResource();
            res = jedis.get(key);
        } catch (Exception e) {
            log.error("redis get key:{} value:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return res;
        }
        RedisPool.returnResource(jedis);
        return res;
    }

    public static String getset(String key,String value) {
        ShardedJedis jedis = null;
        String res = null;

        try {
            jedis = RedisPool.getResource();
            res = jedis.getSet(key, value);
        } catch (Exception e) {
            log.error("redis getset key:{} value:{},res:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return res;
        }
        RedisPool.returnResource(jedis);
        return res;
    }

    public static String set(String key,int seconds, String value) {
        ShardedJedis jedis = null;
        String res = null;

        try {
            jedis = RedisPool.getResource();
            res = jedis.setex(key, seconds, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return res;
        }
        RedisPool.returnResource(jedis);
        return res;
    }

    public static Long setExpir(String key, int seconds) {
        ShardedJedis jedis = null;
        Long res = null;
        try {
            jedis = RedisPool.getResource();
            res = jedis.expire(key, seconds);
        } catch (Exception e) {
            log.error("set expireTime:{} value:{} error", key,seconds, e);
            RedisPool.returnBrokenResource(jedis);
            return res;
        }
        RedisPool.returnResource(jedis);
        return res;
    }

    public static Long setnx(String key, String value) {
        ShardedJedis jedis = null;
        Long res = null;
        try {
            jedis = RedisPool.getResource();
            res = jedis.setnx(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return res;
        }
        RedisPool.returnResource(jedis);
        return res;
    }

    public static Long delete(String key) {
        ShardedJedis jedis = null;
        Long res = null;
        try {
            jedis = RedisPool.getResource();
            res = jedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} value:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return res;
        }
        RedisPool.returnResource(jedis);
        return res;
    }


}
