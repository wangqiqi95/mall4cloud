package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.mall4j.cloud.api.product.feign.ProductFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.product.dto.ProductSearchDTO;
import com.mall4j.cloud.common.product.vo.search.SpuCommonVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.CommissionActivitySpuBatchUpdateDTO;
import com.mall4j.cloud.distribution.dto.CommissionActivitySpuSearchDTO;
import com.mall4j.cloud.distribution.dto.DistributionCommissionActivitySpuDTO;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionActivityMapper;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionActivitySpuMapper;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivity;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivitySpu;
import com.mall4j.cloud.distribution.service.DistributionCommissionActivitySpuService;
import com.mall4j.cloud.distribution.vo.DistributionCommissionActivitySpuVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 佣金配置-活动佣金-商品
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
@Service
public class DistributionCommissionActivitySpuServiceImpl implements DistributionCommissionActivitySpuService {

    @Autowired
    private MapperFacade mapperFacade;
    @Autowired
    private DistributionCommissionActivitySpuMapper distributionCommissionActivitySpuMapper;
    @Autowired
    private DistributionCommissionActivityMapper distributionCommissionActivityMapper;
    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public PageVO<DistributionCommissionActivitySpuVO> page(PageDTO pageDTO, CommissionActivitySpuSearchDTO searchDTO) {
        PageVO<DistributionCommissionActivitySpuVO> pageRep = new PageVO<>();
        List<DistributionCommissionActivitySpu> spuList = distributionCommissionActivitySpuMapper.list(searchDTO);
        if (!CollectionUtils.isEmpty(spuList)) {
            Map<Long, DistributionCommissionActivitySpu> commissionActivitySpuMap = spuList.stream()
                    .collect(Collectors.toMap(DistributionCommissionActivitySpu :: getSpuId, Function.identity()));
            List<Long> spuIds = spuList.stream().map(DistributionCommissionActivitySpu :: getSpuId).collect(Collectors.toList());
            ProductSearchDTO productSearch = mapperFacade.map(searchDTO, ProductSearchDTO.class);
            productSearch.setSpuIds(spuIds);
            productSearch.setPageNum(pageDTO.getPageNum());
            productSearch.setPageSize(pageDTO.getPageSize());
            ServerResponseEntity<PageVO<SpuCommonVO>> responseEntity = productFeignClient.commonSearch(productSearch);
            if (responseEntity.getData() != null && !CollectionUtils.isEmpty(responseEntity.getData().getList())) {
                List<SpuCommonVO> spuCommonVOList = responseEntity.getData().getList();
                List<DistributionCommissionActivitySpuVO> commissionActivitySpuVOList = spuCommonVOList.stream().map(spuCommonVO -> {
                    DistributionCommissionActivitySpuVO commissionActivitySpuVO = buildDistributionCommissionActivitySpu(
                            spuCommonVO, commissionActivitySpuMap.get(spuCommonVO.getSpuId()));
                    return commissionActivitySpuVO;
                }).collect(Collectors.toList());

                //商品组件导入排序，按照前端传入spuIds集合顺序排序
                if(CollectionUtil.isNotEmpty(spuIds) && CollectionUtil.isNotEmpty(commissionActivitySpuVOList)){
                    Map<Long,Integer> spuIdsMap=new LinkedHashMap<>();
                    int seq=0;
                    for(Long spuId:spuIds){
                        seq++;
                        spuIdsMap.put(spuId,seq);
                    }
                    commissionActivitySpuVOList.forEach(spuPageVO -> {
                        if(spuIdsMap.containsKey(spuPageVO.getSpuId())){
                            spuPageVO.setSeq(spuIdsMap.get(spuPageVO.getSpuId()));
                        }
                    });
                    Collections.sort(commissionActivitySpuVOList, Comparator.comparingInt(DistributionCommissionActivitySpuVO::getSeq));
                }

                pageRep.setList(commissionActivitySpuVOList);
                pageRep.setPages(responseEntity.getData().getPages());
                pageRep.setTotal(responseEntity.getData().getTotal());
            }
        }
        return pageRep;
    }

