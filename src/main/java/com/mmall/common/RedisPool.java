package com.mmall.common;

import com.google.common.collect.Lists;
import com.mmall.util.PropertiesUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.List;


public class RedisPool {

    private static ShardedJedisPool jedisPool;

    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("jedis.maxtotal", "20"));
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("jedis.maxidel", "20"));
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("jedis.minidel", "0"));
    private static Boolean testOnBorrow = Boolean.valueOf(PropertiesUtil.getProperty("jedis.testOnBorrow")); //在连接redis实例的时候，是否进行验证操作，如果赋值为true，则得到的redis实例肯定可用
    private static Boolean testOnReturn = Boolean.valueOf(PropertiesUtil.getProperty("jedis.testOnReturn"));//在放回redis实例的时候，是否进行验证操作，如果赋值为true，则放回的redis实例肯定可用
    private static String host = PropertiesUtil.getProperty("redis.host");
    private static Integer port = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));
    private static Integer port2 = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));
    private static String passwd = PropertiesUtil.getProperty("redis.passwd");
    private static String passwd2 = PropertiesUtil.getProperty("redis2.passwd");
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setBlockWhenExhausted(true); //连接耗尽是 false 会抛出异常，true阻塞直到超时。默认为true

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        JedisShardInfo j1 = new JedisShardInfo(host, port, 2000);
        j1.setPassword(passwd);
        JedisShardInfo j2 = new JedisShardInfo(host, port2, 2000);
        j2.setPassword(passwd2);


        List<JedisShardInfo> jedisShardInfos = Lists.newArrayList();
        jedisShardInfos.add(j1);
        jedisShardInfos.add(j2);

        jedisPool = new ShardedJedisPool(config, jedisShardInfos, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);



    }

    public static ShardedJedis getResource()
    {
        return jedisPool.getResource();
    }

    //好连接 放回连接池
    public static void returnResource(ShardedJedis jedis) {
        jedisPool.returnResource(jedis);
    }

    //坏连接 放回连接池
    public static void returnBrokenResource(ShardedJedis jedis) {
        jedisPool.returnBrokenResource(jedis);

    }
}
