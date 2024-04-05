package com.mall4j.cloud.distribution.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.distribution.dto.DistributionCommissionRateDTO;
import com.mall4j.cloud.api.distribution.vo.DistributionCommissionRateVO;
import com.mall4j.cloud.distribution.dto.DistributionCommissionActivitySearchDTO;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionActivityMapper;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionActivitySpuMapper;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionActivityStoreMapper;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionUnityMapper;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivity;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivitySpu;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivityStore;
import com.mall4j.cloud.distribution.model.DistributionCommissionUnity;
import com.mall4j.cloud.distribution.service.DistributionCommissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DistributionCommissionServiceImpl implements DistributionCommissionService {

    @Autowired
    private DistributionCommissionUnityMapper distributionCommissionUnityMapper;
    @Autowired
    private DistributionCommissionActivityMapper distributionCommissionActivityMapper;
    @Autowired
    private DistributionCommissionActivityStoreMapper distributionCommissionActivityStoreMapper;
    @Autowired
    private DistributionCommissionActivitySpuMapper distributionCommissionActivitySpuMapper;

    @Override
    public Map<Long, DistributionCommissionRateVO> getDistributionCommissionRate(DistributionCommissionRateDTO distributionCommissionRateDTO) {
        log.info("DistributionCommissionServiceImpl.getDistributionCommissionRate 执行参数distributionCommissionRateDTO：{}", JSONObject.toJSONString(distributionCommissionRateDTO));
        if (distributionCommissionRateDTO.getLimitOrderType() == null) {
            distributionCommissionRateDTO.setLimitOrderType(0);
        }
        Long storeId = distributionCommissionRateDTO.getStoreId();
        List<Long> spuIdList = distributionCommissionRateDTO.getSpuIdList();
        Map<Long, DistributionCommissionRateVO> rateVOMap = new HashMap<>();
        BigDecimal zero = new BigDecimal("0.00");
        spuIdList.stream().forEach(s->{rateVOMap.put(s, new DistributionCommissionRateVO(zero, zero, zero, 0, 0, 0));});
        // 所有生效中的佣金活动列表
        DistributionCommissionActivitySearchDTO searchDTO = new DistributionCommissionActivitySearchDTO();
        searchDTO.setStatus(1);
        searchDTO.setLimitOrderType(distributionCommissionRateDTO.getLimitOrderType());
        List<DistributionCommissionActivity> commissionActivityList = distributionCommissionActivityMapper.list(searchDTO);
        Date now = new Date();
        if (!CollectionUtils.isEmpty(commissionActivityList)) {
            commissionActivityList = commissionActivityList.stream().filter(c -> c.getLimitTimeType() == 0 ||
                    (c.getLimitTimeType() == 1 && now.after(c.getStartTime()) && now.before(c.getEndTime())))
                    .collect(Collectors.toList());
            Map<Long, DistributionCommissionActivity> commissionActivityMap = commissionActivityList.stream()
                    .collect(Collectors.toMap(DistributionCommissionActivity :: getId, Function.identity()));
            // 商品满足的商品佣金配置列表
            List<DistributionCommissionActivitySpu> commissionActivitySpuList = distributionCommissionActivitySpuMapper
                    .listBySpuIdList(spuIdList);
            if (!CollectionUtils.isEmpty(commissionActivitySpuList)) {
                // 排除已失效和不在活动有效期内的活动
                commissionActivitySpuList = commissionActivitySpuList.stream().filter(s ->
                        (commissionActivityMap.containsKey(s.getActivityId())
                        && ((s.getStartTime() == null && s.getEndTime() == null)
                        || (s.getStartTime() != null && s.getEndTime() != null
                        && now.after(s.getStartTime()) && now.before(s.getEndTime()))))).collect(Collectors.toList());
                // FIXME 忘记取反了
                if (!CollectionUtils.isEmpty(commissionActivitySpuList)) {
                    commissionActivitySpuList.stream().forEach(a -> {
                        a.setPriority(commissionActivityMap.get(a.getActivityId()).getPriority());
                    });
                    List<DistributionCommissionActivitySpu> guideRateList = commissionActivitySpuList.stream()
                            .filter(c -> c.getGuideRateStatus() == 1)
                            .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(guideRateList)) {
                        Map<Long, List<DistributionCommissionActivitySpu>> guideRateMap =
                                guideRateList.stream().collect(Collectors.groupingBy(g -> g.getSpuId(), Collectors.toList()));
                        guideRateMap.forEach((k,v) ->{
                            DistributionCommissionActivitySpu spu = v.stream()
                                    .sorted(Comparator.comparing(DistributionCommissionActivitySpu :: getPriority)
                                    .reversed()).collect(Collectors.toList()).get(0);
                            rateVOMap.get(k).setGuideRate(spu.getGuideRate());
                            rateVOMap.get(k).setGuideRateActivityId(spu.getActivityId());
                            rateVOMap.get(k).setGuideRateStatus(1);
                        });
                    }
                    List<DistributionCommissionActivitySpu> shareRateList = commissionActivitySpuList.stream()
                            .filter(c -> c.getShareRateStatus() == 1)
                            .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(shareRateList)) {
                        Map<Long, List<DistributionCommissionActivitySpu>> shareRateMap =
                                shareRateList.stream().collect(Collectors.groupingBy(g -> g.getSpuId(), Collectors.toList()));
                        shareRateMap.forEach((k,v) ->{
                            DistributionCommissionActivitySpu spu = v.stream()
                                    .sorted(Comparator.comparing(DistributionCommissionActivitySpu :: getPriority)
                                            .reversed()).collect(Collectors.toList()).get(0);
                            rateVOMap.get(k).setShareRate(spu.getShareRate());
                            rateVOMap.get(k).setShareRateActivityId(spu.getActivityId());
                            rateVOMap.get(k).setShareRateStatus(1);
                        });
                    }
                    List<DistributionCommissionActivitySpu> developRateList = commissionActivitySpuList.stream()
                            .filter(c -> c.getDevelopRateStatus() == 1)
                            .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(developRateList)) {
                        Map<Long, List<DistributionCommissionActivitySpu>> developRateMap =
                                shareRateList.stream().collect(Collectors.groupingBy(g -> g.getSpuId(), Collectors.toList()));
                        developRateMap.forEach((k,v) ->{
                            DistributionCommissionActivitySpu spu = v.stream()
                                    .sorted(Comparator.comparing(DistributionCommissionActivitySpu :: getPriority)
                                            .reversed()).collect(Collectors.toList()).get(0);
                            rateVOMap.get(k).setDevelopRate(spu.getDevelopRate());
                            rateVOMap.get(k).setDevelopRateActivityId(spu.getActivityId());
                            rateVOMap.get(k).setDevelopRateStatus(1);
                        });
                    }
                }
            }
            // 计算当前门店满足的佣金活动
            List<DistributionCommissionActivityStore> commissionActivityStoreList = distributionCommissionActivityStoreMapper
                    .listByStoreId(storeId);
            if (!CollectionUtils.isEmpty(commissionActivityStoreList)) {
                Map<Long, List<DistributionCommissionActivity>> guideRateMap = new HashMap<>();
                Map<Long, List<DistributionCommissionActivity>> shareRateMap = new HashMap<>();
                Map<Long, List<DistributionCommissionActivity>> developRateMap = new HashMap<>();
                commissionActivityStoreList.forEach(c -> {
                    DistributionCommissionActivity distributionCommissionActivity = commissionActivityMap.get(c.getActivityId());
                    if (!Objects.isNull(distributionCommissionActivity)) {
                        if (distributionCommissionActivity.getGuideRateStatus() == 1) {
                            List<DistributionCommissionActivity> activityList = CollectionUtils.isEmpty(guideRateMap.get(c.getStoreId()))
                                    ? new ArrayList<>() : guideRateMap.get(c.getStoreId());
                            activityList.add(distributionCommissionActivity);
                            guideRateMap.put(c.getStoreId(), activityList);
                        }
                        if (distributionCommissionActivity.getShareRateStatus() == 1) {
                            List<DistributionCommissionActivity> activityList = CollectionUtils.isEmpty(shareRateMap.get(c.getStoreId()))
                                    ? new ArrayList<>() : shareRateMap.get(c.getStoreId());
                            activityList.add(distributionCommissionActivity);
                            shareRateMap.put(c.getStoreId(), activityList);
                        }
                        if (distributionCommissionActivity.getDevelopRateStatus() == 1) {
                            List<DistributionCommissionActivity> activityList = CollectionUtils.isEmpty(developRateMap.get(c.getStoreId()))
                                    ? new ArrayList<>() : developRateMap.get(c.getStoreId());
                            activityList.add(distributionCommissionActivity);
                            developRateMap.put(c.getStoreId(), activityList);
                        }
                    }
                });
                log.info("DistributionCommissionServiceImpl.getDistributionCommissionRate  rateVOMap参数：{}", JSONObject.toJSONString(rateVOMap));
                rateVOMap.forEach((k,v) ->{
                    if (v.getGuideRateStatus() == 0 && v.getGuideRate().compareTo(BigDecimal.ZERO) == 0) {
                        guideRateMap.forEach((j,l) ->{
                            DistributionCommissionActivity activity = l.stream()
                                    .sorted(Comparator.comparing(DistributionCommissionActivity :: getPriority)
                                            .reversed()).collect(Collectors.toList()).get(0);
                            v.setGuideRate(activity.getGuideRate());
                            v.setGuideRateActivityId(activity.getId());
                            v.setGuideRateStatus(1);
                        });
                    }
                    if (v.getShareRateStatus() == 0 && v.getShareRate().compareTo(BigDecimal.ZERO) == 0) {
                        shareRateMap.forEach((j,l) ->{
                            DistributionCommissionActivity activity = l.stream()
                                    .sorted(Comparator.comparing(DistributionCommissionActivity :: getPriority)
                                            .reversed()).collect(Collectors.toList()).get(0);
                            v.setShareRate(activity.getShareRate());
                            v.setShareRateActivityId(activity.getId());
                            v.setShareRateStatus(1);
                        });
                    }
                    if (v.getDevelopRateStatus() == 0 && v.getDevelopRate().compareTo(BigDecimal.ZERO) == 0) {
                        developRateMap.forEach((j,l) ->{
                            DistributionCommissionActivity activity = l.stream()
                                    .sorted(Comparator.comparing(DistributionCommissionActivity :: getPriority)
                                            .reversed()).collect(Collectors.toList()).get(0);
                            v.setDevelopRate(activity.getDevelopRate());
                            v.setDevelopRateActivityId(activity.getId());
                            v.setDevelopRateStatus(1);
                        });
                    }
                });
            }
            //全部商品&全部门店佣金活动处理
            List<DistributionCommissionActivity> unLimitActivityList = commissionActivityList.stream().filter(s ->
                    s.getLimitSpuType() == 0 && s.getLimitStoreType() == 0).collect(Collectors.toList());
            Map<Integer, List<DistributionCommissionActivity>> guideRateMap = unLimitActivityList.stream()
                    .filter(s -> s.getGuideRateStatus() == 1)
                    .collect(Collectors.groupingBy(d -> d.getGuideRateStatus(),
                    Collectors.toList()));
            Map<Integer, List<DistributionCommissionActivity>> shareRateMap = unLimitActivityList.stream()
                    .filter(s -> s.getShareRateStatus() == 1)
                    .collect(Collectors.groupingBy(d -> d.getShareRateStatus(),
                            Collectors.toList()));
            Map<Integer, List<DistributionCommissionActivity>> developRateMap = unLimitActivityList.stream()
                    .filter(s -> s.getDevelopRateStatus() == 1)
                    .collect(Collectors.groupingBy(d -> d.getDevelopRateStatus(),
                            Collectors.toList()));
            rateVOMap.forEach((k,v) ->{
                if (v.getGuideRateStatus() == 0 && v.getGuideRate().compareTo(BigDecimal.ZERO) == 0) {
                    guideRateMap.forEach((j,l) ->{
                        DistributionCommissionActivity activity = l.stream()
                                .sorted(Comparator.comparing(DistributionCommissionActivity :: getPriority)
                                        .reversed()).collect(Collectors.toList()).get(0);
                        v.setGuideRate(activity.getGuideRate());
                        v.setGuideRateActivityId(activity.getId());
                        v.setGuideRateStatus(1);
                    });
                }
                if (v.getShareRateStatus() == 0 && v.getShareRate().compareTo(BigDecimal.ZERO) == 0) {
                    shareRateMap.forEach((j,l) ->{
                        DistributionCommissionActivity activity = l.stream()
                                .sorted(Comparator.comparing(DistributionCommissionActivity :: getPriority)
                                        .reversed()).collect(Collectors.toList()).get(0);
                        v.setShareRate(activity.getShareRate());
                        v.setShareRateActivityId(activity.getId());
                        v.setShareRateStatus(1);
                    });
                }
                if (v.getDevelopRateStatus() == 0 && v.getDevelopRate().compareTo(BigDecimal.ZERO) == 0) {
                    developRateMap.forEach((j,l) ->{
                        DistributionCommissionActivity activity = l.stream()
                                .sorted(Comparator.comparing(DistributionCommissionActivity :: getPriority)
                                        .reversed()).collect(Collectors.toList()).get(0);
                        v.setDevelopRate(activity.getDevelopRate());
                        v.setDevelopRateActivityId(activity.getId());
                        v.setDevelopRateStatus(1);
                    });
                }
            });
        }
        // 统一佣金
        DistributionCommissionUnity distributionCommissionUnity = distributionCommissionUnityMapper.get();
        if (!Objects.isNull(distributionCommissionUnity)) {
            rateVOMap.forEach((k,v) -> {
                if (v.getGuideRateStatus() == 0 && v.getGuideRate().compareTo(BigDecimal.ZERO) == 0) {
                    v.setGuideRate(distributionCommissionUnity.getGuideRate());
                    v.setGuideRateActivityId(null);
                    v.setGuideRateStatus(1);
                }
                if (v.getShareRateStatus() == 0 && v.getShareRate().compareTo(BigDecimal.ZERO) == 0) {
                    v.setShareRate(distributionCommissionUnity.getShareRate());
                    v.setShareRateActivityId(null);
                    v.setShareRateStatus(1);
                }
                if (v.getDevelopRateStatus() == 0 && v.getDevelopRate().compareTo(BigDecimal.ZERO) == 0) {
                    v.setDevelopRate(distributionCommissionUnity.getDevelopRate());
                    v.setDevelopRateActivityId(null);
                    v.setDevelopRateStatus(1);
                }
            });
        }
        return rateVOMap;
    }
}
