package com.mall4j.cloud.distribution.service.impl;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.mapper.DistributionCommissionActivityOperationLogMapper;
import com.mall4j.cloud.distribution.model.DistributionCommissionActivityOperationLog;
import com.mall4j.cloud.distribution.service.DistributionCommissionActivityOperationLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 佣金配置-活动佣金-操作日志
 *
 * @author gww
 * @date 2021-12-16 16:04:31
 */
@Service
public class DistributionCommissionActivityOperationLogServiceImpl implements DistributionCommissionActivityOperationLogService {

    @Autowired
    private DistributionCommissionActivityOperationLogMapper distributionCommissionActivityOperationLogMapper;

    @Override
    public PageVO<DistributionCommissionActivityOperationLog> page(PageDTO pageDTO, Long activityId) {
        PageVO<DistributionCommissionActivityOperationLog> pageVO = PageUtil.doPage(pageDTO, () ->
                distributionCommissionActivityOperationLogMapper.listByActivityId(activityId));
        List<DistributionCommissionActivityOperationLog> operationLogList = pageVO.getList();
        if (!CollectionUtils.isEmpty(operationLogList)) {
            operationLogList.stream().forEach(o ->{
                if (StringUtils.isNotBlank(o.getLimitStoreIds())) {
                    o.setLimitStoreCount(Stream.of(o.getLimitStoreIds().split(","))
                            .collect(Collectors.toList()).size());
                }
                if (StringUtils.isNotBlank(o.getLimitSpuIds())) {
                    o.setLimitSpuCount(Stream.of(o.getLimitSpuIds().split(","))
                            .collect(Collectors.toList()).size());
                }
                if (StringUtils.isNotBlank(o.getHisLimitStoreIds())) {
                    o.setHisLimitStoreCount(Stream.of(o.getHisLimitStoreIds().split(","))
                            .collect(Collectors.toList()).size());
                }
                if (StringUtils.isNotBlank(o.getHisLimitSpuIds())) {
                    o.setHisLimitSpuCount(Stream.of(o.getHisLimitSpuIds().split(","))
                            .collect(Collectors.toList()).size());
                }
            });
        }
        return pageVO;
    }
}
