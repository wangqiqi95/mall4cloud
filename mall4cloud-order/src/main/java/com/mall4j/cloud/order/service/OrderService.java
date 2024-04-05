package com.mall4j.cloud.order.service;

import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.delivery.dto.DeliveryOrderDTO;
import com.mall4j.cloud.api.distribution.dto.DistributionQueryDTO;
import com.mall4j.cloud.api.order.bo.*;
import com.mall4j.cloud.api.order.dto.*;
import com.mall4j.cloud.api.order.vo.*;
import com.mall4j.cloud.api.user.dto.DistributionUserQueryDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.common.order.bo.PayNotifyBO;
import com.mall4j.cloud.common.order.vo.EsOrderVO;
import com.mall4j.cloud.common.order.vo.ShopCartOrderMergerVO;
import com.mall4j.cloud.common.order.vo.UserOrderStatisticVO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.order.bo.SubmitOrderPayAmountInfoBO;
import com.mall4j.cloud.order.dto.app.DistributionOrderSearchDTO;
import com.mall4j.cloud.order.dto.app.DistributionRankingDTO;
import com.mall4j.cloud.order.dto.app.DistributionSalesStatDTO;
import com.mall4j.cloud.order.dto.multishop.OrderAdminDTO;
import com.mall4j.cloud.order.dto.platform.request.SyncSharerInfoRequest;
import com.mall4j.cloud.order.model.Order;
import com.mall4j.cloud.order.model.OrderItem;
import com.mall4j.cloud.order.vo.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 订单信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public interface OrderService {

	/**
	 * 根据订单信息id获取订单信息
	 *
	 * @param orderId 订单信息id
	 * @return 订单信息
	 */
	Order getByOrderId(Long orderId);
	Order getByOrderNumber(String orderNumber);

	/**
	 * 更新订单信息
	 * @param order 订单信息
	 */
	void update(Order order);

	/**
	 * 根据订单信息id删除订单信息
	 * @param orderId
	 */
	void deleteById(Long orderId);


	/**
	 * 提交订单
	 * @param mergerOrder
	 * @return
	 */
    List<Long> submit(ShopCartOrderMergerVO mergerOrder);

	/**
	 * 查询订单状态
	 * @param orderIds 多个订单的订单id
	 * @return 订单状态列表
	 */
	List<OrderStatusBO> getOrdersStatus(List<Long> orderIds);

	/**
	 * 计算订单实际金额
	 * @param orderIds 多个订单的订单id
	 * @return 订单实际金额总和
	 */
	OrderAmountVO getOrdersActualAmount(List<Long> orderIds);

	/**
	 * 查询订单是否已经支付
	 * @param orderId 订单编号
	 * @param userId 用户id
	 * @return 是否已支付
	 */
    boolean isPay(Long orderId, Long userId);

	/**
	 * 将订单改为已支付状态
     * @param message 支付成功的订单信息
     * @param ordersStatus
     */
    void updateByToPaySuccess(PayNotifyBO message, List<OrderStatusBO> ordersStatus);

	/**
	 * 获取订单中的金额信息，不包含退款
	 * @param orderIds 多个订单的订单id
	 * @return 订单商家id列表
	 */
	List<OrderSimpleAmountInfoBO> getOrdersSimpleAmountInfo(List<Long> orderIds);

	/**
	 * 取消订单
	 * @param orderId 订单id
	 */
	void cancelOrderAndGetCancelOrderIds(List<Long> orderId);

	/**
	 * 根据订单号和用户id获取订单
	 * @param orderId orderId
	 * @param userId userId
	 * @return Order
	 */
	Order getOrderByOrderIdAndUserId(Long orderId, Long userId);

	/**
	 * 根据订单号和店铺id获取订单
	 * @param orderId orderId
	 * @return Order
	 */
	OrderVO getOrderByOrderId(Long orderId);
	/**
	 * 确认收货订单和mq日志要同时落地，所以要事务消息
	 * 这里的确认收货，条件加上订单的状态，确保这次更新是幂等的
	 * @param orderId 订单号
	 * @return 是否成功
	 */
	int receiptOrder(Long orderId);

	/**
	 * 获取订单中的金额信息，包含退款、分销结算金额等信息
	 *
	 * @param orderIds 订单id列表
	 * @return 订单中的金额信息
	 */
	List<OrderAmountInfoBO> getOrdersAmountInfo(List<Long> orderIds);

	/**
	 * 根据订单号删除订单
	 * @param orderId 订单号
	 */
	void deleteOrder(Long orderId);

	/**
	 * 订单物流发货
	 * @param deliveryOrderParam 发货参数
	 */
    void delivery(DeliveryOrderDTO deliveryOrderParam);

	/**
	 * 改变订单金额
	 * @param orderAdminDTO
	 */
	void changeAmount(OrderAdminDTO orderAdminDTO);

	/**
	 * 根据订单id列表获取订单金额信息
	 * @param orderIdList 订单id列表
	 * @return
	 */
    SubmitOrderPayAmountInfoBO getSubmitOrderPayAmountInfo(long[] orderIdList);

	/**
	 * 获取订单需要保存到es中的数据
	 * @param orderId 订单id
	 * @return
	 */
    EsOrderBO getEsOrder(Long orderId);

	EsOrderBO getEsDistributionOrder(Long orderId);

	EsOrderBO getEsOrderByOrderNumber(String orderNumber);

	EsOrderBO getEsOrderByWeChatOrderId(Long weChatOrderId);

	List<EsOrderBO> getEsOrderByWeChatOrderIds(List<Long> weChatOrderIds);

	List<EsOrderBO> getEsOrderByOrderIds(List<Long> orderIds);

	/**
	 * 计算订单项的总分销金额
	 * @param orderItems
	 * @return
	 */
	Long sumTotalDistributionAmountByOrderItem(List<OrderItem> orderItems);

	/**
	 * 获取订单和订单项信息
	 * @param orderId 订单id
	 * @param shopId  店铺id
	 * @return
	 */
	Order getOrderAndOrderItemData(Long orderId,Long shopId);

	/**
	 * 计算每个订单状态的状态数量
	 * @param userId 用户id
	 * @return
	 */
	OrderCountVO countNumberOfStatus(Long userId);

	/**
	 * 订单管理分页列表
	 *
	 * @param orderSearchDTO
	 * @return
	 */
	PageVO<EsOrderVO> orderPage(OrderSearchDTO orderSearchDTO);

	PageVO<EsOrderVO> distributionJointVentureOrderPage(DistributionJointVentureOrderSearchDTO searchDTO);

	DistributionJointVentureOrderPreApplyInfoVO countDistributionJointVentureOrderPreApplyInfo(DistributionJointVentureOrderSearchDTO searchDTO);

	/**
	 * 获取订单的创建状态，给秒杀提供查询是否已经创建订单成功的功能
	 *
	 * @param orderId 订单id
	 * @return
	 */
	int countByOrderId(Long orderId);

	/**
	 * 统计用户相关的订单信息
	 * @param userIds
	 * @return
	 */
	List<UserOrderStatisticVO> countOrderByUserIds(List<Long> userIds);

	/**
	 * 分页获取某个用户的订单数据
	 * @param pageDTO
	 * @param userId
	 * @return
	 */
    PageVO<OrderVO> pageByUserId(PageDTO pageDTO, String userId);

	/**
	 * 获取订单部分数据用户推送消息通知
	 * @param orderIds 订单id列表
	 * @return 订单数量集合
	 */
	List<SendNotifyBO> listByOrderIds(List<Long> orderIds);
	/**
	 * 付款用户列表
	 * @param orderSearchDTO 条件参数
	 * @return 用户id列表
	 */
    List<Long> getOrderUserIdsBySearchDTO(OrderSearchDTO orderSearchDTO);

	/**
	 * 获取在一定时间内消费一定次数的会员信息
	 *
	 * @param isPayed      是否已支付
	 * @param deleteStatus 用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除
	 * @param startDate    开始时间
	 * @param endDate      结束时间
	 * @param status       状态
	 * @param minNum       最小数量
	 * @param maxNum       最大数量
	 * @return 会员id
	 */
    List<Long> listUserIdByPurchaseNum(Integer isPayed, Integer deleteStatus, Date startDate, Date endDate, Integer status, Long minNum, Long maxNum);

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
	List<Long> listUserIdByAverageActualTotal(Integer isPayed, Integer deleteStatus, Date startDate, Date endDate, Integer status, Long minAmount, Long maxAmount);

	/**
	 * 根据参数获取数据分析中的订单信息
	 * @param spuIds 产品id列表
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 商品分析数据
	 */
	List<OrderProdEffectRespVO> getProdEffectByDateAndProdIds(List<Long> spuIds, Date startTime, Date endTime);

	/**
	 * 根据订单id列表， 获取订单流量信息列表
	 * @param orderIds 订单id列表
	 * @return 订单流量信息列表
	 */
    List<FlowOrderVO> listFlowOrderByOrderIds(Collection<Long> orderIds);


	/**
	 * 获取订单id，根据订单订单状态和时间
	 * @param orderStatus 订单状态
	 * @param startTime 开始时间
	 * @return 订单和订单项
	 */
	List<Long> listOrderId(Integer orderStatus, Date startTime);

	List<Long> confirmOrderListOrderId(Integer orderStatus, Date startTime);

	/**
	 * 获取订单id，根据订单订单状态和时间
	 * @param orderStatus 订单状态
	 * @param startTime 开始时间
	 * @return 订单和订单项
	 */
	List<Long> listDistributionOrderId(Integer orderStatus, Date startTime);

	/**
	 * 将订单标记为已结算状态
	 * @param orderIds 订单ids
	 * @return 变更数量
	 */
	int settledOrder(List<Long> orderIds);

	/**
	 * 根据结算时间和店铺id搜索订单信息
	 * @param shopId 店铺id
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<OrderVO> listOrderBySettledTimeAndShopId(Long shopId,Date startTime,Date endTime);

	/**
	 * 获取es订单数据列表
	 *
	 * @param orderSearchDTO
	 * @return 订单列表
	 */
    List<EsOrderBO> pageEsOrder(OrderSearchDTO orderSearchDTO);

	/**
	 * 获取指定时间端内的订单数量
	 *
	 * @param orderSearchDTO
	 * @return 订单列表
	 */
	Long countOrderNum(OrderSearchDTO orderSearchDTO);

	/**
	 * 获取最初的订单创建时间
	 * @return
	 */
    Date minOrderCreateTime();

	/**
	 * 通过订单号获取订单信息
	 * @param orderId 订单号
	 * @return 订单相关信息
	 */
	SendNotifyBO getOrderInfoByOrderId(Long orderId);

	/**
	 * 批量修改订单信息
	 * @param orders 订单信息
	 */
    void updateRefundStatusBatchById(List<Order> orders);

	/**
	 * 根据多个订单号获取订单集合
	 * @param orderIds 订单号集合
	 * @return 订单列表
	 */
    List<EsOrderBO> getEsOrderList(List<Long> orderIds);

	void zhlsApiUtilAddOrder(List<Long> orderIds);

	/**
	 * 批量修改订单分销佣金状态
	 * @param orderIds 订单ID集合
	 * @param distributionStatus 分销佣金状态 0待结算 1已结算 2已提现 3提现中
	 */
	void updateDistributionStatusBatchById(List<Long> orderIds, Integer distributionStatus, Date distributionSettleTime, Date distributionWithdrawTime);

	/**
	 * 批量修改订单发展佣金状态
	 * @param orderIds 订单ID集合
	 * @param developingStatus 发展佣金状态 0待结算 1已结算 2已提现 3提现中
	 */
	void updateDevelopingStatusBatchById(List<Long> orderIds, Integer developingStatus, Date developingSettleTime, Date developingWithdrawTime);

	/**
	 * 导购-待结算/已结算订单列表
	 * @param pageDTO
	 * @param distributionOrderSearchDTO
	 * @return
	 */
	List<DistributionOrderVO> listPageWithDistributionStaff(PageDTO pageDTO, DistributionOrderSearchDTO distributionOrderSearchDTO);

	/**
	 * 微客-待结算/已结算订单列表
	 * @param pageDTO
	 * @param distributionOrderSearchDTO
	 * @return
	 */
	List<DistributionOrderVO> listPageWithDistributionUser(PageDTO pageDTO, DistributionOrderSearchDTO distributionOrderSearchDTO);

	/**
	 * 分销订单列表
	 *
	 * @param pageDTO
	 * @param queryDTO
	 * @return
	 */
	PageVO<EsOrderBO> pageDistributionOrders(PageDTO pageDTO, DistributionQueryDTO queryDTO);

	List<EsOrderBO> listDistributionOrders(DistributionQueryDTO queryDTO);

	List<EsOrderBO> listDistributionJointVentureOrders(DistributionJointVentureOrderSearchDTO searchDTO);

	List<EsOrderBO> listDistributionJointVentureOrdersV2(DistributionJointVentureOrderSearchDTO searchDTO);

	List<DistributionJointVentureOrderRefundRespDTO> listDistributionJointVentureRefundOrders(DistributionJointVentureOrderRefundSearchDTO searchDTO);


	boolean buildQueryCondition(DistributionQueryDTO queryDTO);

	/**
	 * 分销业绩统计
	 *
	 * @param distributionSalesStatDTO
	 * @return
	 */
	Long distributionSalesStat(DistributionSalesStatDTO distributionSalesStatDTO);

	/**
	 * 门店分销业绩统计
	 */
	Long storeDistributionSalesStat(DistributionSalesStatDTO distributionSalesStatDTO);

	/**
	 * 统计分销排名
	 */
	Integer countDistributionRanking(DistributionSalesStatDTO distributionSalesStatDTO);

	/**
	 * 统计门店分销排名
	 */
	Integer countDistributionStoreRanking(DistributionSalesStatDTO distributionSalesStatDTO);

	/**
	 * 统计分销订单数量
	 */
	Integer countDistributionOrderNum(DistributionSalesStatDTO distributionSalesStatDTO);

	/**
	 * 统计门店分销订单数量
	 */
	Integer countStoreDistributionOrderNum(DistributionSalesStatDTO distributionSalesStatDTO);

	/**
	 * 统计分销订单会员数量
	 */
	Integer countDistributionOrderUserNum(DistributionSalesStatDTO distributionSalesStatDTO);

	/**
	 * 统计门店分销订单分销员数量
	 */
	Integer countStoreDistributionUserNum(DistributionSalesStatDTO distributionSalesStatDTO);


	Integer countStoreDistributionUserNum(DistributionSalesStatDTO distributionSalesStatDTO, DistributionUserQueryDTO queryDTO);


	/**
	 * 统计门店分销订单下单会员数量
	 */
	Integer countStoreDistributionOrderUserNum(DistributionSalesStatDTO distributionSalesStatDTO);

	Integer countStoreDistributionOrderUserNum(DistributionSalesStatDTO distributionSalesStatDTO, DistributionUserQueryDTO queryDTO);

	/**
	 * 统计门店分销佣金
	 */
	Long countStoreDistributionCommission(DistributionSalesStatDTO distributionSalesStatDTO);

	/**
	 * 统计分销订单会员消费金额
	 */
	Long countStoreDistributionOrderUserAmount(DistributionSalesStatDTO distributionSalesStatDTO);

	Long countStoreDistributionOrderUserAmount(DistributionSalesStatDTO distributionSalesStatDTO, DistributionUserQueryDTO queryDTO);

	/**
	 * 统计分销订单导购数量
	 */
	Integer countDistributionOrderStaffNum(DistributionSalesStatDTO distributionSalesStatDTO);

	/**
	 * 统计分销订单商品数量
	 */
	Integer countDistributionOrderSpuNum(DistributionSalesStatDTO distributionSalesStatDTO);

	/**
	 * 统计分销订单总金额
	 */
	Long countDistributionOrderTotalPrice(DistributionSalesStatDTO distributionSalesStatDTO);

	/**
	 * 统计会员消费金额
	 *
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Long countOrderAmountByUserId(Long userId, Date startTime, Date endTime);

	PageVO<DistributionRankingDTO> pageDistributionRanking(PageDTO pageDTO, DistributionSalesStatDTO distributionSalesStatDTO);

    PageVO<DistributionStoreStatisticsVO> pageStoreStatistics(PageDTO pageDTO, DistributionSalesStatDTO distributionSalesStatDTO);

    List<DistributionStoreStatisticsVO> storeStatistics(DistributionSalesStatDTO distributionSalesStatDTO);

    void exportDistributionOrder(DistributionQueryDTO queryDTO, HttpServletResponse response);

	OrderShopVO stdDetailByOrderId(Long orderId);

	Long countDistributionCommission(DistributionSalesStatDTO distributionSalesStatDTO);

	/**
	 * 提交活动单
	 * @param confirmDTO
	 * @return
	 */
	ShopCartOrderMergerVO confirm(OrderConfirmDTO confirmDTO) throws ExecutionException, InterruptedException;

	void editPlatformRemark(Long orderId,String remark);

    /**
     * 通过openId获取用户的最近浏览直播间id
     * @param openId
     * @return
     */
    String getBorrowLivingRoomId(String openId);

	JSONObject yesterdayOrderStatistics();

	int syncWechatOrderId(Long orderId,Long wechatOrderId);

	int syncWeichatPromotionInfo(SyncWeichatPromotionInfoDTO syncWeichatPromotionInfoDTO);

	int syncChannelsSharerInfo(SyncSharerInfoRequest request);


	/**
	 * 获取用户消费金额 按照开始时间结束时间
	 *
	 * @param userId
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	Long userConsumeAmount(Long userId, String beginTime, String endTime);

	int jointVentureCommissionOrderSettled(List<Long> orderIds, Integer jointVentureCommissionStatus);

    CalculateDistributionCommissionResultVO calculateDistributionCommissionByUserId(Long userId);

	List<Long> getMatchedOrderIdList(OrderSearchDTO orderSearchDTO);

	/**
	 * 积分活动、抽奖游戏活动限制条件集合类
	 * @param checkOrderDTO
	 * @return
	 */
	int checkIsOrderIntoShops(CheckOrderDTO checkOrderDTO);


	void createEcOrder(ShopCartOrderMergerVO shopCartOrderMergerVO);

	void ecOrderPay(Long outOrderId,String tentacleNo);

	void ecCancelOrder(Long outOrderId);

	void reBuildDistributionInfo(Long orderId,String tentacleNo);
}
