package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.distribution.dto.DistributionJointVentureCommissionStoreDTO;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionStore;

import java.util.List;

/**
 * 联营分佣门店service
 *
 * @author Zhang Fan
 * @date 2022/8/4 14:29
 */
public interface DistributionJointVentureCommissionStoreService {

    /**
     * 通过联营分佣id查询适用门店id集合
     *
     * @param jointVentureId
     * @return
     */
    List<Long> findStoreIdListByJointVentureId(Long jointVentureId);

    /**
     * 佣金联营分佣添加门店
     *
     * @param distributionJointVentureCommissionStoreDTO
     */
    void save(DistributionJointVentureCommissionStoreDTO distributionJointVentureCommissionStoreDTO);

    /**
     * 佣金联营分佣删除门店
     *
     * @param jointVentureId 联营分佣id
     * @param id             门店id
     */
    void deleteByJointVentureIdAndStoreId(Long jointVentureId, Long id);

    /**
     * 通过联营分佣id查询适用门店集合
     *
     * @param jointVentureId
     * @return
     */
    List<DistributionJointVentureCommissionStore> findStoreListByJointVentureId(Long jointVentureId);
}
