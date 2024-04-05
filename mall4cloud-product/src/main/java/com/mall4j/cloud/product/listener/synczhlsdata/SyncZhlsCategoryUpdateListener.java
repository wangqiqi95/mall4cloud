package com.mall4j.cloud.product.listener.synczhlsdata;

import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.product.model.Category;
import com.mall4j.cloud.product.service.ZhlsCommodityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description 同步有数分类更新监听
 * @Author axin
 * @Date 2023-05-16
 **/
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.SYNC_ZHLS_PRODUCT_TOPIC,
        consumerGroup = "GID_" + RocketMqConstant.SYNC_ZHLS_CATEGORY_UPDATE_GROUP,
        selectorExpression = RocketMqConstant.SYNC_ZHLS_CATEGORY_UPDATE_TAG
)
public class SyncZhlsCategoryUpdateListener implements RocketMQListener<Category> {

    @Autowired
    private ZhlsCommodityService zhlsCommodityService;

    @Override
    public void onMessage(Category category) {
        log.info("同步有数监听分类保存mq入参:{}", JSON.toJSONString(category));
        List<Category> list = Lists.newArrayList();
        list.add(category);
        zhlsCommodityService.productCategoriesAdd(list,0);
    }
}
