package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class RedissonManager {

    private Config config;
    private Redisson redisson = null;
    private static String host = PropertiesUtil.getProperty("redis.host");
    private static Integer port = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));
  //  private static Integer port2 = Integer.parseInt(PropertiesUtil.getProperty("redis2.port"));
    private static String passwd = PropertiesUtil.getProperty("redis.passwd");
    //private static String passwd2 = PropertiesUtil.getProperty("redis2.passwd");

    @PostConstruct
    private void init(){

        try {
            config.useSingleServer().setAddress(new StringBuffer().append(host).append(port).toString()).setPassword(passwd);
            redisson = (Redisson) Redisson.create(config);

            log.info("redission 初始化成功 !!!");

        } catch (Exception e) {
            log.error("redission 初始化失败 error:{}", e);
        }

    }

    public Redisson getRedisson() {
        return redisson;
    }
}
