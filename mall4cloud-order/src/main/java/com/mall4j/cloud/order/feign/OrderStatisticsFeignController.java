package com.mall4j.cloud.order.feign;

import cn.hutool.core.date.DateUtil;
import com.mall4j.cloud.api.order.feign.OrderStatisticsFeignClient;
import com.mall4j.cloud.api.order.vo.OrderOverviewApiVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.order.service.OrderStatisticsService;
import com.mall4j.cloud.order.vo.OrderOverviewVO;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description 订单交易分析
 * @Author axin
 * @Date 2022-09-20 11:48
 **/
@RestController
public class OrderStatisticsFeignController implements OrderStatisticsFeignClient {
    @Autowired
    private OrderStatisticsService orderStatisticsService;
    @Autowired
    private MapperFacade mapperFacade;

    @Override
    public ServerResponseEntity<OrderOverviewApiVO> getDetailInfoByHour() {
        OrderOverviewVO orderOverviewVO = orderStatisticsService.getDetailInfoByHour(null);
        OrderOverviewApiVO orderOverviewApiVO = mapperFacade.map(orderOverviewVO, OrderOverviewApiVO.class);
        return ServerResponseEntity.success(orderOverviewApiVO);
    }

    @Override
    public ServerResponseEntity<List<OrderOverviewApiVO>> listShopRankingByPayActual(Integer dayCount, Integer limit) {
        List<OrderOverviewVO> orderOverviewVOList = orderStatisticsService.listShopRankingByPayActual(DateUtil.endOfDay(DateUtil.date()), dayCount, limit);
        List<OrderOverviewApiVO> orderOverviewApiVOS = mapperFacade.mapAsList(orderOverviewVOList, OrderOverviewApiVO.class);
        return ServerResponseEntity.success(orderOverviewApiVOS);
    }
}
