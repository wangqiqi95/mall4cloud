package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.api.platform.feign.StoreFeignClient;
import com.mall4j.cloud.api.platform.vo.StoreVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.distribution.dto.DistributionJointVentureCommissionStoreDTO;
import com.mall4j.cloud.distribution.mapper.DistributionJointVentureCommissionCustomerMapper;
import com.mall4j.cloud.distribution.mapper.DistributionJointVentureCommissionStoreMapper;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionCustomer;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionStore;
import com.mall4j.cloud.distribution.service.DistributionJointVentureCommissionStoreService;
import com.mall4j.cloud.distribution.vo.DistributionJointVentureCommissionCustomerStoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zhang Fan
 * @date 2022/8/4 14:31
 */
@Service
public class DistributionJointVentureCommissionStoreServiceImpl implements DistributionJointVentureCommissionStoreService {

    @Autowired
    private StoreFeignClient storeFeignClient;
    @Autowired
    private DistributionJointVentureCommissionStoreMapper distributionJointVentureCommissionStoreMapper;
    @Autowired
    private DistributionJointVentureCommissionCustomerMapper distributionJointVentureCommissionCustomerMapper;

    @Override
    public List<Long> findStoreIdListByJointVentureId(Long jointVentureId) {
        List<DistributionJointVentureCommissionStore> distributionJointVentureCommissionStoreList = distributionJointVentureCommissionStoreMapper.listByJointVentureId(jointVentureId);
        if (!CollectionUtils.isEmpty(distributionJointVentureCommissionStoreList)) {
            return distributionJointVentureCommissionStoreList.stream().map(DistributionJointVentureCommissionStore::getStoreId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public void save(DistributionJointVentureCommissionStoreDTO distributionJointVentureCommissionStoreDTO) {
        if (CollectionUtils.isEmpty(distributionJointVentureCommissionStoreDTO.getStoreIdList())) {
            throw new LuckException("门店ID集合不能为空");
        }
        DistributionJointVentureCommissionCustomer distributionJointVentureCommissionCustomer = distributionJointVentureCommissionCustomerMapper.getById(
                distributionJointVentureCommissionStoreDTO.getJointVentureId());
        if (Objects.isNull(distributionJointVentureCommissionCustomer)) {
            throw new LuckException("联营分佣客户不存在");
        }
        List<DistributionJointVentureCommissionStore> distributionJointVentureCommissionStoreList = distributionJointVentureCommissionStoreMapper
                .listByJointVentureId(distributionJointVentureCommissionCustomer.getId());
        List<Long> jointVentureStoreIdList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(distributionJointVentureCommissionStoreList)) {
            jointVentureStoreIdList = distributionJointVentureCommissionStoreList.stream().map(DistributionJointVentureCommissionStore::getStoreId)
                    .collect(Collectors.toList());
        }
        List<Long> finalJointVentureStoreIdList = jointVentureStoreIdList;
        List<Long> diffStoreIdList = distributionJointVentureCommissionStoreDTO.getStoreIdList().stream().filter(storeId ->
                !finalJointVentureStoreIdList.contains(storeId)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(diffStoreIdList)) {
            validateJointVentureStoreIsItCited(distributionJointVentureCommissionCustomer.getId(), diffStoreIdList);
            List<DistributionJointVentureCommissionStore> jointVentureStoreList = diffStoreIdList.stream().map(s -> {
                DistributionJointVentureCommissionStore jointVentureCommissionStore = new DistributionJointVentureCommissionStore();
                jointVentureCommissionStore.setJointVentureId(distributionJointVentureCommissionCustomer.getId());
                jointVentureCommissionStore.setStoreId(s);
                jointVentureCommissionStore.setOrgId(0L);
                return jointVentureCommissionStore;
            }).collect(Collectors.toList());
            distributionJointVentureCommissionStoreMapper.saveBatch(jointVentureStoreList);
        }
    }

    private void validateJointVentureStoreIsItCited(Long jointVentureId, List<Long> limitStoreIdList) {
        List<DistributionJointVentureCommissionCustomerStoreVO> citedInfo = distributionJointVentureCommissionStoreMapper.listJointVentureStoreIsItCited(jointVentureId, limitStoreIdList);
        if (citedInfo.size() > 0) {
            Map<String, List<DistributionJointVentureCommissionCustomerStoreVO>> citedInfoGroupByCustomerName = citedInfo.stream().collect(Collectors.groupingBy(DistributionJointVentureCommissionCustomerStoreVO::getCustomerName));
            List<Long> citedStoreIdList = citedInfo.stream().map(DistributionJointVentureCommissionCustomerStoreVO::getStoreId).collect(Collectors.toList());
            ServerResponseEntity<List<StoreVO>> storeVOList = storeFeignClient.listByStoreIdList(citedStoreIdList);
            if (storeVOList.isFail()) {
                throw new LuckException("部分门店已分配给其他联营客户");
            }
            Map<Long, List<StoreVO>> storeGroupByStoreId = storeVOList.getData().stream().collect(Collectors.groupingBy(StoreVO::getStoreId));
            StringBuilder exceptionTips = new StringBuilder("部分门店已分配给其他联营客户 ");
            for (Map.Entry<String, List<DistributionJointVentureCommissionCustomerStoreVO>> citedEntry : citedInfoGroupByCustomerName.entrySet()) {
                citedEntry.getValue().forEach(store ->
                        exceptionTips.append(
                                storeGroupByStoreId.get(store.getStoreId()).get(0).getName()
                        )
                                .append("(").append(citedEntry.getKey()).append(") "));
            }
            throw new LuckException(exceptionTips.toString());
        }
    }

    @Override
    public void deleteByJointVentureIdAndStoreId(Long jointVentureId, Long id) {
        distributionJointVentureCommissionStoreMapper.deleteByJointVentureIdAndStoreId(jointVentureId, id);
    }

    @Override
    public List<DistributionJointVentureCommissionStore> findStoreListByJointVentureId(Long jointVentureId) {
        List<DistributionJointVentureCommissionStore> distributionJointVentureCommissionStoreList = distributionJointVentureCommissionStoreMapper.listByJointVentureId(jointVentureId);
        if (!CollectionUtils.isEmpty(distributionJointVentureCommissionStoreList)) {
            distributionJointVentureCommissionStoreList.forEach(store -> {
                StoreVO storeVO = storeFeignClient.findByStoreId(store.getStoreId());
                if (storeVO != null) {
                    store.setStoreName(storeVO.getName());
                    store.setStoreCode(storeVO.getStoreCode());
                }
            });
        }
        return distributionJointVentureCommissionStoreList;
    }
}
