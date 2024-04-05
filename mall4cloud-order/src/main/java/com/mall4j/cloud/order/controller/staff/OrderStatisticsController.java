package com.mall4j.cloud.order.controller.staff;

import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.order.dto.OrderDetailReportRequest;
import com.mall4j.cloud.order.service.OrderStatisticsService;
import com.mall4j.cloud.order.service.StaffOrderStatisticsService;
import com.mall4j.cloud.order.vo.OrderOverviewVO;
import com.mall4j.cloud.order.vo.OrderRefundStatisticsVO;
import com.mall4j.cloud.order.vo.StaffOrderOverviewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController("staffOrderStatisticsController")
@RequestMapping("/s/order_statistics")
@Api(tags = "staff-订单数据统计接口")
public class OrderStatisticsController {

    @Autowired
    private StaffOrderStatisticsService staffOrderStatisticsService;

//    @GetMapping("/get_detail_by_hour")
//    @ApiOperation(value = "获取当天与昨天订单实时统计数据", notes = "获取当天与昨天订单实时统计数据")
//    public ServerResponseEntity<OrderOverviewVO> getDetailInfoByHour() {
//        OrderOverviewVO orderOverviewVO = orderStatisticsService.getDetailInfoByHour(null);
//        return ServerResponseEntity.success(orderOverviewVO);
//    }

    @GetMapping("/get_order_info_by_day_count")
    @ApiOperation(value = "导购销售报表看板", notes = "导购销售报表看板")
    public ServerResponseEntity<StaffOrderOverviewVO> getOrderInfoByDayCount() {
        Long staffId = AuthUserContext.get().getUserId();
        StaffOrderOverviewVO staffOrderOverviewVO = staffOrderStatisticsService.getStaffOrderOverviewVO(staffId);
        return ServerResponseEntity.success(staffOrderOverviewVO);
    }


    @PostMapping("/get_order_detail_report")
    @ApiOperation(value = "导购销售数据看板概览", notes = "导购销售数据看板概览")
    public ServerResponseEntity<StaffOrderOverviewVO> getOrderDetailReport(@Valid @RequestBody OrderDetailReportRequest orderDetailReportRequest) {
        orderDetailReportRequest.check();

        Long staffId = AuthUserContext.get().getUserId();
        orderDetailReportRequest.setStaffId(staffId);
        StaffOrderOverviewVO staffOrderOverviewVO = staffOrderStatisticsService.getOrderDetailReport(orderDetailReportRequest);
        return ServerResponseEntity.success(staffOrderOverviewVO);
    }

}
