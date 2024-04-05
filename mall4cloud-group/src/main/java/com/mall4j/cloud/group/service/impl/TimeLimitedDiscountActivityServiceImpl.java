package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.group.enums.ActivityChannelEnums;
import com.mall4j.cloud.api.group.feign.dto.TimeDiscountActivityDTO;
import com.mall4j.cloud.api.group.feign.vo.TimeDiscountActivityVO;
import com.mall4j.cloud.api.product.dto.SpuSkuRDTO;
import com.mall4j.cloud.api.product.enums.SpuChannelEnums;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.cache.constant.TimeDiscountCacheNames;
import com.mall4j.cloud.common.cache.util.RedisUtil;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.vo.SpuAttrValueLangVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.group.dto.TimeLimitDiscountActivityCheckDTO;
import com.mall4j.cloud.group.dto.TimeLimitDiscountActivityPageDTO;
import com.mall4j.cloud.group.enums.ActivityStatusEnums;
import com.mall4j.cloud.group.mapper.TimeLimitedDiscountActivityMapper;
import com.mall4j.cloud.group.mapper.TimeLimitedDiscountSkuMapper;
import com.mall4j.cloud.group.model.TimeLimitedDiscountActivity;
import com.mall4j.cloud.group.service.ActivityCommodityBizService;
import com.mall4j.cloud.group.service.TimeLimitedDiscountActivityService;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountActivityPageVO;
import com.mall4j.cloud.group.vo.TimeLimitedDiscountActivityVO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 限时调价活动
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 01:55:13
 */
@Slf4j
@Service
@RefreshScope
@Transactional(rollbackFor = Exception.class)
public class TimeLimitedDiscountActivityServiceImpl implements TimeLimitedDiscountActivityService {

    @Value("${mall4cloud.group.redisCacheSpuFlag:false}")
    @Setter
    private boolean REDIS_CACHE_SPU_FLAG=false;
    @Value("${mall4cloud.group.redisCacheSpu:120}")
    @Setter
    private int REDIS_CACHE_SPU=120;
    @Autowired
    private TimeLimitedDiscountActivityMapper timeLimitedDiscountActivityMapper;

    @Autowired
    private TimeLimitedDiscountSkuMapper discountSkuMapper;
    @Resource
    private ActivityCommodityBizService activityCommodityBizService;

    @Autowired
    private SpuFeignClient spuFeignClient;

    @Override
    public PageVO<TimeLimitedDiscountActivityPageVO> page(PageDTO pageDTO, TimeLimitDiscountActivityPageDTO param) {

        PageVO<TimeLimitedDiscountActivityPageVO> pageVO = PageUtil.doPage(pageDTO, () -> timeLimitedDiscountActivityMapper.list(param));
        Date date = new Date();
        for (TimeLimitedDiscountActivityPageVO activityPageVO : pageVO.getList()) {
            Date activityEndTime = activityPageVO.getActivityEndTime();
            Integer tempActivityStatus = activityPageVO.getStatus();
            Date activityBeginTime = activityPageVO.getActivityBeginTime();

            if (!ActivityStatusEnums.NOT_ENABLED.getCode().equals(tempActivityStatus)) {
                if (DateUtil.isIn(date, activityBeginTime, activityEndTime)) {
                    activityPageVO.setStatusName(ActivityStatusEnums.IN_PROGRESS.getName());
                } else if (date.compareTo(activityBeginTime) < 0) {
                    activityPageVO.setStatusName(ActivityStatusEnums.NOT_START.getName());
                } else if (date.compareTo(activityEndTime) > 0) {
                    activityPageVO.setStatusName(ActivityStatusEnums.END.getName());
                }
            } else {
                activityPageVO.setStatusName("未启用");
            }
        }


        return pageVO;
    }

    @Override
    public TimeLimitedDiscountActivityVO getById(Long id) {
        return timeLimitedDiscountActivityMapper.getById(id);
    }

    @Override
    public void save(TimeLimitedDiscountActivity timeLimitedDiscountActivity) {
        timeLimitedDiscountActivityMapper.save(timeLimitedDiscountActivity);
    }

    @Override
    public void update(TimeLimitedDiscountActivity timeLimitedDiscountActivity) {
        timeLimitedDiscountActivityMapper.update(timeLimitedDiscountActivity);
    }

    @Override
    public void deleteById(Long id) {
        timeLimitedDiscountActivityMapper.deleteById(id);

        //移除商品池商品
        activityCommodityBizService.removeActivityCommodityByActivityId(Long.valueOf(id), ActivityChannelEnums.TIME_DISCOUNT_ACTIVITY.getCode());

        this.removeCache(Long.valueOf(id.longValue()));
    }

