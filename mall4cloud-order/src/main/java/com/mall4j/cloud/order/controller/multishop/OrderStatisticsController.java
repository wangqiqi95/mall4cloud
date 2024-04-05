package com.mall4j.cloud.order.controller.multishop;

import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.order.service.OrderStatisticsService;
import com.mall4j.cloud.order.vo.OrderCountVO;
import com.mall4j.cloud.order.vo.OrderOverviewVO;
import com.mall4j.cloud.order.vo.OrderRefundStatisticsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lth
 */
@RestController("multishopOrderStatisticsController")
@RequestMapping("/m/order_statistics")
@Api(tags = "multishop-订单数据统计接口")
public class OrderStatisticsController {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @GetMapping("/order_count")
    @ApiOperation(value = "查询店铺订单各状态数量", notes = "根据订单状态统计订单数量")
    public ServerResponseEntity<OrderCountVO> getOrderCount() {
        OrderCountVO orderCountVO = orderStatisticsService.getOrderCountOfStatusByShopId(AuthUserContext.get().getTenantId());
        return ServerResponseEntity.success(orderCountVO);
    }

    @GetMapping("/get_to day_by_hour")
    @ApiOperation(value = "获取当天订单实时统计数据", notes = "获取当天订单实时统计数据")
    public ServerResponseEntity<OrderOverviewVO> getToDayInfoByHour() {
        OrderOverviewVO orderOverviewVO = orderStatisticsService.getToDayInfoByHour(AuthUserContext.get().getTenantId(),
                DateUtil.beginOfDay(DateUtil.date()), DateUtil.endOfDay(DateUtil.date()));
        return ServerResponseEntity.success(orderOverviewVO);
    }

    @GetMapping("/get_current_month_by_day")
    @ApiOperation(value = "获取当月订单的统计数据", notes = "获取当月订单的统计数据")
    public ServerResponseEntity<OrderOverviewVO> getCurrentMonthInfoByDay() {
        OrderOverviewVO orderOverviewVO = orderStatisticsService.getCurrentMonthByDay(AuthUserContext.get().getTenantId(),
                DateUtil.beginOfMonth(DateUtil.date()), DateUtil.endOfDay(DateUtil.date()));
        return ServerResponseEntity.success(orderOverviewVO);
    }

    @GetMapping("/list_order_refund_info")
    @ApiOperation(value = "获取近30天的退款订单比率信息及退款金额统计列表", notes = "获取近30天的退款订单比率信息及退款金额统计列表")
    public ServerResponseEntity<List<OrderRefundStatisticsVO>> listOrderRefundInfoInThirtyDay() {
        List<OrderRefundStatisticsVO> orderRefundStatisticsVOList = orderStatisticsService.listOrderRefundInfoInDateRange(AuthUserContext.get().getTenantId(), DateUtil.endOfDay(DateUtil.date()), 30);
        return ServerResponseEntity.success(orderRefundStatisticsVOList);
    }

    @GetMapping("/list_refund_ranking_by_prod")
    @ApiOperation(value = "根据商品名生成退款排行", notes = "根据商品名生成退款排行")
    public ServerResponseEntity<List<OrderRefundStatisticsVO>> listRefundRankingByProd() {
        List<OrderRefundStatisticsVO> orderRefundStatisticsVOList = orderStatisticsService.listRefundRankingByProd(AuthUserContext.get().getTenantId(), DateUtil.endOfDay(DateUtil.date()), 30);
        return ServerResponseEntity.success(orderRefundStatisticsVOList);
    }

    @GetMapping("/list_refund_ranking_by_reason")
    @ApiOperation(value = "根据退款原因生成退款排行")
    public ServerResponseEntity<List<OrderRefundStatisticsVO>> listRefundRankingByReason() {
        List<OrderRefundStatisticsVO> refundList = orderStatisticsService.listRefundRankingByReason(
                AuthUserContext.get().getTenantId(), DateUtil.endOfDay(DateUtil.date()), 30);
        return ServerResponseEntity.success(refundList);
    }

}
