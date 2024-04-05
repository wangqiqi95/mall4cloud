package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionApply;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistributionJointVentureCommissionApplyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(DistributionJointVentureCommissionApply record);

    int insertSelective(DistributionJointVentureCommissionApply record);

    DistributionJointVentureCommissionApply selectByPrimaryKey(Long id);

    DistributionJointVentureCommissionApply selectByAppyNo(String applyNo);

    List<DistributionJointVentureCommissionApply> selectBySelective(DistributionJointVentureCommissionApply record);

    int updateByPrimaryKeySelective(DistributionJointVentureCommissionApply record);

    int updateByPrimaryKey(DistributionJointVentureCommissionApply record);
}