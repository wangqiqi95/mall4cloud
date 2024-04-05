package com.mall4j.cloud.order.service;

import com.mall4j.cloud.order.dto.app.DistributionSalesStatDTO;

/**
 * @Author ZengFanChang
 * @Date 2022/04/05
 */
public interface DistributionOrderExcelService {

    void exportStoreStatistics(DistributionSalesStatDTO distributionSalesStatDTO);

    void exportStaffStatistics(DistributionSalesStatDTO distributionSalesStatDTO);

    void exportWitkeyStatistics(DistributionSalesStatDTO distributionSalesStatDTO);
}
