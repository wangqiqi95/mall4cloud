package com.mall4j.cloud.product.listener.synczhlsdata;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.product.mapper.SkuMapper;
import com.mall4j.cloud.product.mapper.SpuMapper;
import com.mall4j.cloud.product.model.Sku;
import com.mall4j.cloud.product.service.ZhlsCommodityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description 同步有数铺货监听
 * @Author axin
 * @Date 2023-05-16
 **/
@Slf4j
@Component
@RocketMQMessageListener(topic = RocketMqConstant.SYNC_ZHLS_PRODUCT_TOPIC,
        consumerGroup = "GID_" + RocketMqConstant.SYNC_ZHLS_IPH_PRODUT_GROUP,
        selectorExpression = RocketMqConstant.SYNC_ZHLS_IPH_PRODUT_TAG
)
public class SyncZhlsIphProductListener implements RocketMQListener<SpuDTO> {
    @Autowired
    private ZhlsCommodityService zhlsCommodityService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SpuMapper spuMapper;

    @Override
    public void onMessage(SpuDTO spuDTO) {
        log.info("同步有数监听爱铺货mq入参:{}", JSON.toJSONString(spuDTO));
        if(Objects.nonNull(spuDTO) && StringUtils.isNotBlank(spuDTO.getSpuCode())) {
            SpuVO spuVO = spuMapper.getSpuByCode(spuDTO.getSpuCode());
            List<Sku> skus = skuMapper.selectList(Wrappers.lambdaQuery(Sku.class).eq(Sku::getSpuId, spuVO.getSpuId()));
            List<Long> skuIds = skus.stream().map(Sku::getSkuId).collect(Collectors.toList());
            zhlsCommodityService.skuInfoAdd(skuIds);
        }
    }
}
