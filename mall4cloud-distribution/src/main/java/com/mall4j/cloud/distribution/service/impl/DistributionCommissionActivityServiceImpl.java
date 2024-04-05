package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.distribution.dto.DistributionCommissionActivityDTO;
import com.mall4j.cloud.distribution.dto.DistributionCommissionActivitySearchDTO;
import com.mall4j.cloud.distribution.dto.DistributionCommissionActivityUpdateDTO;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionActivityMapper;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionActivityOperationLogMapper;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionActivitySpuMapper;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionActivityStoreMapper;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivity;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivityOperationLog;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivitySpu;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivityStore;
import com.mall4j.cloud.distribution.service.DistributionCommissionActivityService;
import com.mall4j.cloud.distribution.vo.DistributionCommissionActivityCountVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 佣金配置-活动佣金
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
@Service
public class DistributionCommissionActivityServiceImpl implements DistributionCommissionActivityService {

    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private DistributionCommissionActivityMapper distributionCommissionActivityMapper;
    @Autowired
    private DistributionCommissionActivityStoreMapper distributionCommissionActivityStoreMapper;
    @Autowired
    private DistributionCommissionActivitySpuMapper distributionCommissionActivitySpuMapper;
    @Autowired
    private DistributionCommissionActivityOperationLogMapper distributionCommissionActivityOperationLogMapper;

