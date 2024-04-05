package com.mall4j.cloud.distribution.service;

import java.util.List;

/**
 * @Author ZengFanChang
 * @Date 2021/12/12
 */
public interface DistributionSettlementService {

    void processSettlement(List<Long> orderIds);

}
