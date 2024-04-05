package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.api.distribution.dto.DistributionCommissionRateDTO;
import com.mall4j.cloud.api.distribution.vo.DistributionCommissionRateVO;

import java.util.List;
import java.util.Map;

/**
 * 导航&分销-佣金配置
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
public interface DistributionCommissionService {

    /**
     * 通过商品id集合和门店获取佣金比例
     * @param distributionCommissionRateDTO
     * @return
     */
    Map<Long, DistributionCommissionRateVO> getDistributionCommissionRate(DistributionCommissionRateDTO distributionCommissionRateDTO);
}
