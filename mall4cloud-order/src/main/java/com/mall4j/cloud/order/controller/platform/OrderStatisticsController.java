package com.mall4j.cloud.order.controller.platform;

import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.order.service.OrderStatisticsService;
import com.mall4j.cloud.order.vo.OrderOverviewVO;
import com.mall4j.cloud.order.vo.OrderRefundStatisticsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author lth
 * @Date 2021/4/30 14:01
 */
@RestController("platformOrderStatisticsController")
@RequestMapping("/p/order_statistics")
@Api(tags = "platform-订单数据统计接口")
public class OrderStatisticsController {

    @Autowired
    private OrderStatisticsService orderStatisticsService;

    @GetMapping("/get_detail_by_hour")
    @ApiOperation(value = "获取当天与昨天订单实时统计数据", notes = "获取当天与昨天订单实时统计数据")
    public ServerResponseEntity<OrderOverviewVO> getDetailInfoByHour() {
        OrderOverviewVO orderOverviewVO = orderStatisticsService.getDetailInfoByHour(null);
        return ServerResponseEntity.success(orderOverviewVO);
    }

    @GetMapping("/get_order_info_by_day_count")
    @ApiOperation(value = "获取近多少天内的订单统计数据", notes = "获取近多少天内订单内的订单统计数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dayCount", value = "天数（几天内的统计数据）", required = false, dataType = "Integer", defaultValue = "30")
    })
    public ServerResponseEntity<OrderOverviewVO> getCurrentMonthInfoByDay(@RequestParam(value = "dayCount", defaultValue = "30") Integer dayCount) {
        OrderOverviewVO orderOverviewVO = orderStatisticsService.getOrderInfoByDayCountAndShopId(null, dayCount);
        return ServerResponseEntity.success(orderOverviewVO);
    }

    @GetMapping("/list_spu_ranking_by_order_count")
    @ApiOperation(value = "获取商品订单数量排行榜", notes = "获取商品订单数量排行榜")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dayCount", value = "天数（几天内的排行榜）", required = false, dataType = "Integer", defaultValue = "30"),
            @ApiImplicitParam(name = "limit", value = "排行榜条数", required = false, dataType = "Integer", defaultValue = "10")
    })
    public ServerResponseEntity<List<OrderOverviewVO>> listSpuRankingByPayActual(@RequestParam(value = "dayCount", defaultValue = "30") Integer dayCount,
                                                                               @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        List<OrderOverviewVO> orderOverviewVOList = orderStatisticsService.listSpuRankingByOrderCount(null, DateUtil.endOfDay(DateUtil.date()), dayCount, limit);
        return ServerResponseEntity.success(orderOverviewVOList);
    }

    @GetMapping("/list_shop_ranking_by_pay_actual")
    @ApiOperation(value = "获取店铺销售排行榜", notes = "获取店铺销售排行榜")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dayCount", value = "天数（几天内的排行榜）", required = false, dataType = "Integer", defaultValue = "30"),
            @ApiImplicitParam(name = "limit", value = "排行榜条数", required = false, dataType = "Integer", defaultValue = "10")
    })
    public ServerResponseEntity<List<OrderOverviewVO>> listShopRankingByPayActual(@RequestParam(value = "dayCount", defaultValue = "30") Integer dayCount,
                                                                                @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        List<OrderOverviewVO> orderOverviewVOList = orderStatisticsService.listShopRankingByPayActual(DateUtil.endOfDay(DateUtil.date()), dayCount, limit);
        return ServerResponseEntity.success(orderOverviewVOList);
    }

    @GetMapping("/list_shop_ranking_by_refund_count")
    @ApiOperation(value = "获取店铺退款订单数量排行榜", notes = "获取店铺退款订单数量排行榜")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dayCount", value = "天数（几天内的排行榜）", required = false, dataType = "Integer", defaultValue = "30"),
            @ApiImplicitParam(name = "limit", value = "排行榜条数", required = false, dataType = "Integer", defaultValue = "10")
    })
    public ServerResponseEntity<List<OrderRefundStatisticsVO>> listRefundRankingByProd(@RequestParam(value = "dayCount", defaultValue = "30") Integer dayCount,
                                                                                       @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        List<OrderRefundStatisticsVO> orderRefundStatisticsVOList = orderStatisticsService.listShopRankingByRefundCount(DateUtil.endOfDay(DateUtil.date()), dayCount, limit);
        return ServerResponseEntity.success(orderRefundStatisticsVOList);
    }

}
