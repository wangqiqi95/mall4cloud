package com.mall4j.cloud.user.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.product.vo.ScoreProductVO;
import com.mall4j.cloud.common.cache.constant.ProductCacheNames;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.user.model.ScoreProductStockSynLog;
import com.mall4j.cloud.user.service.ScoreProductArrivalService;
import com.mall4j.cloud.user.service.ScoreProductStockSynLogService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 积分礼品到货提醒定时任务
 * @author Grady_Lu
 */
@Component
public class ScoreProductArrivalTask {
    private static final Logger log = LoggerFactory.getLogger(ScoreProductArrivalTask.class);

    @Autowired
    ScoreProductArrivalService scoreProductArrivalService;
    @Autowired
    private ScoreProductStockSynLogService scoreProductStockSynLogService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @XxlJob("scoreProductArrivalTask")
    public void scoreProductArrivalTask(){
        log.info("开始执行积分礼品到货提醒定时任务》》》》》》》》》》》》》》》》》》");

        try {
            //捞取当前时间往前10分钟的数据
            Date date = new Date();
            Date startTime = DateUtil.offset(date, DateField.MINUTE, -10);
            LambdaQueryWrapper<ScoreProductStockSynLog> wrapper = new LambdaQueryWrapper<>();
            wrapper.ge(ScoreProductStockSynLog::getCreateTime,startTime);

            List<ScoreProductStockSynLog> stockSynLogList = scoreProductStockSynLogService.list(wrapper);
            if(CollectionUtil.isNotEmpty(stockSynLogList)){
                //去重
                ArrayList<ScoreProductStockSynLog> resultList = stockSynLogList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getStoreCode() + ";" + o.getSpuId()))), ArrayList::new));
                log.info("本次捞取的数据: {},数量为：{}" , Json.toJsonString(stockSynLogList),stockSynLogList.size());
                //发送消息
                resultList.forEach(scoreProductLog ->{
                    scoreProductArrivalService.ScoreProductArrival(scoreProductLog);

                    //清除缓存记录
                    ScoreProductVO scoreProductVO = new ScoreProductVO();
                    BeanUtils.copyProperties(scoreProductLog, scoreProductVO);
                    redisTemplate.opsForSet().remove(ProductCacheNames.SCORE_PRODUCT_LIST, Json.toJsonString(scoreProductVO));
                });


                //消息发送完成后,删除记录
                List<Long> ids = stockSynLogList.stream().map(ScoreProductStockSynLog::getId).collect(Collectors.toList());
                scoreProductStockSynLogService.removeByIds(ids);
            }

        }catch (Exception e){
            log.error("执行积分礼品到货提醒定时任务错误,异常信息为：{}", e.getMessage());
        }

        log.info("执行积分礼品到货提醒定时任务结束》》》》》》》》》》》》》》》》》》");
    }

}
