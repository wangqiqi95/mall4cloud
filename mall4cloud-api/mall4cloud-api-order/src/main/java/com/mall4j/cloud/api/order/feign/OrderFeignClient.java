package com.mall4j.cloud.api.order.feign;

import com.mall4j.cloud.api.distribution.dto.DistributionQueryDTO;
import com.mall4j.cloud.api.order.bo.*;
import com.mall4j.cloud.api.order.dto.*;
import com.mall4j.cloud.api.order.vo.*;
import com.mall4j.cloud.api.user.bo.UserOrderStatisticBO;
import com.mall4j.cloud.api.user.bo.UserOrderStatisticListBO;
import com.mall4j.cloud.api.user.dto.MemberReqDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.order.vo.EsOrderVO;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.order.vo.UserOrderStatisticVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/11/23
 */
@FeignClient(value = "mall4cloud-order",contextId = "order")
public interface OrderFeignClient {


    /**
     * 如果订单没有被取消的话，获取订单金额，否之会获取失败
     * @param orderIds 订单id列表
     * @return 订单金额
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ordgetOrdersAmountAndIfNoCanceler")
    ServerResponseEntity<OrderAmountVO> getOrdersAmountAndIfNoCancel(@RequestParam("orderIds") List<Long> orderIds);

    /**
     * 获取订单状态，如果订单状态不存在，则说明订单没有创建
     *
     * @param orderIds 订单id列表
     * @return 订单状态
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getOrdersStatus")
    ServerResponseEntity<List<OrderStatusBO>> getOrdersStatus(@RequestParam("orderIds") List<Long> orderIds);

    /**
     * 获取订单中的金额信息，不包含退款
     *
     * @param orderIds 订单id列表
     * @return 订单中的金额信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getOrdersSimpleAmountInfo")
    ServerResponseEntity<List<OrderSimpleAmountInfoBO>> getOrdersSimpleAmountInfo(@RequestParam("orderIds") List<Long> orderIds);

    /**
     * 获取订单中的金额信息，包含退款、分销结算金额等信息
     *
     * @param orderIds 订单id列表
     * @return 订单中的金额信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getOrdersShopId")
    ServerResponseEntity<List<OrderAmountInfoBO>> getOrdersAmountInfo(@RequestParam("orderIds") List<Long> orderIds);


    /**
     * 获取订单需要保存到es中的数据
     * @param orderId 订单id
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getEsOrder")
    ServerResponseEntity<EsOrderBO> getEsOrder(@RequestParam("orderId")Long orderId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getEsDistributionOrder")
    ServerResponseEntity<EsOrderBO> getEsDistributionOrder(@RequestParam("orderId")Long orderId);

    /**
     * 获取订单需要保存到es中的数据
     * @param orderNumber 订单编号
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getEsOrderByOrderNumber")
    ServerResponseEntity<EsOrderBO> getEsOrderByOrderNumber(@RequestParam("orderNumber")String orderNumber);

    /**
     * 获取订单需要保存到es中的数据
     * @param wechatOrderId 微信订单编号
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getEsOrderByWechatOrderId")
    ServerResponseEntity<EsOrderBO> getEsOrderByWechatOrderId(@RequestParam("wechatOrderId")Long wechatOrderId);


    /**
     * 提交订单
     * @param mergerOrder 确认订单的订单信息
     * @return 订单号
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/submit")
    ServerResponseEntity<List<Long>> submit(@RequestBody ShopCartOrderMergerVO mergerOrder);

    /**
     * 会员管理部分获取用户相关的订单统计数据
     * @param userIds 用户信息
     * @return 相关用户的订单中的信息
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/countOrderByUserIds")
    ServerResponseEntity<List<UserOrderStatisticVO>> countOrderByUserIds(@RequestBody List<Long> userIds);

    /**
     * 付款用户列表
     * @param orderSearchDTO 条件参数
     * @return 用户id列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getOrderUserIdsBySearchDTO")
    ServerResponseEntity<List<Long>> getOrderUserIdsBySearchDTO(@RequestBody OrderSearchDTO orderSearchDTO);

    /**
     * 获取在一定时间内消费一定次数的会员信息
     * @param isPayed      是否已支付
     * @param deleteStatus 用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除
     * @param startDate    开始时间
     * @param endDate      结束时间
     * @param status       状态
     * @param minNum       最小数量
     * @param maxNum       最大数量
     * @return 会员id
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/listUserIdByPurchaseNum")
    ServerResponseEntity<List<Long>> listUserIdByPurchaseNum(@RequestParam("isPayed") Integer isPayed,
                                                             @RequestParam("deleteStatus") Integer deleteStatus,
                                                             @RequestParam(value = "startDate", required = false) Date startDate,
                                                             @RequestParam(value = "endDate", required = false) Date endDate,
                                                             @RequestParam(value = "status", required = false) Integer status,
                                                             @RequestParam("minNum") Long minNum,
                                                             @RequestParam("maxNum") Long maxNum);

    /**
     * 获取在一定时间内消费一定金额的会员信息
     * @param isPayed      是否已支付
     * @param deleteStatus 用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除
     * @param startDate    开始时间
     * @param endDate      结束时间
     * @param status       状态
     * @param minAmount    最小支付金额
     * @param maxAmount    最大支付金额
     * @return 会员id
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/listUserIdByAverageActualTotal")
    ServerResponseEntity<List<Long>> listUserIdByAverageActualTotal(@RequestParam("isPayed") Integer isPayed,
                                                                    @RequestParam("deleteStatus") Integer deleteStatus,
                                                                    @RequestParam(value = "startDate", required = false) Date startDate,
                                                                    @RequestParam(value = "endDate", required = false) Date endDate,
                                                                    @RequestParam("status") Integer status,
                                                                    @RequestParam("minAmount") Long minAmount,
                                                                    @RequestParam("maxAmount") Long maxAmount);

    /**
     * 根据参数获取数据分析中的订单信息
     * @param spuIds 产品id列表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 商品效果分析数据
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getProdEffectByDateAndProdIds")
    ServerResponseEntity<List<OrderProdEffectRespVO>> getProdEffectByDateAndProdIds(@RequestParam("spuIds")List<Long> spuIds,
                                                                                    @RequestParam("startTime")Long startTime,
                                                                                    @RequestParam("endTime")Long endTime);



    /**
     * 获取用户的成交留存率
     * @param customerRetainedDTO 条件查询参数
     * @return 成交留存信息列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getTradeRetained")
    ServerResponseEntity<List<CustomerRetainVO>> getTradeRetained(@RequestBody CustomerRetainedDTO customerRetainedDTO);

    /**
     * 根据订单id列表， 获取订单流量信息列表
     * @param orderIds 订单id列表
     * @return 订单流量信息列表
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/listOrderByOrderId")
    ServerResponseEntity<List<FlowOrderVO>> listFlowOrderByOrderIds(@RequestBody Collection<Long> orderIds);

    /**
     * 会员支付信息
     *
     * @param param 筛选参数
     * @return 会员支付信息
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getPaidMemberByParam")
    ServerResponseEntity<List<UserOrderStatisticBO>> getPaidMemberByParam(@RequestBody MemberReqDTO param);

    /**
     * 获取新老会员成交数据
     *
     * @param param 筛选参数
     * @return 新老会员成交数据
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getMemberPayData")
    ServerResponseEntity<UserOrderStatisticListBO> getMemberPayData(@RequestBody MemberReqDTO param);

    /**
     * 刷新交易留存缓存
     * @param customerRetainedDTO 条件查询
     * @return
     */
    @PutMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/removeCacheTradeRetained")
    ServerResponseEntity<Void> removeCacheTradeRetained(CustomerRetainedDTO customerRetainedDTO);

