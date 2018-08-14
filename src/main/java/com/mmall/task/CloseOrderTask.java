package com.mmall.task;

import com.mmall.common.Const;
import com.mmall.common.RedissonManager;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    IOrderService iOrderService;
    @Autowired
    RedissonManager redissonManager;

    @Scheduled(cron = ("0 */1 * * * ?"))
    public void closeOrder() {
        log.info("开始关闭订单");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close_order_task_hour", "2"));

        int locktimeout = Integer.parseInt(PropertiesUtil.getProperty("lock_time_out", "5000"));

        Long block = RedisClientUtil.setnx(Const.CLOSEORDERTASK, String.valueOf(System.currentTimeMillis() + locktimeout));

        if (block != null && block.intValue() == 1) {
            log.info("获取锁成功");
            closeorder(hour);
            return;
        } else {
            String res = RedisClientUtil.get(Const.CLOSEORDERTASK);
            if (StringUtils.isNotBlank(res) && System.currentTimeMillis() > Long.valueOf(res)) {

                String oldvalue = RedisClientUtil.getset(Const.CLOSEORDERTASK, String.valueOf(System.currentTimeMillis() + locktimeout));
                if (oldvalue == null || StringUtils.equals(res, oldvalue)) {
                    log.info("获取锁成功");
                    closeorder(hour);
                    return;
                }
                log.info("未成功获取锁");
            }
            log.info("未成功获取锁");
        }

    }

    private void closeorder(int hour) {
        RedisClientUtil.setExpir(Const.CLOSEORDERTASK, 5);
        iOrderService.closeOrderTask(hour);
        RedisClientUtil.delete(Const.CLOSEORDERTASK);
        log.info("关闭订单结束");
        log.info("+++++++++++++++++++++++++++");
    }

   // @Scheduled(cron = ("0 */1 * * * ?"))
    public void closeOrderTaskByResission() {
        RLock lock = redissonManager.getRedisson().getLock(Const.CLOSEORDERTASK);
        boolean getLock = false;
        try {
            if (getLock = lock.tryLock(0,5, TimeUnit.SECONDS)) {
                int hour = Integer.parseInt(PropertiesUtil.getProperty("close_order_task_hour", "2"));
                iOrderService.closeOrderTask(hour);
                log.info("关闭订单成功");
            }else {
                log.info("未获取到锁 锁名:{},当前线程:{}", Const.CLOSEORDERTASK, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("获取锁异常 ",e);
        }finally {
            if (!getLock) {
                return;
            }
            lock.unlock();
            log.info("关闭订单结束");
            log.info("+++++++++++++++++++++++++++");
        }
    }

}
