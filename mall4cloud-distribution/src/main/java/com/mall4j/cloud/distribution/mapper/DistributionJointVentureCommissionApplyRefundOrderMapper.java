package com.mall4j.cloud.distribution.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.distribution.model.DistributionJointVentureCommissionApplyRefundOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistributionJointVentureCommissionApplyRefundOrderMapper extends BaseMapper<DistributionJointVentureCommissionApplyRefundOrder> {

    int batchInsert(@Param("refundOrders") List<DistributionJointVentureCommissionApplyRefundOrder> refundOrders);
}