    /**
     * 批量更新订单明细的获得积分
     * @param orderItemVOList 订单明细
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/updateOrderItemBatchById")
    ServerResponseEntity<Void> updateOrderItemBatchById(@RequestBody List<OrderItemVO> orderItemVOList);

    /**
     * 获取es订单数据列表
     *
     * @param orderSearchDTO 开始时间
     * @return 订单列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/pageEsOrder")
    ServerResponseEntity<List<EsOrderBO>> pageEsOrder(@RequestBody OrderSearchDTO orderSearchDTO);
    /**
     * 获取指定时间端内的订单数量
     *
     * @param orderSearchDTO
     * @return 订单列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/countOrderNum")
    ServerResponseEntity<Long> countOrderNum(@RequestBody OrderSearchDTO orderSearchDTO);

    /**
     * 获取最初的订单创建时间
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/minOrderCreateTime")
    ServerResponseEntity<Date> minOrderCreateTime();

    /**
     * 获取订单详情
     * @param orderId 订单id
     * @return 订单详情
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getOrderDetailInfo")
    ServerResponseEntity<SendNotifyBO> getOrderDetailInfo(@RequestParam("orderIds") Long orderId);

    /**
     * 获取正常状态的订单数量
     * @param orderIds 订单ids
     * @return 订单详情
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/countNormalOrderByOrderIds")
    ServerResponseEntity<Integer> countNormalOrderByOrderIds(@RequestParam("orderIds") List<Long> orderIds);


    /**
     * 根据用户id列表获取积累消费金额和积累消费笔数
     * @param userIds 用户id列表
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/listSumDataByUserIds")
    ServerResponseEntity<List<SumAmountVO>> listSumDataByUserIds(@RequestBody List<Long> userIds);

    /**
     * 获取用户购买过的商品数量
     * @param spuId 商品id
     * @param userId 分销员userId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/hasBuySuccessProd")
    ServerResponseEntity<Long> hasBuySuccessProd(@RequestParam("spuId") Long spuId, @RequestParam("userId") Long userId);

    /**
     * 统计用户购物数据
     * @param userId 用户id
     * @return 用户购物数据
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/calculateUserInShopData")
    ServerResponseEntity<UserShoppingDataVO> calculateUserInShopData(@RequestParam("userId")Long userId);

    /**
     * 获取订单项数据列表
     *
     * @param orderItemIds 开始时间
     * @return 订单列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getOrderItems")
    ServerResponseEntity<List<OrderItemVO>> getOrderItems(@RequestBody List<Long> orderItemIds);

    /**
     * 获取订单项数据
     *
     * @param orderItemId
     * @return 订单列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getOrderItem")
    ServerResponseEntity<OrderItemVO> getOrderItem(@RequestBody Long orderItemId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getOrderItemsByOrderIds")
    ServerResponseEntity<List<OrderItemVO>> getOrderItemsByOrderIds(@RequestBody List<Long> orderIds);

    /**
     * 根据多个订单号获取订单集合
     *
     * @param orderIds 订单号集合
     * @return 订单列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getEsOrderList")
    ServerResponseEntity<List<EsOrderBO>> getEsOrderList(@RequestBody List<Long> orderIds);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/userConsumeAmount")
    ServerResponseEntity<Long> userConsumeAmount(@RequestParam("userId") Long userId, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime, @RequestParam("orderType") String orderType);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/zhlsApiUtilAddOrder")
    ServerResponseEntity<Void> zhlsApiUtilAddOrder(@RequestBody List<Long> orderIds);

    /**
     * 计算订单项当中含有的分销金额
     * @param orderItems 订单项列表
     * @return 分销金额
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/sumTotalDistributionAmountByOrderItem")
    ServerResponseEntity<Long> sumTotalDistributionAmountByOrderItem(@RequestBody List<EsOrderItemBO> orderItems);


    /**
     * 获取已结算的订单
     * @param settled 是否已经进行结算
     * @param orderIds 订单号集合
     * @return 已结算的订单
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getListBySettledOrOrderIds")
    ServerResponseEntity<List<EsOrderBO>> getListBySettledOrOrderIds(@RequestParam("settled")Integer settled, @RequestParam("orderIds") List<Long> orderIds);

    /**
     * 获取指定的订单项
     * @param orderIds 订单号集合
     * @return 订单项列表
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/listOrderItemByOrderIds")
    ServerResponseEntity<List<OrderItemVO>> listOrderItemByOrderIds(@RequestParam("orderIds") List<Long> orderIds);

    /**
     * 批量更新订单明细的获得佣金
     * @param orderItemVOList 订单明细
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/updateOrderItemCommissionBatchById")
    ServerResponseEntity<Boolean> updateOrderItemCommissionBatchById(@RequestBody List<OrderItemVO> orderItemVOList);

    /**
     * 批量修改订单分销佣金状态
     * @param orderIds 订单ID集合
     * @param distributionStatus 分销佣金状态 0待结算 1已结算 2已提现 3提现中
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/updateDistributionStatusBatchById")
    ServerResponseEntity<Void> updateDistributionStatusBatchById(@RequestParam("orderIds") List<Long> orderIds, @RequestParam("distributionStatus") Integer distributionStatus, @RequestParam(value = "distributionSettleTime", required = false) Date distributionSettleTime, @RequestParam(value = "distributionWithdrawTime", required = false) Date distributionWithdrawTime);

    /**
     * 批量修改订单发展佣金状态
     *
     * @param orderIds         订单ID集合
     * @param developingStatus 发展佣金状态 0待结算 1已结算 2已提现 3提现中
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/updateDevelopingStatusBatchById")
    ServerResponseEntity<Void> updateDevelopingStatusBatchById(@RequestParam("orderIds") List<Long> orderIds, @RequestParam("developingStatus") Integer developingStatus, @RequestParam(value = "developingSettleTime", required = false) Date developingSettleTime, @RequestParam(value = "developingWithdrawTime", required = false) Date developingWithdrawTime);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/updateDistributionRefundStatusBatchByOrderItemId")
    ServerResponseEntity<Void> updateDistributionRefundStatusBatchByOrderItemId(@RequestParam("orderItemIds") List<Long> distributionOrderItemIds, @RequestParam("distributionRefundStatus") Integer distributionRefundStatus);

    /**
     * 根据订单id获取订单项数量
     *
     * @param orderId 订单ID
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/countItemByOrderId")
    ServerResponseEntity<Integer> countItemByOrderId(@RequestParam("orderId") Long orderId);


    /**
     * 获取导购已结算的订单
     * @param staffId 导购ID
     * @param endTime 截至时间
     * @return 已结算的订单
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/listByStaffAndTime")
    ServerResponseEntity<List<EsOrderBO>> listByStaffAndTime(@RequestParam("staffId") Long staffId, @RequestParam("endTime") Date endTime);


    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/listDistributionOrders")
    ServerResponseEntity<List<EsOrderBO>> listDistributionOrders(@RequestBody DistributionQueryDTO queryDTO);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/listDistributionJointVentureOrders")
    ServerResponseEntity<List<EsOrderBO>> listDistributionJointVentureOrders(@RequestBody DistributionJointVentureOrderSearchDTO searchDTO);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/listDistributionJointVentureOrdersV2")
    ServerResponseEntity<List<EsOrderBO>> listDistributionJointVentureOrdersV2(@RequestBody DistributionJointVentureOrderSearchDTO searchDTO);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/listDistributionJointVentureRefundOrders")
    ServerResponseEntity<List<DistributionJointVentureOrderRefundRespDTO>> listDistributionJointVentureRefundOrders(@RequestBody DistributionJointVentureOrderRefundSearchDTO searchDTO);


    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/countOrderAmountByUserId")
    ServerResponseEntity<Long> countOrderAmountByUserId(@RequestParam("userId") Long userId,
                                                        @RequestParam(value = "startTime", required = false) Date startTime,
                                                        @RequestParam(value = "endTime", required = false) Date endTime);

    /**
     * 根据订单号获取订单项(包含sku、spu名称)
     *
     * @param orderId 订单id
     * @return 订单项
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/listOrderItemAndLangByOrderId")
    ServerResponseEntity<List<OrderItemVO>> listOrderItemAndLangByOrderId(@RequestParam("orderId") Long orderId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/std/detail")
    ServerResponseEntity<OrderShopVO> stdDetail(@RequestParam("orderId") Long orderId);

    /**
     * 提交订单
     * @param confirmDTO 确认订单的订单信息
     * @return 订单号er
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/confirm")
    ServerResponseEntity<ShopCartOrderMergerVO> confirm(@RequestBody OrderConfirmDTO confirmDTO) throws Exception;

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/std/cancelOrder")
    ServerResponseEntity<Void> stdCancelOrder(@RequestParam("orderIds") List<Long> orderIds);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/std/order/stdOrderConfirm")
    ServerResponseEntity<Void> stdOrderConfirm(@RequestParam("orderId") Long orderId);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/jointVentureCommissionOrder/settled")
    ServerResponseEntity<Void> jointVentureCommissionOrderSettled(@RequestBody JointVentureCommissionOrderSettledDTO jointVentureCommissionOrderSettledDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/std/order/getOrderItemByOrderNumberAndSkuId")
    ServerResponseEntity<OrderItemVO> getOrderItemByOrderNumberAndSkuId(@RequestParam("orderId") String orderNumber, @RequestParam("skuId") Long skuId);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/livestore/order/syncWeichatPromotionInfo")
    ServerResponseEntity<Void> syncWeichatPromotionInfo(@RequestBody SyncWeichatPromotionInfoDTO syncWeichatPromotionInfoDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/calculateDistributionCommissionByUserId")
    ServerResponseEntity<CalculateDistributionCommissionResultVO> calculateDistributionCommissionByUserId(@RequestParam("userId") Long userId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/getMatchedOrderIdList")
    ServerResponseEntity<List<Long>> getMatchedOrderIdList(@RequestBody OrderSearchDTO orderSearchDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/checkIsOrderIntoShops")
    ServerResponseEntity<Integer> checkIsOrderIntoShops(@RequestBody CheckOrderDTO checkOrderDTO);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/createEcOrder")
    ServerResponseEntity<Integer> createEcOrder(@RequestBody ShopCartOrderMergerVO shopCartOrderMergerVO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ecOrderPay")
    ServerResponseEntity<Void> ecOrderPay(@RequestParam("outOrderId") Long outOrderId,@RequestParam("tentacleNo") String tentacleNo);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ecCancelOrder")
    ServerResponseEntity<Void> ecCancelOrder(@RequestBody Long outOrderId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/std/order/ecOrderConfirm")
    ServerResponseEntity<Void> ecOrderConfirm(@RequestParam("outOrderId") Long outOrderId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/page/support")
    ServerResponseEntity<PageVO<EsOrderVO>> supportPage(@RequestBody OrderSearchDTO orderSearchDTO);
}
