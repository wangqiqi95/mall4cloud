package com.mall4j.cloud.api.order.feign;

import com.mall4j.cloud.api.order.vo.OrderOverviewApiVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Description 订单交易分析
 * @Author axin
 * @Date 2022-09-20 11:37
 **/
@FeignClient(value = "mall4cloud-order",contextId = "order-statistics")
public interface OrderStatisticsFeignClient {

    /**
     * 获取当天与昨天订单实时统计数据
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/order/statistics/get_detail_by_hour")
    ServerResponseEntity<OrderOverviewApiVO> getDetailInfoByHour();

    /**
     * 根据支付金额生成店铺排行
     * @param dayCount 距离结束时间的天数
     * @param limit 排行榜取几条数据
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/order/statistics/list_shop_ranking_by_pay_actual")
    ServerResponseEntity<List<OrderOverviewApiVO>> listShopRankingByPayActual(@RequestParam(value = "dayCount", defaultValue = "30") Integer dayCount,
                                                                              @RequestParam(value = "limit", defaultValue = "10") Integer limit);

}
