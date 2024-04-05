package com.mall4j.cloud.platform.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mall4j.cloud.api.docking.skq_erp.dto.StoreIntegralRankReqDto;
import com.mall4j.cloud.api.docking.skq_erp.dto.StoreIntegralRankRespDto;
import com.mall4j.cloud.api.docking.skq_erp.feign.StdShopFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreIntegralRankVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.platform.service.StoreIntegralRankService;
import com.mall4j.cloud.platform.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @Description 门店积分抵现榜单
 * @Author axin
 * @Date 2023-02-16 14:43
 **/
@Service
@Slf4j
public class StoreIntegralRankServiceImpl implements StoreIntegralRankService {

    @Resource
    private StdShopFeignClient stdShopFeignClient;
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private MapperFacade mapperFacade;

    private final static String STORE_INTEGRAL_RANK_BONUS="STORE_INTEGRAL_RANK_BONUS";

    private final static String STORE_INTEGRAL_RANK="mall4cloud_platform:STORE_INTEGRAL_RANK_";


    @Override
    public StoreIntegralRankVO getStoreIntegralRank() {
        StoreIntegralRankVO storeIntegralRankVO = new StoreIntegralRankVO();
        String storeIntegralRankBonus = sysConfigService.getValue(STORE_INTEGRAL_RANK_BONUS);
        if(StringUtils.isBlank(storeIntegralRankBonus)){
            throw new LuckException("未获取到排名奖金配置");
        }
        Map<String,String> storeIntegralRankBonusMap = JSON.parseObject(storeIntegralRankBonus, Map.class);
        StoreIntegralRankReqDto lastMonthReq = new StoreIntegralRankReqDto();
        Date date = new Date();
        lastMonthReq.setMonth(DateUtil.format(DateUtil.offsetMonth(date, -1), "yyyy-MM"));
        lastMonthReq.setPageNum(1);
        lastMonthReq.setPageSize(storeIntegralRankBonusMap.size());
        String lastMonthRedisKey=STORE_INTEGRAL_RANK +lastMonthReq.getPageSize()+ lastMonthReq.getMonth();
        BoundValueOperations<String,List<StoreIntegralRankVO.StoreIntegralRank>> lastBoundValueOperations = redisTemplate.boundValueOps(lastMonthRedisKey);

        //上月
        List<StoreIntegralRankVO.StoreIntegralRank> lastStoreIntegralRanks = lastBoundValueOperations.get();
        if(CollectionUtils.isEmpty(lastStoreIntegralRanks)){
            log.info("积分抵现榜单上月入参:{}",JSON.toJSONString(lastMonthReq));
            ServerResponseEntity<List<StoreIntegralRankRespDto>> listServerLastMonthRespEntity = stdShopFeignClient.queryStoreIntegralRank(lastMonthReq);
            log.info("积分抵现榜单上月出参:{}", JSON.toJSONString(listServerLastMonthRespEntity));
            if(listServerLastMonthRespEntity.isFail()){
                throw new LuckException(listServerLastMonthRespEntity.getMsg());
            }
            if(CollectionUtils.isNotEmpty(listServerLastMonthRespEntity.getData())){
                lastStoreIntegralRanks = setRankBonus(storeIntegralRankBonusMap, listServerLastMonthRespEntity.getData());
                Iterator<StoreIntegralRankVO.StoreIntegralRank> iterator = lastStoreIntegralRanks.iterator();
                while (iterator.hasNext()){
                    StoreIntegralRankVO.StoreIntegralRank storeIntegralRank = iterator.next();
                    if(storeIntegralRank.getUsageRate() == null || storeIntegralRank.getUsageRate()<0.35){
                        iterator.remove();
                    }
                }
                storeIntegralRankVO.setLastMonth(lastStoreIntegralRanks);
                lastBoundValueOperations.set(lastStoreIntegralRanks,2,TimeUnit.HOURS);
            }
        }else{
            storeIntegralRankVO.setLastMonth(lastStoreIntegralRanks);
        }

        //当月
        StoreIntegralRankReqDto currentMonthReq = new StoreIntegralRankReqDto();
        currentMonthReq.setMonth(DateUtil.format(date, "yyyy-MM"));
        currentMonthReq.setPageNum(1);
        currentMonthReq.setPageSize(storeIntegralRankBonusMap.size());
        String currentRedisKey=STORE_INTEGRAL_RANK +currentMonthReq.getPageSize()+ currentMonthReq.getMonth();
        BoundValueOperations<String,List<StoreIntegralRankVO.StoreIntegralRank>> currentBoundValueOperations = redisTemplate.boundValueOps(currentRedisKey);
        List<StoreIntegralRankVO.StoreIntegralRank> currentStoreIntegralRanks = currentBoundValueOperations.get();
        if(CollectionUtils.isEmpty(currentStoreIntegralRanks)) {
            log.info("积分抵现榜单当月入参:{}", JSON.toJSONString(lastMonthReq));
            ServerResponseEntity<List<StoreIntegralRankRespDto>> listServerCurrentMonthRespEntity = stdShopFeignClient.queryStoreIntegralRank(currentMonthReq);
            log.info("积分抵现榜单当月出参:{}", JSON.toJSONString(listServerCurrentMonthRespEntity));
            if (listServerCurrentMonthRespEntity.isFail()) {
                throw new LuckException(listServerCurrentMonthRespEntity.getMsg());
            }
            if (CollectionUtils.isNotEmpty(listServerCurrentMonthRespEntity.getData())) {
                currentStoreIntegralRanks = setRankBonus(storeIntegralRankBonusMap, listServerCurrentMonthRespEntity.getData());
                storeIntegralRankVO.setCurrentMonth(currentStoreIntegralRanks);
                currentBoundValueOperations.set(currentStoreIntegralRanks,5,TimeUnit.MINUTES);
            }
        }else {
            storeIntegralRankVO.setCurrentMonth(currentStoreIntegralRanks);
        }

        return storeIntegralRankVO;
    }

