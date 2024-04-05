package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionApplyOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistributionJointVentureCommissionApplyOrderMapper {
    int deleteByPrimaryKey(Long id);

    int deleteByApplyId(Long applyId);

    int insert(DistributionJointVentureCommissionApplyOrder record);

    int insertSelective(DistributionJointVentureCommissionApplyOrder record);

    DistributionJointVentureCommissionApplyOrder selectByPrimaryKey(Long id);

    List<DistributionJointVentureCommissionApplyOrder> selectByApplyId(Long applyId);

    int updateByPrimaryKeySelective(DistributionJointVentureCommissionApplyOrder record);

    int updateByPrimaryKey(DistributionJointVentureCommissionApplyOrder record);

    int batchInsertSelective(@Param("applyOrderList") List<DistributionJointVentureCommissionApplyOrder> jointVentureCommissionApplyOrderList);

    int countProcessingOrderByOrderIdList(@Param("applyOrderIdList") List<Long> applyOrderIdList);
}