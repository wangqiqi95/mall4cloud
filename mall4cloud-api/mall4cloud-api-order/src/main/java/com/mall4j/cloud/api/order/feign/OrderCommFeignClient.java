package com.mall4j.cloud.api.order.feign;

import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.order.vo.OrderItemLangVO;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/11/23
 */
@FeignClient(value = "mall4cloud-order",contextId = "orderComm")
public interface OrderCommFeignClient {

    /**
     * 根据orderItemId，获取评论地订单项对应的spuId
     *
     * @param orderItemId 订单项id列表
     * @return spuId
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/canComm")
    ServerResponseEntity<Long> getSpuIdByOrderItemId(@RequestParam("orderItemId") Long orderItemId);

    /**
     * 更新订单项评论状态
     *
     * @param orderItemId 订单项id列表
     * @return spuId
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/canComm")
    void updateOrderItemComm(@RequestBody Long orderItemId);

    /**
     * 根据订单项id获取订单项信息
     * @param orderItemId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getByOrderItemId")
    ServerResponseEntity<OrderItemVO> getOrderItemByOrderItemId(@RequestParam("orderItemId") Long orderItemId);

    /**
     * 根据订单项id获取订单项详情
     * @param orderItemId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getOrderItemVOByOrderItemId")
    ServerResponseEntity<OrderItemVO> getByOrderItemId(@RequestParam("orderItemId") Long orderItemId);

    /**
     * 根据订单项id列表获取订单项国际化信息列表
     * @param orderItemIds
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/listOrderItemLangByIds")
    ServerResponseEntity<List<OrderItemLangVO>> listOrderItemLangByIds(@RequestBody  List<Long> orderItemIds);


    /**
     * 根据订单项id列表获取订单项列表
     * @param orderItemIds
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/listOrderItemByIds")
    ServerResponseEntity<List<OrderItemVO>> listOrderItemByIds(@RequestBody  List<Long> orderItemIds);
}
