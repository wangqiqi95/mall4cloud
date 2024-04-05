package com.mall4j.cloud.order.service;

import com.mall4j.cloud.api.biz.dto.channels.request.EcuploadrefundcertificateRequest;
import com.mall4j.cloud.api.biz.dto.livestore.request.UploadCertificatesRequest;
import com.mall4j.cloud.api.order.vo.OrderRefundAddrVO;
import com.mall4j.cloud.api.order.vo.OrderRefundProdEffectRespVO;
import com.mall4j.cloud.api.order.vo.OrderRefundSimpleVO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.order.bo.RefundNotifyBO;
import com.mall4j.cloud.order.dto.app.DistributionSalesStatDTO;
import com.mall4j.cloud.order.dto.app.OrderRefundPageDTO;
import com.mall4j.cloud.order.dto.multishop.OrderRefundDTO;
import com.mall4j.cloud.order.model.Order;
import com.mall4j.cloud.order.model.OrderRefund;
import com.mall4j.cloud.order.model.OrderRefundAddr;
import com.mall4j.cloud.order.vo.OrderRefundVO;

import java.util.Date;
import java.util.List;

/**
 * 订单退款记录信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public interface OrderRefundService {

    /**
     * 分页获取订单退款记录信息列表
     *
     * @param pageDTO            分页参数
     * @param orderRefundPageDTO
     * @return 订单退款记录信息列表分页数据
     */
    PageVO<OrderRefundVO> page(PageDTO pageDTO, OrderRefundPageDTO orderRefundPageDTO);

    PageVO<OrderRefundVO> excelPage(PageDTO pageDTO, OrderRefundPageDTO orderRefundPageDTO);

    /**
     * 根据订单退款记录信息id获取订单退款记录信息
     *
     * @param refundId 订单退款记录信息id
     * @return 订单退款记录信息
     */
    OrderRefundVO getByRefundId(Long refundId);

    OrderRefundVO getByRefundNumber(String refundNumber);

    OrderRefundVO getByAftersaleId(Long AftersaleId);

    List<OrderRefundVO> getByAftersaleIds(List<Long> AftersaleId);

    Integer getCountByOrderItemId(Long orderItemId);

    /**
     * 根据订单退款记录信息id获取订单退款记录信息 详情，包括订单项，订单信息等
     *
     * @param refundId 订单退款记录信息id
     * @return 订单退款记录信息
     */
    OrderRefundVO getDetailByRefundId(Long refundId);

    OrderRefundVO getDetailByRefundNumber(String refundId);

    /**
     * 保存订单退款记录信息
     *
     * @param orderRefund 订单退款记录信息
     */
    void save(OrderRefund orderRefund);

    /**
     * 更新订单退款记录信息
     *
     * @param orderRefund 订单退款记录信息
     */
    void update(OrderRefund orderRefund);

    /**
     * 根据订单退款记录信息id删除订单退款记录信息
     *
     * @param refundId
     */
    void deleteById(Long refundId);

    /**
     * 校验订单是否在退款时间内，是否可以进行申请退款
     *
     * @param order 订单信息
     * @return
     */
    boolean checkRefundDate(Order order);

    /**
     * 根据订单id，获取正在退款中或退款成功的订单
     *
     * @param orderId
     * @return
     */
    List<OrderRefund> getProcessOrderRefundByOrderId(Long orderId);

    /**
     * 生成退款单
     *
     * @param orderRefund 退款对象
     */
    void applyRefund(OrderRefund orderRefund);

    int syncAftersaleId(Long refundId,Long AftersaleId);

    /**
     * 买家提交物流信息
     *
     * @param orderRefund     退款单对象
     * @param orderRefundAddr 退款物流单对象
     */
    void submitExpress(OrderRefund orderRefund, OrderRefundAddr orderRefundAddr);

    /**
     * 获取订单正在进行退款的退款数量
     *
     * @param returnMoneySts
     * @param returnMoneySts2
     * @param orderId
     * @return
     */
    Integer countByReturnMoneyStsAndOrderId(Integer returnMoneySts, Integer returnMoneySts2, Long orderId);

    /**
     * 订单退款成功处理
     *
     * @param refundNotifyBO
     */
    void refundSuccess(RefundNotifyBO refundNotifyBO);

    /**
     * 用户取消退款
     *
     * @param orderRefundVO
     */
    void cancelRefund(OrderRefundVO orderRefundVO);

    /**
     * 商家拒绝退款
     *
     * @param orderRefundParam
     * @param orderRefundVo
     */
    void disagreeRefund(OrderRefundDTO orderRefundParam, OrderRefundVO orderRefundVo);

    /**
     * 商家同意退货，并返回一个退货地址
     *
     * @param orderRefundParam
     * @param orderRefundVo
     */
    void agreeReturns(OrderRefundDTO orderRefundParam, OrderRefundVO orderRefundVo);

    /**
     * 处理退运费
     * @param orderRefundParam
     * @param orderRefundVo
     */
    void returnFreightAmount(OrderRefundDTO orderRefundParam, OrderRefundVO orderRefundVo);



    /**
     * 同意退款
     *
     * @param orderRefundVO
     * @param orderRefundParam
     * @return 是否成功
     */
    int agreeRefund(OrderRefundVO orderRefundVO, OrderRefundDTO orderRefundParam);

    /**
     * 获取退款状态
     *
     * @param refundId
     * @return
     */
    Integer getRefundStatus(Long refundId);

    int initRefundStatus(Long refundId,Integer status,String buyDesc,String imgs);

    int ecInitRefundStatus(Long refundId,Integer status,Integer applyType,Long refundAmount);

    /**
     * 创建未曾团而退款的团购订单
     *
     * @param refundNotifyBO 退款
     */
    void createGroupUnSuccessRefundInfo(RefundNotifyBO refundNotifyBO);

    /**
     * 通过订单id获取正在处理中的退款订单
     *
     * @param orderId 订单id
     * @return 正在处理中的退款订单
     */
    List<OrderRefundVO> getProcessingOrderRefundByOrderId(Long orderId);

    /**
     * 根据参数获取商品退款订单数据分析
     *
     * @param spuIds    商品id列表
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 商品退款订单数据
     */
    List<OrderRefundProdEffectRespVO> getProdRefundEffectByDateAndProdIds(List<Long> spuIds, Date startTime, Date endTime);

    /**
     * 根据订单id及退款状态，获取订单退款信息
     *
     * @param orderId        订单id
     * @param returnMoneySts 退款状态
     * @return 订单退款数据列表
     */
    List<com.mall4j.cloud.api.order.vo.OrderRefundVO> getOrderRefundByOrderIdAndRefundStatus(Long orderId, Integer returnMoneySts);

    com.mall4j.cloud.api.order.vo.OrderRefundVO getOrderRefundByAftersaleId(Long aftersaleId);

    void aftersaleReturnOverdue(Long aftersaleId);

    /**
     * 根据退款单号列表获取订单号列表
     *
     * @param refundIds
     * @return
     */
    List<OrderRefundSimpleVO> listOrderIdsByRefundIds(List<Long> refundIds);

    /**
     * 获取退款超时的订单
     *
     * @param date 超时时间
     * @return 退款订单
     */
    List<OrderRefundVO> listOrderRefundTimeOut(Date date);

    /**
     * 处理的超时申请的退款订单
     *
     * @param orderRefundList 超时退款订单
     */
    void cancelWhenTimeOut(List<OrderRefundVO> orderRefundList);


    /**
     * 退单推送处理
     * @param refundId 退款单号
     * @param returnStatus 退款状态，枚举申请退款 等待卖家同意（1）卖家同意退款/退货(2)买家已发货 等待卖家收货(3)退款完成（4）已拒绝（5）关闭（6）
     */
    void pushRefund(Long refundId, Integer returnStatus);

    /**
     * 获取临近超时的订单
     *
     * @param date     临近超时时间
     * @param overDate 超时时间
     * @return 退款订单
     */
    List<OrderRefundVO> listProximityRefundTimeOut(Date date, Date overDate);


    /**
     * 退款信息
     *
     * @param refundId 退款编号
     * @return 获取退款信息
     */
    com.mall4j.cloud.api.order.vo.OrderRefundVO getOrderRefundByRefundId(Long refundId);

    com.mall4j.cloud.api.order.vo.OrderRefundVO getOrderRefundByRefundNumber(String refundNumber);

    /**
     * 查询退款成功的订单商品数量
     *
     * @param orderId 订单ID
     * @return
     */
    Integer countRefundSuccessRefundCountByOrderId(Long orderId);

    /**
     * 正在进行退款的退款订单数量
     *
     * @param orderId 订单号
     * @return 正在进行退款的退款订单数量
     */
    Integer countReturnProcessingItemByOrderId(Long orderId);

    /**
     * 查询分销订单退单金额
     */
    Long countDistributionRefundAmount(DistributionSalesStatDTO distributionSalesStatDTO);

    /**
     * 查询分销订单退单数量
     */
    Integer countDistributionRefundNum(DistributionSalesStatDTO distributionSalesStatDTO);


    /**
     * 查询门店分销订单退单金额
     */
    Long countStoreDistributionRefundAmount(DistributionSalesStatDTO distributionSalesStatDTO);


    /**
     * 查询门店分销订单退单数量
     */
    Integer countStoreDistributionRefundNum(DistributionSalesStatDTO distributionSalesStatDTO);

    /**
     * 修改订单备注
     * @param orderId
     * @param remark
     */
    void editPlatformRemark(Long refundId,String remark);

    /**获取退单用户上传物流信息
     * @param refundId
     * @return
     */
    OrderRefundAddrVO getRefundAddr(Long refundId);

    /**
     * 更新纠纷单id到售后单中
     * @param complaintOrderId
     * @param aftersaleId
     */
    void syncComplaintOrderId(Long complaintOrderId,Long aftersaleId);

    /**
     * 更新纠纷单
     * @param complaintOrderId
     */
    void syncComplaintOrderStatus(Long complaintOrderId);

    /**
     * 修改售后单为线下退款
     * @param aftersaleId
     * @return
     */
    int offlineRefund(Long aftersaleId);

    /**
     * 售后单线下退款成功
     * @param aftersaleId
     */
    int offlineRefundSuccess(Long aftersaleId);

    /**
     * 售后单 线下退款上传退款凭证
     * @param uploadCertificatesRequest
     */
    void uploadCertificates(UploadCertificatesRequest uploadCertificatesRequest);

    /**
     * 视频号4.0 上传退款凭证
     * @param request
     */
    void uploadrefundcertificate(EcuploadrefundcertificateRequest request);

    /**
     * 视频号4.0 超时自动同意退货
     * @param aftersaleId
     */
    void ecAftersaleReturnOverdue(Long aftersaleId);

    /**
     * 修改联营分佣结算状态
     * @param refundIds
     * @param jointVentureRefundStatus
     */
    void updateJointVentureRefundOrder(List<Long> refundIds, Integer jointVentureRefundStatus);
}
