package com.mall4j.cloud.seckill.manager;

import cn.hutool.core.util.IdUtil;
import com.mall4j.cloud.common.cache.constant.SeckillCacheNames;
import com.mall4j.cloud.common.cache.util.CacheManagerUtil;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.seckill.service.SeckillSkuService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @author FrozenWatermelon
 */
@Service
public class SeckillCacheManager {

    /**
     * 秒杀限流前缀
     */
    private final static String SECKILL_LIMIT_PREFIX = "SECKILL_LIMIT_";

    /**
     * 秒杀路径前缀
     */
    private final static String SECKILL_PATH_PREFIX = "SECKILL_PATH_";

    private static final String REDISSON_LOCK_PREFIX = "redisson_lock:";

    private final static String SECKILL_SKU_STOCKS_PREFIX = "SECKILL_SKU_STOCKS_";

    @Autowired
    private SeckillSkuService seckillSkuService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CacheManagerUtil cacheManagerUtil;

    public String createOrderPath(Long userId) {

        String limitKey = SECKILL_LIMIT_PREFIX + userId;
        int maxCount = 5;

        // 秒杀次数+1
        long seckillNum = RedisUtil.incr(limitKey, 1);

        // 5秒只能提交5次请求
        if (seckillNum == 1) {
            // 5秒后失效
            RedisUtil.expire(limitKey, 5);
        }
        if (seckillNum >= maxCount) {
            // 请求过频繁，请稍后再试
            throw new LuckException("请求过频繁，请稍后再试");
        }
        String orderPath = IdUtil.simpleUUID();

        // 保存秒杀路径，5分钟这个路径就失效
        RedisUtil.set(getPathKey(userId), orderPath, 300);

        return orderPath;
    }

    public void checkOrderPath(Long userId, String orderPath) {
        String cacheOrderPath = RedisUtil.get(getPathKey(userId));

        if (!Objects.equals(cacheOrderPath,orderPath)) {
            // 订单已过期，请重新选择商品进行秒杀
            throw new LuckException("订单已过期，请重新选择商品进行秒杀");
        }
    }

    private String getPathKey(Long userId) {
        return SECKILL_PATH_PREFIX + userId;
    }

    public void decrSeckillSkuStocks(Long seckillSkuId, Integer prodCount) {
        String key = SECKILL_SKU_STOCKS_PREFIX + seckillSkuId;

        Long cacheStocks = RedisUtil.getLongValue(key);

        if (cacheStocks != null && cacheStocks <= 0) {
            throw new LuckException("商品库存不足");
        }
        // 如果没有库存就缓存一个库存
        if (cacheStocks == null || RedisUtil.getExpire(key) < 1) {

            // 加锁，防止缓存击穿
            RLock rLock = redissonClient.getLock(REDISSON_LOCK_PREFIX + ":getSeckillSkuStocks");
            try {
                int lockWait = 10;
                if (rLock.tryLock(lockWait,lockWait, TimeUnit.SECONDS)){
                    // 再获取一遍缓存
                    cacheStocks = RedisUtil.getLongValue(key);
                    if (cacheStocks == null) {
                        cacheManagerUtil.evictCache(SeckillCacheNames.SECKILL_SKU_BY_ID, String.valueOf(seckillSkuId));
                        Integer seckillStocks = seckillSkuService.getBySeckillSkuId(seckillSkuId).getSeckillStocks();
                        // 秒杀实时性要求比较高，但是不能缓存时间太短呀，不然锁竞争很激烈的呀，缓存15秒
                        if (seckillStocks > 0) {
                            RedisUtil.setLongValue(key, Long.valueOf(seckillStocks),-1);
                        } else {
                            RedisUtil.setLongValue(key, 0L, 30);
                        }
                        cacheStocks = Long.valueOf(seckillStocks);
                    }
                } else {
                    // 网络异常
                    throw new LuckException("网络异常");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rLock.isLocked()) {
                        rLock.unlock();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        if (cacheStocks == null || cacheStocks < prodCount || RedisUtil.decr(key, prodCount) < 0) {
            RedisUtil.expire(key,30);
            // 本轮商品已被秒杀完毕，还有用户未支付，还有机会哟
            throw new LuckException("本轮商品已被秒杀完毕");
        }

    }
}
