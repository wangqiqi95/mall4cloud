package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionOrderCommissionLogDTO;
import com.mall4j.cloud.distribution.model.DistributionOrderCommissionLog;
import com.mall4j.cloud.distribution.vo.DistributionOrderCommissionLogVO;

import java.util.List;

/**
 * 订单佣金流水信息service
 * @author Zhang Fan
 * @date 2022/9/9 14:24
 */
public interface DistributionOrderCommissionLogService {
    PageVO<DistributionOrderCommissionLogVO> page(PageDTO pageDTO, DistributionOrderCommissionLogDTO distributionOrderCommissionLogDTO);

    DistributionOrderCommissionLogVO getById(Long id);

    int save(DistributionOrderCommissionLog log);

    int batchSave(List<DistributionOrderCommissionLog> logList);
}