    /**
     * 设置排名奖金
     * @param rankBonusMap
     * @param rankRespDtos
     * @return
     */
    private List<StoreIntegralRankVO.StoreIntegralRank> setRankBonus(Map<String, String> rankBonusMap, List<StoreIntegralRankRespDto> rankRespDtos) {
        List<StoreIntegralRankVO.StoreIntegralRank> lastMonthRankList= Lists.newArrayList();
        Map<Double, Integer> usageRateRank = getUsageRateRank(rankRespDtos.stream().map(StoreIntegralRankRespDto::getUsageRate).collect(Collectors.toList()));
        for (StoreIntegralRankRespDto rankRespDto : rankRespDtos) {
            StoreIntegralRankVO.StoreIntegralRank monthRank = new StoreIntegralRankVO.StoreIntegralRank();
            mapperFacade.map(rankRespDto, monthRank);
            monthRank.setRankNo(usageRateRank.get(rankRespDto.getUsageRate()));
            monthRank.setRankBonus(rankBonusMap.get(Objects.nonNull(monthRank.getRankNo())?monthRank.getRankNo().toString():null));
            lastMonthRankList.add(monthRank);
        }
        return lastMonthRankList;
    }

    /**
     * 获取积分占比使用排行
     * @param usageRateList
     * @return
     */
    public static Map<Double,Integer> getUsageRateRank(List<Double> usageRateList){
        List<Double> usageRates = usageRateList.stream().filter(Objects::nonNull).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        Map<Double, Integer> map = Maps.newHashMap();
        if(CollectionUtils.isNotEmpty(usageRates)) {
            for (int i = 0; i < usageRates.size(); i++) {
                if (!map.containsKey(usageRates.get(i))) {
                    map.put(usageRates.get(i), i+1);
                }
            }
        }
        return map;
    }

}
