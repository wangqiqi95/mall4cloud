package com.mall4j.cloud.order.mapper;

import com.mall4j.cloud.api.order.bo.RefundAmountWithOrderIdBO;
import com.mall4j.cloud.api.order.vo.OrderRefundProdEffectRespVO;
import com.mall4j.cloud.api.order.vo.OrderRefundSimpleVO;
import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.order.dto.app.DistributionSalesStatDTO;
import com.mall4j.cloud.order.dto.app.OrderRefundPageDTO;
import com.mall4j.cloud.order.model.OrderRefund;
import com.mall4j.cloud.order.vo.OrderRefundStatisticsVO;
import com.mall4j.cloud.order.vo.OrderRefundVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.function.IntToDoubleFunction;

/**
 * 订单退款记录信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
public interface OrderRefundMapper {

	/**
	 * 获取订单退款记录信息列表
	 *
	 * @param orderRefundPageDTO
	 * @param pageAdapter
	 * @return 订单退款记录信息列表
	 */
	List<OrderRefundVO> list(@Param("orderRefundPageDTO") OrderRefundPageDTO orderRefundPageDTO, @Param("pageAdapter") PageAdapter pageAdapter);

	/**
	 * 获取订单退款记录信息列表
	 *
	 * @param orderRefundPageDTO
	 * @param pageAdapter
	 * @return 订单退款记录信息列表
	 */
	List<OrderRefundVO> excelList(@Param("orderRefundPageDTO") OrderRefundPageDTO orderRefundPageDTO, @Param("pageAdapter") PageAdapter pageAdapter);

	/**
	 * 获取订单退款记录信息列表数量
	 *
	 * @param orderRefundPageDTO
	 * @return 订单退款记录信息列表数量
	 */
	Long count(@Param("orderRefundPageDTO") OrderRefundPageDTO orderRefundPageDTO);

	/**
	 * 根据订单退款记录信息id获取订单退款记录信息
	 *
	 * @param refundId 订单退款记录信息id
	 * @return 订单退款记录信息
	 */
	OrderRefundVO getByRefundId(@Param("refundId") Long refundId);

	OrderRefundVO getByRefundNumber(@Param("refundNumber") String refundNumber);

	OrderRefundVO getByAftersaleId(@Param("AftersaleId") Long AftersaleId);

	List<OrderRefundVO> getByAftersaleIds(@Param("aftersaleIds") List<Long> aftersaleIds);


	Integer getCountByOrderItemId(@Param("orderItemId") Long orderItemId);

	int syncAftersaleId(@Param("refundId") Long refundId, @Param("AftersaleId") Long AftersaleId);

	/**
	 * 根据订单退款记录信息id获取订单退款记录信息 详情，包括订单项，订单信息等
	 *
	 * @param refundId 订单退款记录信息id
	 * @return 订单退款记录信息
	 */
	OrderRefundVO getDetailByRefundId(Long refundId);

	OrderRefundVO getDetailByRefundNumber(String refundNumber);

	/**
	 * 保存订单退款记录信息
	 *
	 * @param orderRefund 订单退款记录信息
	 */
	void save(@Param("orderRefund") OrderRefund orderRefund);

	/**
	 * 更新订单退款记录信息
	 *
	 * @param orderRefund 订单退款记录信息
	 */
	void update(@Param("orderRefund") OrderRefund orderRefund);

	/**
	 * 根据订单退款记录信息id删除订单退款记录信息
	 *
	 * @param refundId
	 */
	void deleteById(@Param("refundId") Long refundId);

	/**
	 * 根据订单id获取退款金额
	 *
	 * @param orderIds 订单id
	 * @return
	 */
	List<RefundAmountWithOrderIdBO> sumRefundSuccessAmountByOrderId(@Param("orderIds") List<Long> orderIds);

	/**
	 * 根据订单id，获取正在退款中或退款成功的订单
	 *
	 * @param orderId
	 * @return
	 */
	List<OrderRefund> getProcessOrderRefundByOrderId(@Param("orderId") Long orderId);

	/**
	 * 根据订单id，获取正在退款中或退款成功的订单
	 *
	 * @param orderId        订单id
	 * @param returnMoneySts 退款状态
	 * @return 成功的退款订单
	 */
	List<com.mall4j.cloud.api.order.vo.OrderRefundVO> getOrderRefundByOrderIdAndRefundStatus(@Param("orderId") Long orderId, @Param("returnMoneySts") Integer returnMoneySts);

	com.mall4j.cloud.api.order.vo.OrderRefundVO getOrderRefundByAftersaleId(@Param("aftersaleId") Long aftersaleId);

	/**
	 * 获取订单正在进行退款的退款数量
	 *
	 * @param returnMoneySts
	 * @param returnMoneySts2
	 * @param orderId
	 * @return
	 */
	Integer countByReturnMoneyStsAndOrderId(@Param("returnMoneySts") Integer returnMoneySts, @Param("returnMoneySts2") Integer returnMoneySts2, @Param("orderId") Long orderId);

	/**
	 * 正在进行退款的退款订单数量
	 *
	 * @param orderId 订单号
	 * @return 正在进行退款的退款订单数量
	 */
	Integer countReturnProcessingItemByOrderId(@Param("orderId") Long orderId);


	/**
	 * 查询退款成功的订单商品数量
	 *
	 * @param orderId
	 * @return
	 */
	Integer countRefundSuccessRefundCountByOrderId(@Param("orderId") Long orderId);

	Integer allCountRefundSuccessRefundCountByOrderId(@Param("orderId") Long orderId);


	/**
	 * 同意退货
	 * <p>
	 * 注意：如果你无法理解下面这句话的话，请不要随意更改里面sql的where语句
	 * 众所周知，如果不是申请的话，下一步不可能是同意退货，
	 * 但是在执行下面代码之前，退款的状态是可能发生改变的，
	 * 也就是下面这条sql执行之前进行了退款状态的改变（比如同意退款），那会造成不可预知的后果，
	 * 所以更新订单状态的时候也要在条件当中加上退款状态，确定这条sql是原子性的
	 *
	 * @param sellerMsg
	 * @param refundId
	 * @return
	 */
	int agreeReturns(@Param("sellerMsg") String sellerMsg, @Param("refundId") Long refundId);

	int ecAgreeReturns(@Param("sellerMsg") String sellerMsg, @Param("refundId") Long refundId);

	/**
	 * 拒绝退款
	 * <p>
	 * 注意：如果你无法理解下面这句话的话，请不要随意更改里面的sql的where语句
	 * 众所周知，如果不是申请的话，下一步不可能是拒绝，
	 * 但是在执行下面代码之前，退款的状态是可能发生改变的，
	 * 也就是下面这条sql执行之前进行了退款状态的改变（比如同意退款），那会造成不可预知的后果，
	 * 所以更新订单状态的时候也要在条件当中加上退款状态，确定这条sql是原子性的
	 *
	 * @param sellerMsg
	 * @param rejectMessage
	 * @param refundId
	 * @param returnMoneySts
	 * @param receiver
	 * @return
	 */
	int disagreeRefund(@Param("sellerMsg") String sellerMsg, @Param("rejectMessage") String rejectMessage, @Param("refundId") Long refundId,
					   @Param("returnMoneySts") Integer returnMoneySts, @Param("receiver") Boolean receiver);

	/**
	 * 同意退款
	 *
	 * @param sellerMsg
	 * @param refundId
	 * @param returnMoneySts
	 * @param receiver
	 * @param applyType
	 * @return
	 */
	int agreeRefund(@Param("sellerMsg") String sellerMsg, @Param("refundId") Long refundId, @Param("returnMoneySts") Integer returnMoneySts,
					@Param("receiver") Boolean receiver, @Param("applyType") Integer applyType);

	/**
	 * 获取退款状态
	 *
	 * @param refundId
	 * @return
	 */
	Integer getRefundStatus(@Param("refundId") Long refundId);

	Integer initRefundStatus(@Param("refundId") Long refundId, @Param("status") Integer status,@Param("buyDesc") String buyDesc,@Param("imgs") String imgs);

	/**
	 * 获取指定时间范围的退款订单比率信息及退款金额统计列表
	 *
	 * @param shopId
	 * @param startTime
	 * @param endTime
	 * @param dayCount
	 * @return
	 */
	List<OrderRefundStatisticsVO> listOrderRefundInfoByShopIdAndDateRange(@Param("shopId") Long shopId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("dayCount") Integer dayCount);

	/**
	 * 根据指定时间范围获取根据商品名生成退款排行
	 *
	 * @param shopId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<OrderRefundStatisticsVO> listRefundRankingByProd(@Param("shopId") Long shopId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 根据日期范围获取订单退款概况概况信息
	 *
	 * @param shopId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	OrderRefundStatisticsVO getOrderRefundInfoByShopId(@Param("shopId") Long shopId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 获取退款原因排行
	 *
	 * @param shopId
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<OrderRefundStatisticsVO> listRefundRankingByReason(@Param("shopId") Long shopId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 获取店铺退单数量排行
	 *
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @param limit     排行榜取几条数据
	 * @return
	 */
	List<OrderRefundStatisticsVO> listShopRankingByRefundCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("limit") Integer limit);

	/**
	 * 通过订单id获取正在处理中的退款订单
	 *
	 * @param orderId 订单id
	 * @return 正在处理中的退款订单
	 */
	List<OrderRefundVO> getProcessingOrderRefundByOrderId(@Param("orderId") Long orderId);

	/**
	 * 根据参数获取商品退款订单数据分析
	 *
	 * @param spuIds    商品id列表
	 * @param startTime 开始时间
	 * @param endTime   结束时间
	 * @return 商品退款订单数据
	 */
	List<OrderRefundProdEffectRespVO> getProdRefundEffectByDateAndProdIds(@Param("spuIds") List<Long> spuIds, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 获取退款超时的订单
	 *
	 * @param date 超时时间
	 * @return 退款订单
	 */
	List<OrderRefundVO> listOrderRefundTimeOut(@Param("date") Date date);

	/**
	 * 批量更新订单退款记录信息
	 *
	 * @param orderRefundList 订单退款记录信息
	 */
	void updateBatchById(@Param("orderRefundList") List<OrderRefundVO> orderRefundList);

	/**
	 * 获取临近超时的订单
	 *
	 * @param date     临近超时时间
	 * @param overDate 超时时间
	 * @return 退款订单
	 */
	List<OrderRefundVO> listProximityRefundTimeOut(@Param("date") Date date, @Param("overDate") Date overDate);

	/**
	 * 根据退款单号列表获取订单号列表
	 *
	 * @param refundIds
	 * @return
	 */
	List<OrderRefundSimpleVO> listOrderIdsByRefundIds(@Param("refundIds") List<Long> refundIds);

	void editPlatformRemark(@Param("refundId") Long refundId, @Param("remark") String remark);

	int syncComplaintOrderId(@Param("complaintOrderId") Long complaintOrderId, @Param("aftersaleId") Long aftersaleId);

	int syncComplaintOrderStatus(@Param("complaintOrderId") Long complaintOrderId);

	int complaintOrderPlatformRead(@Param("complaintOrderId") Long complaintOrderId);

	int complaintOrderUserRead(@Param("complaintOrderId") Long complaintOrderId);

	int offlineRefund(@Param("aftersaleId") Long aftersaleId);

	int offlineRefundSuccess(@Param("aftersaleId") Long aftersaleId);

	int offlineUploadCertificates(@Param("aftersaleId") Long aftersaleId);

	void returnFreightAmount(@Param("refundId") Long refundId, @Param("refundFreightAmount") Long refundFreightAmount);

	void unReturnFreightAmount(@Param("refundId") Long refundId);

	List<OrderRefundSimpleVO> listOrderIdsByRefundId(@Param("refundId") String refundId);

	Long countDistributionRefundAmount(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

	Integer countDistributionRefundNum(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

	Long countStoreDistributionRefundAmount(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

	Integer countStoreDistributionRefundNum(@Param("distributionSalesStatDTO") DistributionSalesStatDTO distributionSalesStatDTO);

    int ecInitRefundStatus(@Param("refundId")Long refundId,@Param("status") Integer status,@Param("applyType") Integer applyType,@Param("refundAmount")Long refundAmount);

	void updateJointVentureRefundOrder(@Param("refundIds") List<Long> refundIds, @Param("jointVentureRefundStatus") Integer jointVentureRefundStatus);
}
