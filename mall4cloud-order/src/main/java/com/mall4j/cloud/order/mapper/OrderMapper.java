package com.mall4j.cloud.order.mapper;

import com.mall4j.cloud.api.distribution.dto.DistributionQueryDTO;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.bo.OrderSimpleAmountInfoBO;
import com.mall4j.cloud.api.order.bo.OrderStatusBO;
import com.mall4j.cloud.api.order.bo.SendNotifyBO;
import com.mall4j.cloud.api.order.dto.*;
import com.mall4j.cloud.api.order.vo.*;
import com.mall4j.cloud.api.user.bo.UserOrderStatisticBO;
import com.mall4j.cloud.api.user.dto.DistributionUserQueryDTO;
import com.mall4j.cloud.api.user.dto.MemberReqDTO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.dto.OrderSearchDTO;
import com.mall4j.cloud.common.order.vo.EsOrderVO;
import com.mall4j.cloud.common.order.vo.UserOrderStatisticVO;
import com.mall4j.cloud.order.bo.SubmitOrderPayAmountInfoBO;
import com.mall4j.cloud.order.dto.app.DistributionOrderSearchDTO;
import com.mall4j.cloud.order.dto.app.DistributionRankingDTO;
import com.mall4j.cloud.order.dto.app.DistributionSalesStatDTO;
import com.mall4j.cloud.order.dto.platform.request.SyncSharerInfoRequest;
import com.mall4j.cloud.order.model.Order;
import com.mall4j.cloud.order.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 订单信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public interface OrderMapper {

    /**
     * 获取订单信息列表
     *
     * @param orderSearchDTO 订单搜索参数
     * @return 订单信息列表
     */
    List<EsOrderVO> list(@Param("order") OrderSearchDTO orderSearchDTO, @Param("pageAdapter") PageAdapter pageAdapter);

    Long listCount(@Param("order") OrderSearchDTO orderSearchDTO);

    Long newListCount(@Param("order") OrderSearchDTO orderSearchDTO);

    DistributionJointVentureOrderPreApplyInfoVO countDistributionJointVentureOrderPreApplyInfo(@Param("order") DistributionJointVentureOrderSearchDTO searchDTO);

    /**
     * 获取订单信息数量
     *
     * @param orderSearchDTO 订单搜索参数
     * @param time           订单开始时间
     * @return 订单信息列表
     */
    OrderListCountVO countOrderList(@Param("order") OrderSearchDTO orderSearchDTO, @Param("startTime") Date time);

    /**
     * 根据订单信息id获取订单信息
     *
     * @param orderId 订单信息id
     * @return 订单信息
     */
    Order getByOrderId(@Param("orderId") Long orderId);

    Order getByOrderNumber(@Param("orderNumber") String orderNumber);

    /**
     * 更新订单信息
     *
     * @param order 订单信息
     */
    void update(@Param("order") Order order);

    void updateDistributioninfo(@Param("order") Order order);

    /**
     * 根据订单信息id删除订单信息
     *
     * @param orderId
     */
    void deleteById(@Param("orderId") Long orderId);

    /**
     * 批量保存订单数据
     *
     * @param orders
     */
    void saveBatch(@Param("orders") List<Order> orders);

    /**
     * 查询订单状态
     *
     * @param orderIds 多个订单的订单id
     * @return 订单状态列表
     */
    List<OrderStatusBO> getOrdersStatus(@Param("orderIds") List<Long> orderIds);

    /**
     * 计算订单实际金额
     *
     * @param orderIds 多个订单的订单id
     * @return 订单实际金额总和
     */
    OrderAmountVO getOrdersActualAmount(@Param("orderIds") List<Long> orderIds);

    /**
     * 查询订单是否已经支付
     *
     * @param orderId 订单编号
     * @param userId  用户id
     * @return 是否已支付
     */
    Integer isPay(@Param("orderId") Long orderId, @Param("userId") Long userId);


    /**
     * 将订单改为已支付状态
     *
     * @param orderIds 订单ids
     * @param payType  支付方式
     */
    void updateByToPaySuccess(@Param("orderIds") List<Long> orderIds, @Param("payType") Integer payType);

    /**
     * 获取订单中的金额信息，不包含退款
     *
     * @param orderIds 多个订单的订单id
     * @return 获取订单中的金额信息
     */
    List<OrderSimpleAmountInfoBO> getOrdersSimpleAmountInfo(@Param("orderIds") List<Long> orderIds);

    /**
     * 取消订单
     *
     * @param orderIds 订单ids
     * @return
     */
    int cancelOrders(@Param("orderIds") List<Long> orderIds);

    /**
     * 根据订单号和用户id获取订单
     *
     * @param orderId orderId
     * @param userId  userId
     * @return Order
     */
    Order getOrderByOrderIdAndUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);

    /**
     * 注意：如果你无法理解下面这句话的话，请不要随意更改里面的sql
     * 众所周知，确认收货之后是不能改变状态的，
     * 但是确认收货之前是可以改变状态的，如果在确认收货之前，
     * 也就是下面这条sql执行之前进行了订单状态的改变（比如退款），那会造成不可预知的后果，
     * 所以更新订单状态的时候也要在条件当中加上订单状态，确定这条sql是原子性的
     * 这里的确认收货，条件加上订单的状态，确保这次更新是幂等的
     *
     * @param orderId
     * @return
     */
    int receiptOrder(@Param("orderId") Long orderId);

    /**
     * 根据订单号删除订单
     *
     * @param orderId 订单号
     */
    void deleteOrder(@Param("orderId") Long orderId);

    /**
     * 根据订单号和店铺id获取订单
     *
     * @param orderId orderId
     * @param shopId  shopId
     * @return Order
     */
    Order getOrderByOrderIdAndShopId(@Param("orderId") Long orderId, @Param("shopId") Long shopId);

    /**
     * 获取订单和订单项的数据
     *
     * @param orderId
     * @param shopId
     * @return
     */
    Order getOrderAndOrderItemData(@Param("orderId") Long orderId, @Param("shopId") Long shopId);

    /**
     * 根据订单id列表获取订单金额信息
     *
     * @param orderIdList 订单id列表
     * @return
     */
    SubmitOrderPayAmountInfoBO getSubmitOrderPayAmountInfo(@Param("orderIdList") long[] orderIdList);

    /**
     * 获取订单需要保存到es中的数据
     *
     * @param orderId 订单id
     * @return
     */
    EsOrderBO getEsOrder(@Param("orderId") Long orderId);

    EsOrderBO getEsOrderByOrderNumber(@Param("orderNumber") String orderNumber);

    EsOrderBO getEsOrderByWeChatOrderId(@Param("wechatOrderId") Long wechatOrderId);

    List<EsOrderBO> getEsOrderByWeChatOrderIds(@Param("wechatOrderIds") List<Long> wechatOrderIds);

    List<EsOrderBO> getEsOrderByOrderIds(@Param("orderIds") List<Long> orderIds);
    /**
     * 计算每个订单状态的状态数量
     *
     * @param userId 用户id
     * @return
     */
    OrderCountVO countNumberOfStatus(@Param("userId") Long userId);

    /**
     * 获取订单信息列表（excel导出）
     *
     * @param orderSearchDTO
     * @return
     */
    List<OrderExcelVO> excelOrderList(@Param("order") OrderSearchDTO orderSearchDTO,@Param("pageAdapter") PageAdapter pageAdapter);

    Integer excelOrderListCount(@Param("order") OrderSearchDTO orderSearchDTO);

    /**
     * 获取待发货的订单信息列表（excel）
     *
     * @param orderSearchDTO 搜索参数
     * @param lang           语言
     * @return 订单信息列表
     */
    List<UnDeliveryOrderExcelVO> excelUnDeliveryOrderList(@Param("order") OrderSearchDTO orderSearchDTO, @Param("lang") Integer lang);

    /**
     * 获取订单的创建状态，给秒杀提供查询是否已经创建订单成功的功能
     *
     * @param orderId 订单id
     * @return
     */
    int countByOrderId(Long orderId);

    /**
     * 根据店铺id查询不同状态的订单数量
     *
     * @param shopId
     * @return
     */
    OrderCountVO getOrderCountOfStatusByShopId(Long shopId);

    /**
     * 通过24小时分段获取支付金额
     *
     * @param shopId
     * @param startTime
     * @param endTime
     * @return
     */
    List<OrderOverviewVO> listActualByHour(@Param("shopId") Long shopId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<OrderOverviewVO> stafflistActualByHour(@Param("shopIds") List<Long> shopIds, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 根据日期范围获取订单概况信息列表
     *
     * @param shopId
     * @param startTime
     * @param endTime
     * @param dayCount
     * @return
     */
    List<OrderOverviewVO> listOrderOverviewInfoByShopIdAndDateRange(@Param("shopId") Long shopId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("dayCount") Integer dayCount);

    List<OrderOverviewVO> staffListOrderOverviewInfoByShopIdAndDateRange(@Param("shopIds") List<Long> shopIds, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 根据日期范围获取订单概况信息
     *
     * @param shopId
     * @param startTime
     * @param endTime
     * @return
     */
    OrderOverviewVO getOrderOverviewInfoByShopId(@Param("shopId") Long shopId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    OrderOverviewVO getStaffOrderOverviewInfoByShopId(@Param("shopIds") List<Long> shopIds, @Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 统计用户相关的订单信息
     *
     * @param userIds
     * @return
     */
    List<UserOrderStatisticVO> countOrderByUserIds(@Param("userIds") List<Long> userIds);

    /**
     * 将订单更新为待成团状态
     *
     * @param orderIds 需要更新的订单id
     * @param payType  支付方式
     */
    void updateByToGroupPaySuccess(@Param("orderIds") List<Long> orderIds, @Param("payType") Integer payType);

    /**
     * 更新团购订单到待发货状态
     *
     * @param orderIds 订单ids
     */
    void updateGroupOrderSuccessStatus(@Param("orderIds") List<Long> orderIds);

    /**
     * 统计用户已支付的订单数量
     *
     * @param userId
     * @return
     */
    int countPageOrderByUserId(@Param("userId") String userId);

    /**
     * 获取用户已支付订单数据
     *
     * @param pageAdapter
     * @param userId
     * @return
     */
    List<OrderVO> pageByUserId(@Param("page") PageAdapter pageAdapter, @Param("userId") String userId);

    /**
     * 获取订单部分数据用户推送消息通知
     *
     * @param orderIds 订单ids
     * @return 订单数量集合
     */
    List<SendNotifyBO> listByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 付款用户列表
     *
     * @param orderSearchDTO 条件参数
     * @return 用户id列表
     */
    List<Long> getOrderUserIdsBySearchDTO(@Param("orderSearchDTO") OrderSearchDTO orderSearchDTO);

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
    List<Long> listUserIdByPurchaseNum(@Param("isPayed") Integer isPayed,
                                       @Param("deleteStatus") Integer deleteStatus,
                                       @Param("startDate") Date startDate,
                                       @Param("endDate") Date endDate,
                                       @Param("status") Integer status,
                                       @Param("minNum") Long minNum,
                                       @Param("maxNum") Long maxNum);

    /**
     * 获取在一定时间内消费一定金额的会员信息
     *
     * @param isPayed      是否已支付
     * @param deleteStatus 用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除
     * @param startDate    开始时间
     * @param endDate      结束时间
     * @param status       状态
     * @param minAmount    最小支付金额
     * @param maxAmount    最大支付金额
     * @return 会员id
     */
    List<Long> listUserIdByAverageActualTotal(@Param("isPayed") Integer isPayed,
                                              @Param("deleteStatus") Integer deleteStatus,
                                              @Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate,
                                              @Param("status") Integer status,
                                              @Param("minAmount") Long minAmount,
                                              @Param("maxAmount") Long maxAmount);

    /**
     * 获取用户的成交留存率, 统计用户第一次成交后，之后的1到6个月的留存用户数
     *
     * @param customerRetainedDTO 条件查询参数
     * @return 成交留存信息列表
     */
    List<CustomerRetainVO> getTradeRetained(@Param("customerRetainedDTO") CustomerRetainedDTO customerRetainedDTO);

    /**
     * 根据参数获取数据分析中的订单信息
     *
     * @param spuIds    产品id列表
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 商品效果分析数据
     */
    List<OrderProdEffectRespVO> getProdEffectByDateAndProdIds(@Param("spuIds") List<Long> spuIds, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 根据订单id列表， 获取订单流量信息列表
     *
     * @param orderIds 订单id列表
     * @return 订单流量信息列表
     */
    List<FlowOrderVO> listFlowOrderByOrderIds(@Param("orderIds") Collection<Long> orderIds);

    /**
     * 会员支付信息
     *
     * @param param 参数
     * @return 会员支付信息
     */
    List<UserOrderStatisticBO> getPaidMemberByParam(@Param("param") MemberReqDTO param);


    /**
     * 获取新老会员成交数据
     *
     * @param param 筛选参数
     * @param type  1.新会员 0.老会员
     * @return 新老会员成交数据
     */
    List<UserOrderStatisticBO> getMemberPayData(@Param("param") MemberReqDTO param, @Param("type") Integer type);

    /**
     * 获取订单id，根据订单订单状态和时间
     *
     * @param orderStatus 订单状态
     * @param startTime   开始时间
     * @return 订单和订单项
     */
    List<Long> listOrderId(@Param("orderStatus") Integer orderStatus, @Param("startTime") Date startTime);


    List<Long> confirmOrderListOrderId(@Param("orderStatus") Integer orderStatus, @Param("startTime") Date startTime);

    /**
     * 获取订单id，根据订单订单状态和时间
     *
     * @param orderStatus 订单状态
     * @param startTime   开始时间
     * @return 订单和订单项
     */
    List<Long> listDistributionOrderId(@Param("orderStatus") Integer orderStatus, @Param("startTime") Date startTime);

    /**
     * 将订单标记为已结算状态
     *
     * @param orderIds 订单ids
     * @return 变更数量
     */
    int settledOrder(@Param("orderIds") List<Long> orderIds);

    /**
     * 获取指定时间端内的订单数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 订单列表
     */
    Long countOrderNum(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 获取最初的订单创建时间
     *
     * @return
     */
    Date minOrderCreateTime();

    /**
     * 获取订单及订单项的列表信息
     *
     * @param orderSearchDTO 订单查询参数
     * @param pageAdapter    分页
     * @return 订单及订单项的列表
     */
    List<Order> listOrder(@Param("order") OrderSearchDTO orderSearchDTO, @Param("page") PageAdapter pageAdapter);


    /**
     * 批量修改订单信息
     *
     * @param orders 订单信息
     */
    void updateRefundStatusBatchById(@Param("orders") List<Order> orders);

    /**
     * 获取正常状态的订单数量
     *
     * @param orderIds 订单ids
     * @return 订单详情
     */
    Integer countNormalOrderByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 更新订单时间，用于更新es数据，发票使用
     *
     * @param orderId
     */
    void updateOrderTime(@Param("orderId") Long orderId);

    /**
     * 根据用户id列表获取积累消费金额和积累消费笔数
     *
     * @param userIds 用户id列表
     * @return
     */
    List<SumAmountVO> listSumDataByUserIds(@Param("userIds") List<Long> userIds);

	/**
	 * 判断是否需要购买置指定商品（购买指定商品的数量）
	 *
	 * @param spuId 商品id
	 * @param userId 用户id
	 * @return 购买指定商品的数量
	 */
    long hasBuySuccessProd(@Param("spuId") Long spuId, @Param("userId") Long userId);

	/**
	 * 统计用户购物数据
	 * @param userId 用户id
	 * @return 用户购物数据
	 */
    UserShoppingDataVO calculateUserInShopData(@Param("userId") Long userId);

    /**
     * 根据多个订单号获取订单集合
     * @param orderIds 订单号集合
     * @return 订单列表
     */
    List<EsOrderBO> getEsOrderList(@Param("orderIds") List<Long> orderIds);

    /**
     * 修改订单分销金额
     * @param orders 订单
     */
    void updateBatchDistributionAmount(@Param("orders") List<Order> orders);

    /**
     * 获取已结算的订单
     * @param settled 是否已经进行结算
     * @param orderIds 订单号集合
     * @return 已结算的订单
     */
    List<EsOrderBO> listBySettledOrOrderIds(@Param("settled") Integer settled, @Param("orderIds") List<Long> orderIds);

    /**
     * 根据订单id获取发货需要的信息
     * @param orderId
     * @return
     */
    Order getDeliveryInfoByOrderId(@Param("orderId") Long orderId);


    void updateDistributionStatusBatchById(@Param("orderIds") List<Long> orderIds, @Param("distributionStatus") Integer distributionStatus, @Param("distributionSettleTime") Date distributionSettleTime, @Param("distributionWithdrawTime") Date distributionWithdrawTime);


    void updateDevelopingStatusBatchById(@Param("orderIds") List<Long> orderIds, @Param("developingStatus") Integer developingStatus, @Param("developingSettleTime") Date developingSettleTime, @Param("developingWithdrawTime") Date developingWithdrawTime);

    int updateJointVentureCommissionStatusBatchById(@Param("orderIds") List<Long> orderIds,
                                                    @Param("jointVentureCommissionStatus") Integer jointVentureCommissionStatus,
                                                    @Param("jointVentureSettleTime") Date jointVentureSettleTime,
                                                    @Param("jointVentureRefundStatus") Integer jointVentureRefundStatus);

    /**
     * 导购-待结算/已结算订单列表
     *
     * @param distributionOrderSearchDTO 订单搜索参数
     * @param pageAdapter                分页信息
     * @return 订单信息列表
     */
    List<DistributionOrderVO> listPageWithDistributionStaff(@Param("order") DistributionOrderSearchDTO distributionOrderSearchDTO, @Param("page") PageAdapter pageAdapter);

    /**
     * 微客-待结算/已结算订单列表
     *
     * @param distributionOrderSearchDTO 订单搜索参数
     * @param pageAdapter    分页信息
     * @return 订单信息列表
     */
    List<DistributionOrderVO> listPageWithDistributionUser(@Param("order") DistributionOrderSearchDTO distributionOrderSearchDTO, @Param("page") PageAdapter pageAdapter);


    List<EsOrderBO> listByStaffAndTime(@Param("staffId") Long staffId, @Param("endTime") Date endTime);
    /**
     * 分销订单业绩统计
     *
     * @param distributionSalesStatDTO
     * @return
     */
    Long distributionSalesStat(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);


    List<EsOrderBO> listDistributionOrders(@Param("queryDTO") DistributionQueryDTO queryDTO);

    List<EsOrderBO> listDistributionJointVentureOrders(@Param("searchDTO") DistributionJointVentureOrderSearchDTO searchDTO);

    List<EsOrderBO> listDistributionJointVentureOrdersV2(@Param("searchDTO") DistributionJointVentureOrderSearchDTO searchDTO);

    List<DistributionJointVentureOrderRefundRespDTO> listDistributionJointVentureRefundOrders(@Param("searchDTO")DistributionJointVentureOrderRefundSearchDTO searchDTO);

    /**
     * 统计成功支付及成功退款金额
     * @param searchDTO
     * @return
     */
    DistributionJointVentureOrderPayRefundRespDTO totalDistributionJointVenturePayRefund(@Param("searchDTO")DistributionJointVentureOrderRefundSearchDTO searchDTO);

    /**
     * 统计分销排名
     */
    Integer countDistributionRanking(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    Integer countDistributionOrderNum(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    Integer countDistributionOrderUserNum(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    Long storeDistributionSalesStat(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    Integer countStoreDistributionOrderNum(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    Integer countStoreDistributionUserNum(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    Long countStoreDistributionCommission(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    Integer countStoreDistributionOrderUserNum(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    Long countStoreDistributionOrderUserAmount(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    Integer countDistributionStoreRanking(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    Integer countDistributionOrderStaffNum(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    Integer countDistributionOrderSpuNum(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    Long countDistributionOrderTotalPrice(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    Long countOrderAmountByUserId(@Param("userId") Long userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<DistributionRankingDTO> listDistributionRanking(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    List<DistributionStoreStatisticsVO> pageStoreStatistics(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    Long countDistributionCommission(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    void editPlatformRemark(@Param("orderId") Long orderId, @Param("remark") String remark);

    List<DistributionStaffExcelVO> listDistributionStaffExcel(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    List<DistributionWitkeyExcelVO> listDistributionWitkeyExcel(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    Integer countStoreDistributionWitkeyNum(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO, @Param("queryDTO") DistributionUserQueryDTO queryDTO);

    Integer countStoreOrderUserNum(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO, @Param("queryDTO") DistributionUserQueryDTO queryDTO);

    Long countStoreOrderUserAmount(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO, @Param("queryDTO") DistributionUserQueryDTO queryDTO);

    YesterdayOrderStatisticsVO yesterdayOrderStatistics(@Param("beginTime") String startTime, @Param("endTime") String endTime);

    int syncWechatOrderId(@Param("orderId") Long orderId, @Param("wechatOrderId") Long wechatOrderId);

    int syncWeichatPromotionInfo(@Param("syncWeichatPromotionInfoDTO") SyncWeichatPromotionInfoDTO syncWeichatPromotionInfoDTO);

    int syncChannelsSharerInfo(@Param("syncSharerInfoRequest") SyncSharerInfoRequest syncSharerInfoRequest);

    Long userConsumeAmount(@Param("userId") Long userId, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    Long listDistributionJointVentureOrderCount(@Param("order") DistributionJointVentureOrderSearchDTO searchDTO);

    List<EsOrderVO> listDistributionJointVentureOrder(@Param("order") DistributionJointVentureOrderSearchDTO searchDTO, @Param("pageAdapter") PageAdapter pageAdapter);

    CalculateDistributionCommissionResultVO calculateDistributionCommissionByUserId(@Param("userId") Long userId);

    List<Long> getMatchedOrderIdList(@Param("orderSearchDTO") OrderSearchDTO orderSearchDTO);

    int checkIsOrderIntoShops(@Param("userId") Long userId, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("orderNum") Long orderNum, @Param("storeIdList") List<Long> storeIdList);

    int syncFinderOpenid(@Param("orderId") Long orderId, @Param("finderId") String finderId, @Param("finderName") String finderName, @Param("storeId") Long storeId);
}
