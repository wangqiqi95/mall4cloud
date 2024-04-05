package com.mall4j.cloud.api.feign;

import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.order.vo.EsOrderVO;
import com.mall4j.cloud.common.order.vo.UserOrderStatisticVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 订单搜索
 * @author FrozenWatermelon
 * @date 2021/02/05
 */
@FeignClient(value = "mall4cloud-search",contextId = "searchOrder")
public interface SearchOrderFeignClient {


//    /**
//     * 订单搜索
//     * @param orderSearch 订单搜索参数
//     * @return 订单列表
//     */
//    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/searchOrder/getOrderPage")
//    ServerResponseEntity<EsPageVO<EsOrderVO>> getOrderPage(@RequestBody OrderSearchDTO orderSearch);

    /**
     * 订单搜索
     * @param orderSearch 订单搜索参数
     * @return 订单列表
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/searchOrder/getOrderList")
    ServerResponseEntity<List<EsOrderVO>> getOrderList(@RequestBody OrderSearchDTO orderSearch);

    /**
     * 统计用户的订单相关信息
     * @param userIds 用户id集合
     * @return 用户的订单相关统计数据
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/searchOrder/countOrderByUserIds")
    ServerResponseEntity<List<UserOrderStatisticVO>> countOrderByUserIds(@RequestBody List<Long> userIds);
}
