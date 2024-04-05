package com.mall4j.cloud.product.listener.synczhlsdata;
import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.mall4j.cloud.common.product.vo.CategoryVO;
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
 * @Description 同步有数分类删除监听
 * @Author axin
 * @Date 2023-05-16
 **/
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.SYNC_ZHLS_PRODUCT_TOPIC,
        consumerGroup = "GID_" + RocketMqConstant.SYNC_ZHLS_CATEGORY_DEL_GROUP,
        selectorExpression = RocketMqConstant.SYNC_ZHLS_CATEGORY_DEL_TAG
)
public class SyncZhlsCategoryDelListener implements RocketMQListener<CategoryVO> {

    @Autowired
    private ZhlsCommodityService zhlsCommodityService;

    @Override
    public void onMessage(CategoryVO categoryVO) {
        log.info("同步有数监听分类删除mq入参:{}", JSON.toJSONString(categoryVO));
        List<Category> list = Lists.newArrayList();
        Category category = new Category();
        category.setCategoryId(categoryVO.getCategoryId());
        category.setShopId(categoryVO.getShopId());
        category.setParentId(categoryVO.getParentId());
        category.setName(categoryVO.getName());
        category.setDesc(categoryVO.getDesc());
        category.setPath(categoryVO.getPath());
        category.setStatus(categoryVO.getStatus());
        category.setIcon(categoryVO.getIcon());
        category.setImgUrl(categoryVO.getImgUrl());
        category.setLevel(categoryVO.getLevel());
        category.setDeductionRate(categoryVO.getDeductionRate());
        category.setSeq(categoryVO.getSeq());
        list.add(category);

        zhlsCommodityService.productCategoriesAdd(list,1);
    }
}
