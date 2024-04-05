package com.mall4j.cloud.group.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall4j.cloud.api.group.enums.ActivityChannelEnums;
import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.api.product.feign.SpuFeignClient;
import com.mall4j.cloud.common.product.vo.SkuVO;
import com.mall4j.cloud.common.product.vo.SpuVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.group.dto.ActivityCommodityAddDTO;
import com.mall4j.cloud.group.mapper.CommodityPoolMapper;
import com.mall4j.cloud.group.model.CommodityPool;
import com.mall4j.cloud.group.service.ActivityCommodityBizService;
import com.mall4j.cloud.group.service.CommodityPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.logging.Handler;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ActivityCommodityBizServiceImpl implements ActivityCommodityBizService {
    @Resource
    private CommodityPoolService commodityPoolService;
    @Autowired
    private SpuFeignClient spuFeignClient;
    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private CommodityPoolMapper commodityPoolMapper;

    @Override
    public ActivityCommodityAddDTO addActivityCommodity(List<Long> commodityIds, Date beginTime, Date endTime, Integer activityChannel, Long activityId) {
        ActivityCommodityAddDTO activityCommodityAddDTO = new ActivityCommodityAddDTO();
        List<CommodityPool> pools = commodityPoolService.list(new LambdaQueryWrapper<CommodityPool>()
                .eq(CommodityPool::getDelFlag,0)
                .in(CommodityPool::getCommodityId, commodityIds)
                .gt(CommodityPool::getEndTime, beginTime)
                .lt(CommodityPool::getBeginTime, endTime));
        log.info("查询调价数据:{}", JSONObject.toJSONString(pools));
        if (CollectionUtil.isNotEmpty(pools)) {
            List<String> commodityIdList = pools.stream().map(temp -> temp.getCommodityId().toString()).collect(Collectors.toList());
            activityCommodityAddDTO.setFailCommodityIds(commodityIdList);
        } else {
            List<CommodityPool> commodityPools = new ArrayList<>();
            commodityIds.forEach(temp -> {
                CommodityPool pool = CommodityPool.builder()
                        .activityId(activityId)
                        .activityChannel(activityChannel)
                        .commodityId(temp)
                        .beginTime(beginTime)
                        .endTime(endTime)
                        .delFlag(0)
                        .build();
                commodityPools.add(pool);
            });
            commodityPoolService.saveBatch(commodityPools);
        }
        return activityCommodityAddDTO;
    }

    /**
     * 商品池区分门店
     * @param commodityIds
     * @param beginTime
     * @param endTime
     * @param activityChannel
     * @param activityId
     * @param storeIds
     * @return
     */
    @Override
    public ActivityCommodityAddDTO addActivityCommodity(List<Long> commodityIds, Date beginTime, Date endTime, Integer activityChannel, Long activityId, List<Long> storeIds) {

        //原来逻辑
//        return addActivityCommodity(commodityIds,beginTime,endTime,activityChannel,activityId);

        //新调整逻辑
        if(CollectionUtil.isEmpty(storeIds)){
            return activityCommodityAllStore(commodityIds,beginTime,endTime,activityChannel,activityId,storeIds);
        }else{
            return activityCommodityStore(commodityIds,beginTime,endTime,activityChannel,activityId,storeIds);
        }

    }


    /**
     * 全部门店
     * @param commodityIds
     * @param beginTime
     * @param endTime
     * @param activityChannel
     * @param activityId
     * @param storeIds
     * @return
     */
    private ActivityCommodityAddDTO activityCommodityAllStore(List<Long> commodityIds, Date beginTime, Date endTime, Integer activityChannel, Long activityId, List<Long> storeIds){
        ActivityCommodityAddDTO activityCommodityAddDTO = new ActivityCommodityAddDTO();
        List<CommodityPool> pools = commodityPoolService.list(new LambdaQueryWrapper<CommodityPool>()
//                .eq(CommodityPool::getStoreId,-1)
                .eq(CommodityPool::getDelFlag,0)
                .in(CommodityPool::getCommodityId, commodityIds)
                .gt(CommodityPool::getEndTime, beginTime)// 大于
                .gt(CommodityPool::getEndTime, new Date())// 大于(活动过期)
                .lt(CommodityPool::getBeginTime, endTime));// 小于

//        List<CommodityPool> pools=commodityPoolMapper.getCommodityPools(beginTime,endTime,commodityIds,null);

        log.info("商品池查询调价数据-->全部门店 :{}", JSONObject.toJSONString(pools));
        if (CollectionUtil.isNotEmpty(pools)) {

            backErrorMsg(activityCommodityAddDTO,pools);

//            List<String> commodityIdList = pools.stream().map(temp -> temp.getCommodityId().toString()).collect(Collectors.toList());
//            activityCommodityAddDTO.setFailCommodityIds(commodityIdList);
//            List<Long> spuIds = pools.stream().map(temp -> temp.getCommodityId()).collect(Collectors.toList());
//            ServerResponseEntity<List<SpuVO>> response=spuFeignClient.listSpuBySpuIds(spuIds);
//            if(response.isSuccess() && CollectionUtil.isNotEmpty(response.getData())){
//                List<String> spuCodes = new ArrayList<>();
//                Map<Long, CommodityPool> spuMaps = pools.stream().collect(Collectors.toMap(CommodityPool::getCommodityId, a -> a,(k1, k2)->k1));
//                response.getData().stream().forEach(spuVO -> {
//                    if(spuMaps.containsKey(spuVO.getSpuId())){
//                        spuCodes.add("重复商品片【"+spuVO.getSpuCode()+"】在->"
//                                + ActivityChannelEnums.getName(spuMaps.get(spuVO.getSpuId()).getActivityChannel())
//                                +" ID"+spuMaps.get(spuVO.getSpuId()).getActivityId()+"中");
//                    }
//                });
//                activityCommodityAddDTO.setBackRepSpus(spuCodes);
//                String msg=""+activityCommodityAddDTO.getBackRepSpus().toString();
//                activityCommodityAddDTO.setBackMsg(msg);
//            }
        } else {
            List<CommodityPool> commodityPools = new ArrayList<>();
            commodityIds.forEach(temp -> {
                CommodityPool pool = CommodityPool.builder()
                        .activityId(activityId)
                        .activityChannel(activityChannel)
                        .commodityId(temp)
                        .beginTime(beginTime)
                        .endTime(endTime)
                        .storeId(-1L)
                        .delFlag(0)
                        .build();
                commodityPools.add(pool);
            });
            commodityPoolService.saveBatch(commodityPools);
        }
        return activityCommodityAddDTO;
    }

    private void backErrorMsg(ActivityCommodityAddDTO activityCommodityAddDTO,List<CommodityPool> pools){
        List<String> commodityIdList = pools.stream().filter(pool -> Objects.nonNull(pool.getCommodityId())).map(temp -> temp.getCommodityId().toString()).collect(Collectors.toList());
        activityCommodityAddDTO.setFailCommodityIds(commodityIdList);
        List<Long> spuIds = pools.stream().map(temp -> temp.getCommodityId()).collect(Collectors.toList());
        ServerResponseEntity<List<SpuVO>> response=spuFeignClient.listSpuBySpuIds(spuIds);
        if(response.isSuccess() && CollectionUtil.isNotEmpty(response.getData())){
            List<String> spuCodes = new ArrayList<>();
            Map<Long, CommodityPool> spuMaps = pools.stream().collect(Collectors.toMap(CommodityPool::getCommodityId, a -> a,(k1, k2)->k1));
            response.getData().stream().forEach(spuVO -> {
                if(spuMaps.containsKey(spuVO.getSpuId())){
                    spuCodes.add("重复商品【"+spuVO.getSpuCode()+"】在"
                            + ActivityChannelEnums.getName(spuMaps.get(spuVO.getSpuId()).getActivityChannel())
                            +"ID:"+spuMaps.get(spuVO.getSpuId()).getActivityId()+"中");
                }
            });
            activityCommodityAddDTO.setBackRepSpus(spuCodes);
            String msg=""+activityCommodityAddDTO.getBackRepSpus().toString();
            activityCommodityAddDTO.setBackMsg(msg);
        }
    }

    /**
     * 部分门店
     * @param commodityIds
     * @param beginTime
     * @param endTime
     * @param activityChannel
     * @param activityId
     * @param storeIds
     * @return
     */
    private ActivityCommodityAddDTO activityCommodityStore(List<Long> commodityIds, Date beginTime, Date endTime, Integer activityChannel, Long activityId, List<Long> storeIds){
        ActivityCommodityAddDTO activityCommodityAddDTO = new ActivityCommodityAddDTO();
//        List<CommodityPool> pools = commodityPoolService.list(new LambdaQueryWrapper<CommodityPool>()
//                .eq(CommodityPool::getDelFlag,0)
//                .in(CommodityPool::getStoreId,storeIds)
//                .in(CommodityPool::getCommodityId, commodityIds)
//                .gt(CommodityPool::getEndTime, beginTime)// 大于
//                .gt(CommodityPool::getEndTime, new Date())// 大于(活动过期)
//                .lt(CommodityPool::getBeginTime, endTime));// 小于

        List<CommodityPool> pools=commodityPoolMapper.getCommodityPoolsByStore(beginTime,endTime,commodityIds,storeIds);

        log.info("商品池查询调价数据-->部分门店 :{}", JSONObject.toJSONString(pools));
        if (CollectionUtil.isNotEmpty(pools)) {

            backErrorMsg(activityCommodityAddDTO,pools);

//            List<String> commodityIdList = pools.stream().map(temp -> temp.getCommodityId().toString()).collect(Collectors.toList());
//            activityCommodityAddDTO.setFailCommodityIds(commodityIdList);
//
//            List<Long> storeIdList = pools.stream().map(temp -> temp.getStoreId()).collect(Collectors.toList());
//            ServerResponseEntity<List<StoreVO>> storeRes=storeFeignClient.listByStoreIdList(storeIdList);
//            if(storeRes.isSuccess() && CollectionUtil.isNotEmpty(storeRes.getData())){
//                //设置的门店集合
//                Map<Long, StoreVO> storeCodeMap = storeRes.getData().stream().collect(Collectors.toMap(StoreVO::getStoreId, storeCodeVO -> storeCodeVO,(k1, k2)->k1));
//                //设置的商品集合
//                List<Long> spuIds = pools.stream().map(temp -> temp.getCommodityId()).collect(Collectors.toList());
//                ServerResponseEntity<List<SpuVO>> response=spuFeignClient.listSpuBySpuIds(spuIds);
//                if(response.isSuccess() && CollectionUtil.isNotEmpty(response.getData())){
//                    //商品信息
//                    Map<Long, SpuVO> spuCodeMap = response.getData().stream().collect(Collectors.toMap(SpuVO::getSpuId, spuCodeVO -> spuCodeVO,(k1, k2)->k1));
//                    //筛选出重复的门店商品数据
//                    Map<String,List<String>> backStoreAndSpus=new HashMap<>();
//                    pools.forEach(poolitem->{
//                        if(storeCodeMap.containsKey(poolitem.getStoreId())){
//                            String storeCode=storeCodeMap.get(poolitem.getStoreId()).getStoreCode();
//                            String spuCode=spuCodeMap.get(poolitem.getCommodityId()).getSpuCode();
//                            if(backStoreAndSpus.containsKey(storeCode)){
//                                backStoreAndSpus.get(storeCode).add(spuCode);
//                            }else{
//                                List<String> spuCodes=new ArrayList<>();
//                                spuCodes.add(spuCode);
//                                backStoreAndSpus.put(storeCode,spuCodes);
//                            }
//                        }
//                    });
//                    activityCommodityAddDTO.setBackRepStoreAndSpus(backStoreAndSpus);
//
//                    StringBuilder sb=new StringBuilder();
//                    activityCommodityAddDTO.getBackRepStoreAndSpus().forEach((key, value) -> {
//                        sb.append("门店[").append(key).append("]").append("->商品货号:").append(value.toString()).append(" ");
//                    });
//                    activityCommodityAddDTO.setBackMsg("重复商品为:"+sb.toString());
//                }
//            }
        } else {
            List<CommodityPool> commodityPools = new ArrayList<>();
            commodityIds.forEach(temp -> {
                storeIds.forEach(storeId->{//需要保存门店对应商品
                    CommodityPool pool = CommodityPool.builder()
                            .activityId(activityId)
                            .activityChannel(activityChannel)
                            .commodityId(temp)
                            .beginTime(beginTime)
                            .endTime(endTime)
                            .storeId(storeId)
                            .delFlag(0)
                            .build();
                    commodityPools.add(pool);
                });
            });
            commodityPoolService.saveBatch(commodityPools);
        }
        return activityCommodityAddDTO;
    }



    @Override
    public void removeActivityCommodity(List<String> commodityIds) {
        commodityPoolService.remove(new LambdaQueryWrapper<CommodityPool>().in(CommodityPool::getCommodityId, commodityIds));
    }

    @Override
    public void removeActivityCommodityByActivityId(Long activityId, Integer activityChannel) {
        commodityPoolService.remove(new LambdaQueryWrapper<CommodityPool>().eq(CommodityPool::getActivityId, activityId).eq(CommodityPool::getActivityChannel, activityChannel));

    }
}