    @Override
    public void updateCheckStatusBatch(List<TimeLimitDiscountActivityCheckDTO> checkDTOS) {
        if(CollectionUtil.isNotEmpty(checkDTOS)){
            checkDTOS.forEach(checkDTO->{
                checkDTO.setCheckBy(AuthUserContext.get().getUserId().toString());
            });
            this.timeLimitedDiscountActivityMapper.updateCheckStatusBatch(checkDTOS);
        }
    }

    @Override
    public List<TimeDiscountActivityVO> convertActivityPrice(TimeDiscountActivityDTO params) {

        List<TimeDiscountActivityVO> skus = new ArrayList<>();
        if (CollectionUtil.isEmpty(params.getSkuIds()) && CollectionUtil.isEmpty(params.getSkuStoreNoStockIds())) {
            return skus;
        }
        Integer type= Objects.nonNull(params.getType())?params.getType():1;//类型1，限时调价。2，会员日活动调价
        //审核状态：0待审核 1审核通过 2驳回
        Integer checkStatus=1;//默认只查审核通过
        //查询当前店铺是否有调价活动在开展。
        List<TimeLimitedDiscountActivityVO> activityVOS1 = timeLimitedDiscountActivityMapper.currentActivity(params.getShopId(),checkStatus,type);

        //如果 有符合条件的活动。查询商品列表中是否有参与活动的商品，返回对应的活动价格。
        if (CollectionUtil.isNotEmpty(activityVOS1)) {
            List<Integer> activityids = activityVOS1.stream().map(TimeLimitedDiscountActivityVO::getId).collect(Collectors.toList());
            if(CollectionUtil.isNotEmpty(params.getSkuIds())){
//                skus = discountSkuMapper.selectBySkuIds(activityids, params.getSkuIds());
                skus = this.selectBySkuIds(activityids, params.getSkuIds());
            }

            log.info("convertActivityPrice---> 显示调价sku1：{}", JSON.toJSONString(skus));

            //门店无库存sku(门店无库存取官店价格，官店有限时调价价格优先取
            boolean checkShopR=false;
            log.info("convertActivityPrice---> 门店无库存sku：{}", JSON.toJSONString(params.getSkuStoreNoStockIds()));
            if(CollectionUtil.isNotEmpty(params.getSkuStoreNoStockIds()) && params.getSkuStoreNoStockIds().size()>0){
                List<TimeLimitedDiscountActivityVO> activityVOS_shop = timeLimitedDiscountActivityMapper.currentActivity(1L,checkStatus,type);
                log.info("convertActivityPrice---> 门店无库存 官店限时调价活动：{}", activityVOS_shop.size());
                if(CollectionUtil.isNotEmpty(activityVOS_shop)){
                    List<Integer> activityids_shop = activityVOS_shop.stream().map(TimeLimitedDiscountActivityVO::getId).collect(Collectors.toList());
//                    List<TimeDiscountActivityVO> skus_shop = discountSkuMapper.selectBySkuIds(activityids_shop, params.getSkuStoreNoStockIds());
                    List<TimeDiscountActivityVO> skus_shop = this.selectBySkuIds(activityids_shop, params.getSkuStoreNoStockIds());
                    log.info("convertActivityPrice---> 门店无库存 官店限时调价活动sku：{} skus.size():{}", skus_shop.size(),skus.size());
                    if(CollectionUtil.isNotEmpty(skus_shop) && skus_shop.size()>0){
                        log.info("convertActivityPrice---> 门店无库存 官店限时调价活动sku：{}", JSON.toJSONString(skus_shop));
                        if(CollectionUtil.isEmpty(skus)){
                            skus = new ArrayList<>();
                        }
                        checkShopR=true;
                        skus.addAll(skus_shop);
                    }
                }
            }

            log.info("convertActivityPrice---> 显示调价sku2：{}", JSON.toJSONString(skus));

            //R渠道商品不参与限时调价活动 (仅限官店)
            log.info("前置--> R渠道商品不参与限时调价活动(仅限官店) storeId:{} {}",params.getShopId(),(params.getShopId()==1?"官店不参与执行剔除逻辑":"门店参与"));
            if((CollectionUtil.isNotEmpty(skus) && skus.size()>0 && params.getShopId()==1) || checkShopR){
                Map<Long, List<TimeDiscountActivityVO>> spuMap = skus.stream().collect(Collectors.groupingBy(TimeDiscountActivityVO::getSpuId));
                List<Long> spuIds=new ArrayList<>(spuMap.keySet());
                ServerResponseEntity<List<Long>> serverResponseEntity=spuFeignClient.isSpuSkuChannel(new SpuSkuRDTO(SpuChannelEnums.CHANNEL_R.getCode(),spuIds));
                if(serverResponseEntity.isSuccess()
                        && CollectionUtil.isNotEmpty(serverResponseEntity.getData())
                        && serverResponseEntity.getData().size()>0){
                    serverResponseEntity.getData().forEach(item->{
                        if(spuMap.containsKey(item)){
                            spuMap.remove(item);//提出R渠道商品
                        }
                    });
                }
                skus=new ArrayList<>();
                for(Map.Entry<Long,List<TimeDiscountActivityVO>> entry : spuMap.entrySet()){
                    skus.addAll(entry.getValue());
                }
            }

            log.info("convertActivityPrice---> 显示调价sku3：{}", JSON.toJSONString(skus));
        }
        return skus;
    }

