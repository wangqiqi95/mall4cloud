package com.mall4j.cloud.product.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.group.feign.GroupFeignClient;
import com.mall4j.cloud.api.seckill.feign.SeckillFeignClient;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.constant.SpuType;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.config.ProductConfigProperties;
import com.mall4j.cloud.product.constant.SpuStatus;
import com.mall4j.cloud.product.mapper.SpuMapper;
import com.mall4j.cloud.product.service.BrandService;
import com.mall4j.cloud.product.service.SpuOfflineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * spu信息
 *
 * @author lhd
 * @date 2021-07-03 15:27:24
 */
@Service
@Slf4j
public class SpuOfflineServiceImpl implements SpuOfflineService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private BrandService brandService;
    @Autowired
    private GroupFeignClient groupFeignClient;
    @Autowired
    private SeckillFeignClient seckillFeignClient;
    @Autowired
    private OnsMQTemplate syncZhlsProductTemplate;
    @Autowired
    private ProductConfigProperties productConfigProperties;

    @Override
    public void offlineSpuStatusAndActivity(Integer type, List<Long> spuIds, Long shopId, List<Long> categoryIds, Integer status) {
        // 查询出所有的启用商品进行修改
        List<SpuVO> spuList = spuMapper.getActivitySpuInfoByCategoryIdsAndShopId(type,spuIds,shopId,categoryIds,status);
        List<Long> seckillSpuIds = new ArrayList<>();
        List<Long> groupSpuIds = new ArrayList<>();
        for (SpuVO spuVO : spuList) {
            if (Objects.equals(spuVO.getSpuType(), SpuType.SECKILL.value())){
                spuVO.setSpuType(0);
                spuVO.setActivityId(0L);
                seckillSpuIds.add(spuVO.getSpuId());
            }
            if (Objects.equals(spuVO.getSpuType(), SpuType.GROUP.value())){
                spuVO.setSpuType(0);
                spuVO.setActivityId(0L);
                groupSpuIds.add(spuVO.getSpuId());
            }
            // 商品那边可能有平台下架和商家下架，其余都为0，商家下架
            if(type != 1){
               spuVO.setStatus(0);
               log.info("分类下架商品:{},操作人:{}", JSON.toJSONString(spuVO),
                       AuthUserContext.get()!=null?AuthUserContext.get().getUserId():"");
            }
        }
        if(CollectionUtil.isEmpty(spuList)){
            return;
        }
        spuMapper.updateBatch(spuList);
        // 下线秒杀、团购活动
        offlineActivityBySpuIds(seckillSpuIds, groupSpuIds);
        // 更新品牌商品数量
        spuIds = spuList.stream().map(SpuVO::getSpuId).collect(Collectors.toList());
        brandService.updateSpuCountBySpuIds(spuIds);

        // 有数商品同步下架
        if(productConfigProperties.getSyncZhlsData() && type != 1){
            SendResult sendResult = syncZhlsProductTemplate.syncSend(spuIds, RocketMqConstant.SYNC_ZHLS_PUT_OFF_SHELF_TAG);
            log.info("分类下架同步有数,msgId:{},入参：{}",sendResult.getMessageId(),JSON.toJSONString(spuIds));
        }
    }

    @Override
    public void offlineSpuStatusAndActivityByBrandId(List<Long> spuIds, Long brandId) {
        // 查询出所有的启用商品进行修改
        List<SpuVO> spuList = spuMapper.getActivitySpuInfoByCategoryIdsAndShopId(1, spuIds,null,null,null);
        List<Long> seckillSpuIds = new ArrayList<>();
        List<Long> groupSpuIds = new ArrayList<>();
        for (SpuVO spuVO : spuList) {
            spuVO.setBrandId(null);
            spuVO.setStatus(SpuStatus.OFF_SHELF.value());
            if (Objects.equals(spuVO.getSpuType(), SpuType.GROUP.value())){
                spuVO.setSpuType(0);
                spuVO.setActivityId(0L);
                groupSpuIds.add(spuVO.getSpuId());
            }
            if (Objects.equals(spuVO.getSpuType(), SpuType.SECKILL.value())){
                spuVO.setSpuType(0);
                spuVO.setActivityId(0L);
                seckillSpuIds.add(spuVO.getSpuId());
            }
        }
        if(CollectionUtil.isEmpty(spuList)){
            return;
        }
        spuMapper.updateBatchFieldSetNull(spuList);
        // 下线对应的活动
        offlineActivityBySpuIds(seckillSpuIds, groupSpuIds);
        // 更新品牌商品数量
        brandService.updateSpuCount(brandId);
    }


    private void offlineActivityBySpuIds(List<Long> seckillSpuIds, List<Long> groupSpuIds) {
        // 下线对应的活动
        if (CollectionUtil.isNotEmpty(seckillSpuIds)) {
            ServerResponseEntity<Void> responseEntity = seckillFeignClient.offlineSeckillBySpuIds(seckillSpuIds);
            if (!responseEntity.isSuccess()) {
                throw new LuckException(responseEntity.getMsg());
            }
        }
        // 下线对应的团购活动
        if (CollectionUtil.isNotEmpty(groupSpuIds)) {
            ServerResponseEntity<Void> responseEntity = groupFeignClient.offlineGroupBySpuIds(groupSpuIds);
            if (!responseEntity.isSuccess()) {
                throw new LuckException(responseEntity.getMsg());
            }
        }
    }
}
