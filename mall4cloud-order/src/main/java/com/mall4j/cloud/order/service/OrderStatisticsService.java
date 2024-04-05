package com.mall4j.cloud.order.service;

import cn.hutool.core.date.DateTime;
import com.mall4j.cloud.order.vo.OrderCountVO;
import com.mall4j.cloud.order.vo.OrderOverviewVO;
import com.mall4j.cloud.order.vo.OrderRefundStatisticsVO;

import java.util.Date;
import java.util.List;

/**
 * @author lth
 */
public interface OrderStatisticsService {

    /**
     * 根据店铺id查询不同状态的订单数量
     * @param shopId
     * @return
     */
    OrderCountVO getOrderCountOfStatusByShopId(Long shopId);

    /**
     * 获取指定时间范围的退款订单比率信息及退款金额统计列表
     * @param shopId
     * @param dayCount
     * @param endTime
     * @return
     */
    List<OrderRefundStatisticsVO> listOrderRefundInfoInDateRange(Long shopId, Date endTime, Integer dayCount);

    /**
     * 根据指定时间范围获取根据商品名生成退款排行
     * @param shopId
     * @param endTime
     * @param dayCount
     * @return
     */
    List<OrderRefundStatisticsVO> listRefundRankingByProd(Long shopId, Date endTime, Integer dayCount);

    /**
     * 以小时分段获取当天订单实时统计数据
     * @param shopId
     * @param startTime
     * @param endTime
     * @return
     */
    OrderOverviewVO getToDayInfoByHour(Long shopId, Date startTime, Date endTime);

    /**
     * 根据shopId与时间范围获取当月订单的统计数据
     * @param shopId
     * @param startTime
     * @param endTime
     * @return
     */
    OrderOverviewVO getCurrentMonthByDay(Long shopId, Date startTime, Date endTime);

    /**
     * 根据退款原因生成退款排行
     * @param shopId
     * @param endTime
     * @param dayCount
     * @return
     */
    List<OrderRefundStatisticsVO> listRefundRankingByReason(Long shopId, Date endTime, int dayCount);

    /**
     * 获取当天与昨天订单实时统计数据
     * @param shopId
     * @return
     */
    OrderOverviewVO getDetailInfoByHour(Long shopId);

    /**
     * 根据订单数量生成商品排行
     * @param shopId 店铺ID
     * @param endTime 结束时间
     * @param dayCount 距离结束时间的天数
     * @param limit 排行榜取几条数据
     * @return
     */
    List<OrderOverviewVO> listSpuRankingByOrderCount(Long shopId, Date endTime, int dayCount, int limit);

    /**
     * 根据支付金额生成商品排行
     * @param endTime 结束时间
     * @param dayCount 距离结束时间的天数
     * @param limit 排行榜取几条数据
     * @return
     */
    List<OrderOverviewVO> listShopRankingByPayActual(Date endTime, int dayCount, int limit);

    /**
     * 获取店铺退款订单数量排行榜
     * @param endTime 结束时间
     * @param dayCount 距离结束时间的天数
     * @param limit 排行榜取几条数据
     * @return
     */
    List<OrderRefundStatisticsVO> listShopRankingByRefundCount(Date endTime, Integer dayCount, Integer limit);

    /**
     * 获取近多少天内的订单统计数据
     * @param shopId 店铺id
     * @param dayCount 天数
     * @return
     */
    OrderOverviewVO getOrderInfoByDayCountAndShopId(Long shopId, Integer dayCount);
}
