package com.mall4j.cloud.order.controller.app;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.order.dto.app.DistributionOrderSearchDTO;
import com.mall4j.cloud.order.dto.app.DistributionSalesStatDTO;
import com.mall4j.cloud.order.service.OrderService;
import com.mall4j.cloud.order.vo.DistributionOrderVO;
import com.mall4j.cloud.order.vo.DistributionSalesStatVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController("appDistributionOrderController")
@RequestMapping("/distribution_order")
@Api(tags = "微客小程序-分销订单")
public class DistributionOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/settleOrderList")
    @ApiOperation(value = "全部/待结算/已结算列表", notes = "全部/待结算/已结算列表")
    public ServerResponseEntity<List<DistributionOrderVO>> page(@Valid PageDTO pageDTO, @Valid DistributionOrderSearchDTO distributionOrderSearchDTO) {
        distributionOrderSearchDTO.setDistributionUserId(AuthUserContext.get().getUserId());
        distributionOrderSearchDTO.setDistributionUserType(2);
        return ServerResponseEntity.success(orderService.listPageWithDistributionUser(pageDTO, distributionOrderSearchDTO));
    }

    @GetMapping("/sales")
    @ApiOperation(value = "业绩统计", notes = "今日&累计业绩")
    public ServerResponseEntity<DistributionSalesStatVO> sales() {
        DistributionSalesStatVO distributionSalesStatVO = new DistributionSalesStatVO();
        DistributionSalesStatDTO distributionSalesStatDTO = new DistributionSalesStatDTO();
        distributionSalesStatDTO.setDistributionUserType(2);
        distributionSalesStatDTO.setDistributionUserId(AuthUserContext.get().getUserId());
        Long totalSales = orderService.distributionSalesStat(distributionSalesStatDTO);
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        ZoneId zone = ZoneId.systemDefault();
        distributionSalesStatDTO.setStartTime(Date.from(todayStart.atZone(zone).toInstant()));
        distributionSalesStatDTO.setEndTime(Date.from(todayEnd.atZone(zone).toInstant()));
        Long todaySales = orderService.distributionSalesStat(distributionSalesStatDTO);
        distributionSalesStatVO.setTodaySales(todaySales);
        distributionSalesStatVO.setTotalSales(totalSales);
        return ServerResponseEntity.success(distributionSalesStatVO);
    }

}
