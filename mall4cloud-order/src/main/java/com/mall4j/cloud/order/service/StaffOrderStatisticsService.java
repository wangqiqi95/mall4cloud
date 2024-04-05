package com.mall4j.cloud.order.service;


import com.mall4j.cloud.order.dto.OrderDetailReportRequest;
import com.mall4j.cloud.order.vo.StaffOrderOverviewVO;

public interface StaffOrderStatisticsService {

    StaffOrderOverviewVO getStaffOrderOverviewVO(Long staffId);

    StaffOrderOverviewVO getOrderDetailReport(OrderDetailReportRequest orderDetailReportRequest);
}