    @Override
    public void save(DistributionCommissionActivitySpuDTO distributionCommissionActivitySpuDTO) {
        if (CollectionUtils.isEmpty(distributionCommissionActivitySpuDTO.getSpuIdList())) {
            throw new LuckException("商品ID集合不能为空");
        }
        DistributionCommissionActivity distributionCommissionActivity = distributionCommissionActivityMapper
                .getById(distributionCommissionActivitySpuDTO.getActivityId());
        if (Objects.isNull(distributionCommissionActivity)) {
            throw new LuckException("活动不存在");
        }
        // 保存新添加的 如果商品已单独配置的不做更新
        List<Long> spuIdList = distributionCommissionActivitySpuDTO.getSpuIdList();
        List<DistributionCommissionActivitySpu> commissionActivitySpus = distributionCommissionActivitySpuMapper
                .listByActivityId(distributionCommissionActivity.getId());
        List<Long> activitySpuIdList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(commissionActivitySpus)) {
            activitySpuIdList = commissionActivitySpus.stream().map(DistributionCommissionActivitySpu :: getSpuId)
                    .collect(Collectors.toList());
        }
        List<Long> finalActivitySpuIdList = activitySpuIdList;
        List<Long> diffSpuIdList = spuIdList.stream().filter(t -> !finalActivitySpuIdList.contains(t))
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(diffSpuIdList)) {
            List<DistributionCommissionActivitySpu> distributionCommissionActivitySpuList =
                    spuIdList.stream().map(s -> {
                        DistributionCommissionActivitySpu distributionCommissionActivitySpu = new DistributionCommissionActivitySpu();
                        BeanUtils.copyProperties(distributionCommissionActivity, distributionCommissionActivitySpu);
                        distributionCommissionActivitySpu.setId(null);
                        distributionCommissionActivitySpu.setOrgId(0l);
                        distributionCommissionActivitySpu.setActivityId(distributionCommissionActivity.getId());
                        distributionCommissionActivitySpu.setSpuId(s);
                        return distributionCommissionActivitySpu;
                    }).collect(Collectors.toList());
            distributionCommissionActivitySpuMapper.saveBatch(distributionCommissionActivitySpuList);
        }
    }

    @Override
    public void delete(CommissionActivitySpuBatchUpdateDTO activitySpuBatchUpdateDTO) {
        if (CollectionUtils.isEmpty(activitySpuBatchUpdateDTO.getIdList())) {
            throw new LuckException("Id集合不能为空");
        }
        DistributionCommissionActivity distributionCommissionActivity = distributionCommissionActivityMapper
                .getById(activitySpuBatchUpdateDTO.getActivityId());
        if (Objects.isNull(distributionCommissionActivity)) {
            throw new LuckException("活动不存在");
        }
        distributionCommissionActivitySpuMapper.deleteByIdList(activitySpuBatchUpdateDTO.getIdList());
    }

    @Override
    public void updateCommissionRate(CommissionActivitySpuBatchUpdateDTO activitySpuBatchUpdateDTO) {
        updateBatchCheck(activitySpuBatchUpdateDTO);
        if (activitySpuBatchUpdateDTO.getGuideRate() == null && activitySpuBatchUpdateDTO.getShareRate() == null
                && activitySpuBatchUpdateDTO.getDevelopRate() == null) {
            throw new LuckException("导购&微客&发展佣金不能全为空");
        }
        List<DistributionCommissionActivitySpu> commissionActivitySpuList = activitySpuBatchUpdateDTO.getIdList()
                .stream().map(a -> {
                    DistributionCommissionActivitySpu commissionActivitySpu = new DistributionCommissionActivitySpu();
                    commissionActivitySpu.setId(a);
                    if (activitySpuBatchUpdateDTO.getGuideRate() != null) {
                        commissionActivitySpu.setGuideRate(activitySpuBatchUpdateDTO.getGuideRate());
                    }
                    if (activitySpuBatchUpdateDTO.getShareRate() != null) {
                        commissionActivitySpu.setShareRate(activitySpuBatchUpdateDTO.getShareRate());
                    }
                    if (activitySpuBatchUpdateDTO.getDevelopRate() != null) {
                        commissionActivitySpu.setDevelopRate(activitySpuBatchUpdateDTO.getDevelopRate());
                    }
                    return commissionActivitySpu;
                }).collect(Collectors.toList());
        distributionCommissionActivitySpuMapper.updateBatch(commissionActivitySpuList);
    }


    @Override
    public void updateCommissionRateStatus(CommissionActivitySpuBatchUpdateDTO activitySpuBatchUpdateDTO) {
        updateBatchCheck(activitySpuBatchUpdateDTO);
        if (activitySpuBatchUpdateDTO.getGuideRateStatus() == null && activitySpuBatchUpdateDTO.getShareRateStatus() == null
                && activitySpuBatchUpdateDTO.getDevelopRateStatus() == null) {
            throw new LuckException("导购&微客&发展佣金状态不能全为空");
        }
        List<DistributionCommissionActivitySpu> commissionActivitySpuList = activitySpuBatchUpdateDTO.getIdList()
                .stream().map(a -> {
                    DistributionCommissionActivitySpu commissionActivitySpu = new DistributionCommissionActivitySpu();
                    commissionActivitySpu.setId(a);
                    if (activitySpuBatchUpdateDTO.getGuideRateStatus() != null) {
                        commissionActivitySpu.setGuideRateStatus(activitySpuBatchUpdateDTO.getGuideRateStatus());
                    }
                    if (activitySpuBatchUpdateDTO.getShareRateStatus() != null) {
                        commissionActivitySpu.setShareRateStatus(activitySpuBatchUpdateDTO.getShareRateStatus());
                    }
                    if (activitySpuBatchUpdateDTO.getDevelopRateStatus() != null) {
                        commissionActivitySpu.setDevelopRateStatus(activitySpuBatchUpdateDTO.getDevelopRateStatus());
                    }
                    return commissionActivitySpu;
                }).collect(Collectors.toList());
        distributionCommissionActivitySpuMapper.updateBatch(commissionActivitySpuList);
    }

    @Override
    public void updateActivityTime(CommissionActivitySpuBatchUpdateDTO activitySpuBatchUpdateDTO) {
        DistributionCommissionActivity distributionCommissionActivity = updateBatchCheck(activitySpuBatchUpdateDTO);
        if (activitySpuBatchUpdateDTO.getStartTime() == null && activitySpuBatchUpdateDTO.getEndTime() == null) {
            throw new LuckException("商品活动开始时间和结束时间不能为空");
        }
        if (!activitySpuBatchUpdateDTO.getStartTime().before(activitySpuBatchUpdateDTO.getEndTime())) {
            throw new LuckException("商品活动开始时间不能大于结束时间");
        }
        if (distributionCommissionActivity.getLimitTimeType() == 1) {
            if (!(activitySpuBatchUpdateDTO.getStartTime().after(distributionCommissionActivity.getStartTime())
                    && activitySpuBatchUpdateDTO.getStartTime().before(distributionCommissionActivity.getEndTime())
                    && activitySpuBatchUpdateDTO.getEndTime().after(distributionCommissionActivity.getStartTime())
                    && activitySpuBatchUpdateDTO.getEndTime().before(distributionCommissionActivity.getEndTime()))) {
                throw new LuckException("商品活动开始时间和结束时间只允许在活动有效期内");
            }
        }
        List<DistributionCommissionActivitySpu> commissionActivitySpuList = activitySpuBatchUpdateDTO.getIdList()
                .stream().map(a -> {
                    DistributionCommissionActivitySpu commissionActivitySpu = new DistributionCommissionActivitySpu();
                    commissionActivitySpu.setId(a);
                    if (activitySpuBatchUpdateDTO.getStartTime() != null) {
                        commissionActivitySpu.setStartTime(activitySpuBatchUpdateDTO.getStartTime());
                    }
                    if (activitySpuBatchUpdateDTO.getEndTime() != null) {
                        commissionActivitySpu.setEndTime(activitySpuBatchUpdateDTO.getEndTime());
                    }
                    return commissionActivitySpu;
                }).collect(Collectors.toList());
        distributionCommissionActivitySpuMapper.updateBatch(commissionActivitySpuList);
    }

    @Override
    public List<Long> findSpuIdListByActivityId(Long activityId) {
        return distributionCommissionActivitySpuMapper.listByActivityId(activityId).stream().map(DistributionCommissionActivitySpu :: getSpuId)
                .collect(Collectors.toList());
    }

    private DistributionCommissionActivity updateBatchCheck(CommissionActivitySpuBatchUpdateDTO activitySpuBatchUpdateDTO) {
        if (CollectionUtils.isEmpty(activitySpuBatchUpdateDTO.getIdList())) {
            throw new LuckException("Id集合不能为空");
        }
        DistributionCommissionActivity distributionCommissionActivity = distributionCommissionActivityMapper
                .getById(activitySpuBatchUpdateDTO.getActivityId());
        if (Objects.isNull(distributionCommissionActivity)) {
            throw new LuckException("活动不存在");
        }
        return distributionCommissionActivity;
    }

    private DistributionCommissionActivitySpuVO buildDistributionCommissionActivitySpu(
            SpuCommonVO spuCommonVO, DistributionCommissionActivitySpu distributionCommissionActivitySpu) {
        DistributionCommissionActivitySpuVO distributionCommissionActivitySpuVO = mapperFacade.map(spuCommonVO, DistributionCommissionActivitySpuVO.class);
        distributionCommissionActivitySpuVO.setId(distributionCommissionActivitySpu.getId());
        distributionCommissionActivitySpuVO.setGuideRateStatus(distributionCommissionActivitySpu.getGuideRateStatus());
        distributionCommissionActivitySpuVO.setGuideRate(distributionCommissionActivitySpu.getGuideRate());
        distributionCommissionActivitySpuVO.setShareRateStatus(distributionCommissionActivitySpu.getShareRateStatus());
        distributionCommissionActivitySpuVO.setShareRate(distributionCommissionActivitySpu.getShareRate());
        distributionCommissionActivitySpuVO.setDevelopRateStatus(distributionCommissionActivitySpu.getDevelopRateStatus());
        distributionCommissionActivitySpuVO.setDevelopRate(distributionCommissionActivitySpu.getDevelopRate());
        distributionCommissionActivitySpuVO.setCreateTime(distributionCommissionActivitySpu.getCreateTime());
        distributionCommissionActivitySpuVO.setUpdateTime(distributionCommissionActivitySpu.getUpdateTime());
        distributionCommissionActivitySpuVO.setStartTime(distributionCommissionActivitySpu.getStartTime());
        distributionCommissionActivitySpuVO.setEndTime(distributionCommissionActivitySpu.getEndTime());
        return distributionCommissionActivitySpuVO;
    }
}
