package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionStore;
import com.mall4j.cloud.distribution.vo.DistributionJointVentureCommissionCustomerStoreVO;
import com.mall4j.cloud.distribution.vo.DistributionJointVentureCommissionStoreCountVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 联营分佣门店mapper
 *
 * @author Zhang Fan
 * @date 2022/8/4 11:58
 */
@Repository
public interface DistributionJointVentureCommissionStoreMapper {

    /**
     * 通过联营分佣id查询适用门店集合
     *
     * @param jointVentureId
     * @return 门店列表
     */
    List<DistributionJointVentureCommissionStore> listByJointVentureId(@Param("jointVentureId") Long jointVentureId);

    /**
     * 通过门店id查询适用门店集合
     *
     * @param storeId
     * @return 门店列表
     */
    List<DistributionJointVentureCommissionStore> listByStoreId(@Param("storeId") Long storeId);

    /**
     * 保存门店
     *
     * @param distributionJointVentureCommissionStore 门店
     */
    void save(@Param("entity") DistributionJointVentureCommissionStore distributionJointVentureCommissionStore);

    /**
     * 修改门店
     *
     * @param distributionJointVentureCommissionStore 门店
     */
    void update(@Param("entity") DistributionJointVentureCommissionStore distributionJointVentureCommissionStore);

    /**
     * 根据门店id删除门店
     *
     * @param storeId
     */
    void deleteByJointVentureIdAndStoreId(@Param("jointVentureId") Long jointVentureId, @Param("storeId") Long storeId);

    /**
     * 根据联营分佣id删除门店
     *
     * @param jointVentureId
     */
    void deleteByJointVentureId(@Param("jointVentureId") Long jointVentureId);

    /**
     * 批量保存门店
     *
     * @param jointVentureStoreList
     */
    void saveBatch(@Param("jointVentureStoreList") List<DistributionJointVentureCommissionStore> jointVentureStoreList);

    /**
     * 分组统计联营分佣下门店
     *
     * @return
     */
    List<DistributionJointVentureCommissionStoreCountVO> countByJointVentureIdList(@Param("jointVentureIdList") List<Long> jointVentureIdList);

    /**
     * 同居联营分佣下门店
     *
     * @param jointVentureId 联营分佣客户id
     * @return
     */
    Integer countByJointVentureId(@Param("jointVentureId") Long jointVentureId);

    /**
     * 获取已被其他联营分佣客户配置的门店
     *
     * @param jointVentureId
     * @param limitStoreIdList
     * @return
     */
    List<DistributionJointVentureCommissionCustomerStoreVO> listJointVentureStoreIsItCited(@Param("jointVentureId") Long jointVentureId, @Param("limitStoreIdList") List<Long> limitStoreIdList);

    int batchDeleteByJointVentureIdAndStoreIds(@Param("jointVentureId") Long jointVentureId, @Param("needDeleteStoreIdList") List<Long> needDeleteStoreIdList);
}