    @Override
    public List<TimeDiscountActivityVO> convertActivityPricesNoFilter(TimeDiscountActivityDTO params) {
        List<TimeDiscountActivityVO> skus = new ArrayList<>();
        if (CollectionUtil.isEmpty(params.getSkuIds()) && CollectionUtil.isEmpty(params.getSkuStoreNoStockIds())) {
            return skus;
        }
        Integer type= Objects.nonNull(params.getType())?params.getType():1;//类型1，限时调价。2，会员日活动调价 3,虚拟门店价
        log.info("取活动价类型 type:{}",type);
        //审核状态：0待审核 1审核通过 2驳回
        Integer checkStatus=1;//默认只查审核通过
        //查询当前店铺是否有调价活动在开展。
        List<TimeLimitedDiscountActivityVO> activityVOS1 = timeLimitedDiscountActivityMapper.currentActivity(params.getShopId(),checkStatus,type);

        if (CollectionUtil.isNotEmpty(activityVOS1)) {
            List<Integer> activityids = activityVOS1.stream().map(TimeLimitedDiscountActivityVO::getId).collect(Collectors.toList());
            if(params.isNoStoreStock() && params.getSkuStoreNoStockIds().size()>0){
                log.info("调价取价 -> 取价类型【{}】 门店无库存sku数【{}】 活动id【{}】",params.getType(),params.getSkuStoreNoStockIds().size(),activityids.toString());
//                List<TimeDiscountActivityVO> noStockSkus = discountSkuMapper.selectBySkuIds(activityids, params.getSkuStoreNoStockIds());
                List<TimeDiscountActivityVO> noStockSkus = this.selectBySkuIds(activityids, params.getSkuStoreNoStockIds());
                skus.addAll(noStockSkus);
            }
            if(params.getSkuIds().size()>0){
                log.info("调价取价 ->取价类型【{}】 可参与sku数【{}】活动id【{}】",params.getType(),params.getSkuIds().size(),activityids.toString());
//                List<TimeDiscountActivityVO> skuList = discountSkuMapper.selectBySkuIds(activityids, params.getSkuIds());
                List<TimeDiscountActivityVO> skuList = this.selectBySkuIds(activityids, params.getSkuIds());
                skus.addAll(skuList);
            }
            if(CollectionUtil.isNotEmpty(skus) && skus.size()>0){
                Map<Integer, TimeLimitedDiscountActivityVO> activityVOMaps = activityVOS1.stream()
                        .collect(Collectors.toMap(TimeLimitedDiscountActivityVO::getId, s -> s));
                skus.forEach(activityVO -> {
                    if(activityVOMaps.containsKey(activityVO.getActivityId())){
                        activityVO.setFriendlyDiscountFlag(activityVOMaps.get(activityVO.getActivityId()).getFriendlyDiscountFlag());
                        activityVO.setFriendlyCouponUseFlag(activityVOMaps.get(activityVO.getActivityId()).getFriendlyCouponUseFlag());
                    }
                });
            }
        }
        return skus;
    }

    @Override
    public List<TimeDiscountActivityVO> currentActivityBySpuId(TimeDiscountActivityDTO params) {
        List<TimeDiscountActivityVO> skus=timeLimitedDiscountActivityMapper.currentActivityBySpuId(params.getShopId(),params.getSpuId());
        return skus;
    }

    @Override
    public List<TimeDiscountActivityVO> selectBySkuIds(List<Integer> activityids, List<Long> skuIds) {
        String rediskey= TimeDiscountCacheNames.TIMEDISCOUNT_ACTIVITY_SKUS_PREFIX+ DigestUtil.md5Hex(JSON.toJSONString(activityids));
        if(RedisUtil.hasKey(rediskey) && REDIS_CACHE_SPU_FLAG){
            log.info("cache selectBySkuIds data--> {}",rediskey);
            List<TimeDiscountActivityVO> skus= JSONObject.parseArray(RedisUtil.get(rediskey),TimeDiscountActivityVO.class);
            return skus;
        }
        List<TimeDiscountActivityVO> skus=discountSkuMapper.selectBySkuIds(activityids, skuIds);
        if(REDIS_CACHE_SPU_FLAG && CollectionUtil.isNotEmpty(skus)){
            RedisUtil.set(rediskey,JSONObject.toJSONString(skus),REDIS_CACHE_SPU);
        }
        return skus;
    }

    @Override
    public void removeCache(Long id) {

        RedisUtil.deleteBatchKeys(TimeDiscountCacheNames.TIMEDISCOUNT_ACTIVITY_SKUS_PREFIX+"*");

    }

}
