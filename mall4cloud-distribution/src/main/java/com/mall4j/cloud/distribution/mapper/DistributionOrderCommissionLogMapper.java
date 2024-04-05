package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionOrderCommissionLogDTO;
import com.mall4j.cloud.distribution.model.DistributionOrderCommissionLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销订单佣金日志
* @author Zhang Fan
*/
public interface DistributionOrderCommissionLogMapper {

    DistributionOrderCommissionLog getById(@Param("id") Long id);

    List<DistributionOrderCommissionLog> list(@Param("logDTO") DistributionOrderCommissionLogDTO distributionOrderCommissionLogDTO);

    int save(@Param("log") DistributionOrderCommissionLog log);

    int batchSave(@Param("logList") List<DistributionOrderCommissionLog> logList);
}