    @Override
    public PageVO<DistributionCommissionActivity> page(PageDTO pageDTO,  DistributionCommissionActivitySearchDTO commissionActivitySearchDTO) {
        PageVO<DistributionCommissionActivity> page = PageUtil.doPage(pageDTO, () -> distributionCommissionActivityMapper
                .list(commissionActivitySearchDTO));
        if (!CollectionUtils.isEmpty(page.getList())) {
            List<Long> limitStoreActivityIdList =  page.getList().stream().filter(d -> d.getLimitStoreType() == 1)
                    .map(DistributionCommissionActivity :: getId)
                    .collect(Collectors.toList());
            Map<Long, Integer> limitStoreCountMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(limitStoreActivityIdList)) {
                limitStoreCountMap = distributionCommissionActivityStoreMapper.countByActivityIdList(limitStoreActivityIdList)
                        .stream().collect(Collectors.toMap(DistributionCommissionActivityCountVO:: getActivityId,
                                DistributionCommissionActivityCountVO:: getNum));
            }
            Map<Long, Integer> limitSpuCountMap = new HashMap<>();
            List<Long> limitSpuActivityIdList =  page.getList().stream().filter(d -> d.getLimitSpuType() == 1)
                    .map(DistributionCommissionActivity :: getId)
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(limitSpuActivityIdList)) {
                limitSpuCountMap = distributionCommissionActivitySpuMapper.countByActivityIdList(limitSpuActivityIdList)
                        .stream().collect(Collectors.toMap(DistributionCommissionActivityCountVO:: getActivityId,
                                DistributionCommissionActivityCountVO:: getNum));
            }
            Map<Long, Integer> finalLimitStoreCountMap = limitStoreCountMap;
            Map<Long, Integer> finalLimitSpuCountMap = limitSpuCountMap;
            page.getList().stream().forEach(a -> {
                Long activityId = a.getId();
                if (finalLimitStoreCountMap.containsKey(activityId)) {
                    a.setLimitStoreNum(finalLimitStoreCountMap.get(activityId));
                }
                if (finalLimitSpuCountMap.containsKey(activityId)) {
                    a.setLimitSpuNum(finalLimitSpuCountMap.get(activityId));
                }
            });
        }
        return page;
    }

    @Override
    public DistributionCommissionActivity getById(Long id) {
        DistributionCommissionActivity distributionCommissionActivity = distributionCommissionActivityMapper.getById(id);
        if (Objects.nonNull(distributionCommissionActivity)) {
            Long activityId = distributionCommissionActivity.getId();
            if (distributionCommissionActivity.getLimitStoreType() == 1) {
                List<DistributionCommissionActivityStore> storeList = distributionCommissionActivityStoreMapper.listByActivityId(activityId);
                if (!CollectionUtils.isEmpty(storeList)) {
                    distributionCommissionActivity.setLimitStoreNum(storeList.size());
                }
            }
            if (distributionCommissionActivity.getLimitSpuType() == 1) {
                List<DistributionCommissionActivitySpu> spuList = distributionCommissionActivitySpuMapper.listByActivityId(activityId);
                if (!CollectionUtils.isEmpty(spuList)) {
                    distributionCommissionActivity.setLimitSpuNum(spuList.size());
                }
            }
        }
        return distributionCommissionActivity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(DistributionCommissionActivityDTO distributionCommissionActivityDTO) {
        DistributionCommissionActivity distributionCommissionActivity = mapperFacade.map(distributionCommissionActivityDTO,
                DistributionCommissionActivity.class);
        distributionCommissionActivity.setId(null);
        distributionCommissionActivity.setOrgId(0l);
        distributionCommissionActivity.setDeleted(0);
        // 默认为最大的优先级
        Integer maxMaxPriority = distributionCommissionActivityMapper.getMaxPriority();
        distributionCommissionActivity.setPriority(maxMaxPriority+1);
        // 保存活动信息
        distributionCommissionActivityMapper.save(distributionCommissionActivity);
        Long activityId = distributionCommissionActivity.getId();
        Integer limitStoreType = distributionCommissionActivityDTO.getLimitStoreType();
        // 适用门店保存
        List<Long> limitStoreIdList = distributionCommissionActivityDTO.getLimitStoreIdList();
        if (limitStoreType == 1 && !CollectionUtils.isEmpty(limitStoreIdList)) {
            batchSaveDistributionCommissionActivityStore(activityId, limitStoreIdList);
        }
        // 适用商品保存(每个商品可单独设置佣金比例、状态、活动时间)
        Integer limitSpuType = distributionCommissionActivityDTO.getLimitSpuType();
        List<Long> limitSpuIdList = distributionCommissionActivityDTO.getLimitSpuIdList();
        if (limitSpuType == 1 && !CollectionUtils.isEmpty(limitSpuIdList)) {
            batchSaveDistributionCommissionActivitySpu(distributionCommissionActivityDTO, activityId, limitSpuIdList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DistributionCommissionActivityDTO distributionCommissionActivityDTO) {
        DistributionCommissionActivity oldDistributionCommissionActivity = distributionCommissionActivityMapper
                .getById(distributionCommissionActivityDTO.getId());
        if (Objects.isNull(oldDistributionCommissionActivity)) {
            throw new LuckException("活动不存在");
        }
        DistributionCommissionActivity distributionCommissionActivity = mapperFacade.map(distributionCommissionActivityDTO,
                DistributionCommissionActivity.class);
        distributionCommissionActivityMapper.update(distributionCommissionActivity);
        Long activityId = distributionCommissionActivity.getId();
        Integer limitStoreType = distributionCommissionActivityDTO.getLimitStoreType();
        List<Long> limitStoreIdList = distributionCommissionActivityDTO.getLimitStoreIdList();
        List<Long> activityStoreIdList = new ArrayList<>();
        if (limitStoreType == 0) {
            distributionCommissionActivityStoreMapper.deleteByActivityId(distributionCommissionActivity.getId());
        } else if (limitStoreType == 1 && !CollectionUtils.isEmpty(distributionCommissionActivityDTO.getLimitStoreIdList())) {
            // 适用门店保存
//            List<DistributionCommissionActivityStore> distributionCommissionActivityStoreList = distributionCommissionActivityStoreMapper
//                    .listByActivityId(activityId);
//            if (!CollectionUtils.isEmpty(distributionCommissionActivityStoreList)) {
//                activityStoreIdList = distributionCommissionActivityStoreList.stream().map(DistributionCommissionActivityStore:: getStoreId)
//                        .collect(Collectors.toList());
//            }
//            List<Long> finalActivityStoreIdList = activityStoreIdList;
//            List<Long> diffStoreIdList = limitStoreIdList.stream().filter(t -> !finalActivityStoreIdList.contains(t)).collect(Collectors.toList());
//            if (!CollectionUtils.isEmpty(diffStoreIdList)) {
//                batchSaveDistributionCommissionActivityStore(distributionCommissionActivity.getId(), limitStoreIdList);
//            }

            //如果适用门店不为空 先删除原来活动的适用门店字段，再新增新的活动门店数据
            if (!CollectionUtils.isEmpty(limitStoreIdList)) {
                distributionCommissionActivityStoreMapper.deleteByActivityId(distributionCommissionActivity.getId());
                batchSaveDistributionCommissionActivityStore(distributionCommissionActivity.getId(), limitStoreIdList);
            }

        }
        // 适用商品保存
        Integer limitSpuType = distributionCommissionActivityDTO.getLimitSpuType();
        List<Long> limitSpuIdList = distributionCommissionActivityDTO.getLimitSpuIdList();
        List<Long> activitySpuIdList = new ArrayList<>();
        if (limitSpuType == 1 && !CollectionUtils.isEmpty(limitSpuIdList)) {
            // 保存新添加的 如果商品已单独配置的不做更新
            List<DistributionCommissionActivitySpu> commissionActivitySpus = distributionCommissionActivitySpuMapper
                    .listByActivityId(distributionCommissionActivity.getId());
            if (!CollectionUtils.isEmpty(commissionActivitySpus)) {
                activitySpuIdList = commissionActivitySpus.stream().map(DistributionCommissionActivitySpu :: getSpuId)
                        .collect(Collectors.toList());
            }
            List<Long> finalActivitySpuIdList = activitySpuIdList;
            List<Long> diffSpuIdList = limitSpuIdList.stream().filter(t -> !finalActivitySpuIdList.contains(t))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(diffSpuIdList)) {
                batchSaveDistributionCommissionActivitySpu(distributionCommissionActivityDTO, activityId, limitSpuIdList);
            }
        }
        // 活动更新日志记录
        DistributionCommissionActivityOperationLog activityOperationLog = mapperFacade.map(distributionCommissionActivityDTO,
                DistributionCommissionActivityOperationLog.class);
        activityOperationLog.setId(null);
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        activityOperationLog.setOrgId(0l);
        activityOperationLog.setActivityId(activityId);
        activityOperationLog.setOperationUserId(userInfoInTokenBO.getUserId());
        activityOperationLog.setOperationUserName(userInfoInTokenBO.getUsername());
        activityOperationLog.setHisLimitTimeType(oldDistributionCommissionActivity.getLimitTimeType());
        activityOperationLog.setHisLimitStoreType(oldDistributionCommissionActivity.getLimitStoreType());
        activityOperationLog.setHisLimitSpuType(oldDistributionCommissionActivity.getLimitSpuType());
        if (!CollectionUtils.isEmpty(limitStoreIdList)) {
            activityOperationLog.setLimitStoreCount(limitStoreIdList.size());
            activityOperationLog.setLimitStoreIds(limitStoreIdList.stream().map(s -> s.toString())
                    .collect(Collectors.joining(",")));
        }
        if (!CollectionUtils.isEmpty(activityStoreIdList)) {
            activityOperationLog.setHisLimitStoreCount(activityStoreIdList.size());
            activityOperationLog.setHisLimitStoreIds(activityStoreIdList.stream().map(s -> s.toString())
                    .collect(Collectors.joining(",")));
        }
        if (!CollectionUtils.isEmpty(limitSpuIdList)) {
            activityOperationLog.setLimitSpuCount(limitSpuIdList.size());
            activityOperationLog.setLimitSpuIds(limitSpuIdList.stream().map(s -> s.toString())
                    .collect(Collectors.joining(",")));
        }
        if (!CollectionUtils.isEmpty(activitySpuIdList)) {
            activityOperationLog.setHisLimitSpuCount(activitySpuIdList.size());
            activityOperationLog.setHisLimitSpuIds(activitySpuIdList.stream().map(s -> s.toString())
                    .collect(Collectors.joining(",")));
        }
        activityOperationLog.setHisStartTime(oldDistributionCommissionActivity.getStartTime());
        activityOperationLog.setHisEndTime(oldDistributionCommissionActivity.getEndTime());
        activityOperationLog.setHisGuideRateStatus(oldDistributionCommissionActivity.getGuideRateStatus());
        activityOperationLog.setHisGuideRate(oldDistributionCommissionActivity.getGuideRate());
        activityOperationLog.setHisShareRateStatus(oldDistributionCommissionActivity.getShareRateStatus());
        activityOperationLog.setHisShareRate(oldDistributionCommissionActivity.getShareRate());
        activityOperationLog.setHisDevelopRateStatus(oldDistributionCommissionActivity.getDevelopRateStatus());
        activityOperationLog.setHisDevelopRate(oldDistributionCommissionActivity.getDevelopRate());
        distributionCommissionActivityOperationLogMapper.save(activityOperationLog);
    }

    @Override
    public void deleteById(Long id) {
        distributionCommissionActivityMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePriority(Long sourceId, Long targetId) {
        DistributionCommissionActivity sourceActivity = distributionCommissionActivityMapper.getById(sourceId);
        if (Objects.isNull(sourceActivity)) {
            throw new LuckException("原活动不存在");
        }
        DistributionCommissionActivity targetActivity = distributionCommissionActivityMapper.getById(targetId);
        if (Objects.isNull(sourceActivity)) {
            throw new LuckException("目标活动不存在");
        }
        Integer sourcePriority = sourceActivity.getPriority();
        Integer targetPriority = targetActivity.getPriority();
        sourceActivity.setPriority(targetPriority);
        targetActivity.setPriority(sourcePriority);
        distributionCommissionActivityMapper.update(sourceActivity);
        distributionCommissionActivityMapper.update(targetActivity);
    }

    @Override
    public void updateStatus(DistributionCommissionActivityUpdateDTO distributionCommissionActivityUpdateDTO) {
        DistributionCommissionActivity distributionCommissionActivity = distributionCommissionActivityMapper.getById(
                distributionCommissionActivityUpdateDTO.getId());
        distributionCommissionActivity.setStatus(distributionCommissionActivityUpdateDTO.getStatus());
        distributionCommissionActivity.setGuideRateStatus(distributionCommissionActivityUpdateDTO.getGuideRateStatus());
        distributionCommissionActivity.setDevelopRateStatus(distributionCommissionActivityUpdateDTO.getDevelopRateStatus());
        distributionCommissionActivity.setShareRateStatus(distributionCommissionActivityUpdateDTO.getShareRateStatus());
        distributionCommissionActivityMapper.update(distributionCommissionActivity);
    }

    @Override
    public void updateRate(DistributionCommissionActivityUpdateDTO distributionCommissionActivityUpdateDTO) {
        DistributionCommissionActivity distributionCommissionActivity = distributionCommissionActivityMapper.getById(
                distributionCommissionActivityUpdateDTO.getId());
        distributionCommissionActivity.setShareRate(distributionCommissionActivityUpdateDTO.getShareRate());
        distributionCommissionActivity.setGuideRate(distributionCommissionActivityUpdateDTO.getGuideRate());
        distributionCommissionActivity.setDevelopRate(distributionCommissionActivityUpdateDTO.getDevelopRate());
        distributionCommissionActivityMapper.update(distributionCommissionActivity);
    }

    @Override
    public void updateActivityTime(DistributionCommissionActivityUpdateDTO distributionCommissionActivityUpdateDTO) {
        DistributionCommissionActivity distributionCommissionActivity = distributionCommissionActivityMapper.getById(
                distributionCommissionActivityUpdateDTO.getId());
        distributionCommissionActivity.setStartTime(distributionCommissionActivityUpdateDTO.getStartTime());
        distributionCommissionActivity.setEndTime(distributionCommissionActivityUpdateDTO.getEndTime());
        distributionCommissionActivityMapper.update(distributionCommissionActivity);
    }

    private void batchSaveDistributionCommissionActivityStore(Long activityId,
                                                              List<Long> limitStoreIdList) {
        List<DistributionCommissionActivityStore> activityStoreList = limitStoreIdList.stream().map(s -> {
            DistributionCommissionActivityStore commissionActivityStore = new DistributionCommissionActivityStore();
            commissionActivityStore.setActivityId(activityId);
            commissionActivityStore.setStoreId(s);
            commissionActivityStore.setOrgId(0l);
            return commissionActivityStore;
        }).collect(Collectors.toList());
        distributionCommissionActivityStoreMapper.saveBatch(activityStoreList);
    }

    private void batchSaveDistributionCommissionActivitySpu(DistributionCommissionActivityDTO distributionCommissionActivityDTO,
                                                            Long activityId,
                                                            List<Long> limitSpuIdList) {
        List<DistributionCommissionActivitySpu> distributionCommissionActivitySpuList =
                limitSpuIdList.stream().map(s -> {
                    DistributionCommissionActivitySpu distributionCommissionActivitySpu = new DistributionCommissionActivitySpu();
                    BeanUtils.copyProperties(distributionCommissionActivityDTO, distributionCommissionActivitySpu);
                    distributionCommissionActivitySpu.setId(null);
                    distributionCommissionActivitySpu.setOrgId(0l);
                    distributionCommissionActivitySpu.setActivityId(activityId);
                    distributionCommissionActivitySpu.setSpuId(s);
                    return distributionCommissionActivitySpu;
                }).collect(Collectors.toList());
        distributionCommissionActivitySpuMapper.saveBatch(distributionCommissionActivitySpuList);
    }


}
