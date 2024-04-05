package com.mall4j.cloud.api.order.feign;

import com.mall4j.cloud.api.order.dto.InitRefundStatusDTO;
import com.mall4j.cloud.api.order.dto.OrderRefundSaleDTO;
import com.mall4j.cloud.api.order.dto.RefundDeliveryDTO;
import com.mall4j.cloud.api.order.vo.OrderRefundAddrVO;
import com.mall4j.cloud.api.order.vo.OrderRefundProdEffectRespVO;
import com.mall4j.cloud.api.order.vo.OrderRefundSimpleVO;
import com.mall4j.cloud.api.order.vo.OrderRefundVO;
import com.mall4j.cloud.common.feign.FeignInsideAuthConfig;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/11/23
 */
@FeignClient(value = "mall4cloud-order", contextId = "order-refund")
public interface OrderRefundFeignClient {


    /**
     * 获取退款状态
     *
     * @param refundId 退款id
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/getRefundStatus")
    ServerResponseEntity<Integer> getRefundStatus(@RequestParam("refundId") Long refundId);


    /**
     * 更新退款状态
     *
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/initRefundStatus")
    ServerResponseEntity<Integer> initRefundStatus(@RequestBody InitRefundStatusDTO initRefundStatusDTO);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/ec/refund/initRefundStatus")
    ServerResponseEntity<Integer> ecInitRefundStatus(@RequestBody InitRefundStatusDTO initRefundStatusDTO);

    /**
     * 根据参数获取商品退款订单数据分析
     *
     * @param spuIds    商品id列表
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 商品退款订单数据
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/getProdRefundEffectByDateAndProdIds")
    ServerResponseEntity<List<OrderRefundProdEffectRespVO>> getProdRefundEffectByDateAndProdIds(@RequestParam("spuIds") List<Long> spuIds,
                                                                                                @RequestParam("startTime") Long startTime,
                                                                                                @RequestParam("endTime") Long endTime);

    /**
     * 通过订单id获取成功的退款订单
     *
     * @param orderId        订单id
     * @param returnMoneySts 退款状态
     * @return 成功的退款订单
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/getOrderRefundByOrderIdAndRefundStatus")
    ServerResponseEntity<List<OrderRefundVO>> getOrderRefundByOrderIdAndRefundStatus(@RequestParam("orderId") Long orderId, @RequestParam("returnMoneySts") Integer returnMoneySts);

    /**
     * 通过视频号订单编号查询退款单信息
     *
     * @param aftersaleId 视频号售后单id
     * @return 成功的退款订单
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/getOrderRefundByAftersaleId")
    ServerResponseEntity<OrderRefundVO> getOrderRefundByAftersaleId(@RequestParam("aftersaleId") Long aftersaleId);

    /**
     * 视频号售后超时 同意退货
     *
     * @param aftersaleId 视频号售后单id
     * @return 成功的退款订单
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/aftersaleReturnOverdue")
    ServerResponseEntity<Void> aftersaleReturnOverdue(@RequestParam("aftersaleId") Long aftersaleId);

    /**
     * 视频号4.0售后超时 同意退货
     *
     * @param aftersaleId 视频号售后单id
     * @return 成功的退款订单
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/ec/aftersaleReturnOverdue")
    ServerResponseEntity<Void> ecAftersaleReturnOverdue(@RequestParam("aftersaleId") Long aftersaleId);

    /**
     * 根据支付单号列表获取订单号列表
     *
     * @param refundIds
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/settlement/listOrderIdsByRefundIds")
    ServerResponseEntity<List<OrderRefundSimpleVO>> listOrderIdsByRefundIds(@RequestBody List<Long> refundIds);

    /**
     * 通过退款编号id获取退款订单
     *
     * @param refundId 退款id
     * @return 成功的退款订单
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/getOrderRefundByRefundId")
    ServerResponseEntity<OrderRefundVO> getOrderRefundByRefundId(@RequestParam("refundId") Long refundId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/getOrderRefundByRefundNumber")
    ServerResponseEntity<OrderRefundVO> getOrderRefundByRefundNumber(@RequestParam("refundNumber") String refundNumber);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/pushRefund")
    ServerResponseEntity<Void> pushRefund(@RequestParam("refundId") Long refundId,@RequestParam("returnStatus")Integer returnStatus);

    /**
     * 查询退款成功的订单商品数量
     *
     * @param orderId 订单ID
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/countRefundSuccessRefundCountByOrderId")
    ServerResponseEntity<Integer> countRefundSuccessRefundCountByOrderId(@RequestParam("orderId") Long orderId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/countReturnProcessingItemByOrderId")
    ServerResponseEntity<Integer> countReturnProcessingItemByOrderId(@RequestParam("orderId") Long orderId);

    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/applyRefundSale")
    ServerResponseEntity<Long> applyRefundSale(@RequestBody OrderRefundSaleDTO orderRefundSaleDTO);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/getRefundAddr")
    ServerResponseEntity<OrderRefundAddrVO> getRefundAddr(@RequestParam("refundId") Long refundId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/saleCancel")
    ServerResponseEntity<Void> saleCancel(@RequestParam("aftersaleId") Long aftersaleId, @RequestParam("outAftersaleId") String outAftersaleId);

    /**
     * 提交视频号上传的退单物流信息
     *
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/express/submit")
    ServerResponseEntity<Void> submitExpress(@RequestBody RefundDeliveryDTO refundDeliveryDTO);

    /**
     * 视频号- 用户修改退货物流信息
     *
     * @return
     */
    @PostMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/express/modify")
    ServerResponseEntity<Void> modifyExpress(@RequestBody RefundDeliveryDTO refundDeliveryDTO);

    /**
     * 视频号退款成功
     *
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/success")
    ServerResponseEntity<Void> returnSuccess(@RequestParam("aftersaleId") Long aftersaleId, @RequestParam("outAftersaleId") String outAftersaleId);

    /**
     * 纠纷单
     * @param event
     * @param complaintOrderId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/complaintNotify")
    ServerResponseEntity<Void> complaintNotify(@RequestParam("event") int event, @RequestParam("complaintOrderId") Long complaintOrderId);

    /**
     * 4.0纠纷单
     * @param after_sale_order_id
     * @param complaintOrderId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/channelsComplaintNotify")
    ServerResponseEntity<Void> channelsComplaintNotify(@RequestParam("after_sale_order_id") Long after_sale_order_id, @RequestParam("complaintOrderId") Long complaintOrderId);

    /**
     * 视频号退款失败，线下退款处理
     * @param aftersaleId
     * @return
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/offlineRefund")
    ServerResponseEntity<Void> offlineRefund(@RequestParam("aftersaleId") Long aftersaleId);

    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/offlineRefundSuccess")
    ServerResponseEntity<Void> offlineRefundSuccess(@RequestParam("aftersaleId") Long aftersaleId);

    /**
     * 联营分佣修改退单状态
     * @param refundIds
     * @param jointVentureRefundStatus 0待分佣 1分佣中 2分佣完成
     */
    @GetMapping(value = FeignInsideAuthConfig.FEIGN_INSIDE_URL_PREFIX + "/insider/refund/updateJointVentureRefundOrder")
    void updateJointVentureRefundOrder(@RequestParam("refundIds") List<Long> refundIds,
                                       @RequestParam("jointVentureRefundStatus")Integer jointVentureRefundStatus);
}
