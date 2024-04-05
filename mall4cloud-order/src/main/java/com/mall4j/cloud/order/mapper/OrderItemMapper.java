package com.mall4j.cloud.order.mapper;

import com.mall4j.cloud.api.delivery.dto.DeliveryOrderItemDTO;
import com.mall4j.cloud.api.order.bo.DistributionAmountWithOrderIdBO;
import com.mall4j.cloud.api.order.bo.EsOrderItemBO;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.order.model.OrderItem;
import com.mall4j.cloud.order.vo.OrderItemDetailVO;
import com.mall4j.cloud.order.vo.OrderItemPriceErrorVO;
import com.mall4j.cloud.order.vo.OrderOverviewVO;
import com.mall4j.cloud.order.vo.PaperCouponOrderItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 订单项
 *
 * @author FrozenWatermelon
 * @date 2020-12-04 11:27:35
 */
public interface OrderItemMapper {

	/**
	 * 获取订单项列表
	 *
	 * @return 订单项列表
	 */
	List<OrderItemVO> list();

	/**
	 * 根据订单项id获取订单项
	 *
	 * @param orderItemId 订单项id
	 * @param lang
	 * @return 订单项
	 */
	OrderItemVO getByOrderItemId(@Param("orderItemId") Long orderItemId, @Param("lang") Integer lang);

	/**
	 * 根据订单项id获取订单项
	 *
	 * @param orderItemId 订单项id
	 * @return 订单项
	 */
	OrderItemVO getByItemId(@Param("orderItemId") Long orderItemId);

	/**
	 * 保存订单项
	 *
	 * @param orderItem 订单项
	 */
	void save(@Param("orderItem") OrderItem orderItem);

	/**
	 * 更新订单项
	 *
	 * @param orderItem 订单项
	 */
	void update(@Param("orderItem") OrderItem orderItem);

	/**
	 * 根据订单项id删除订单项
	 *
	 * @param orderItemId
	 */
	void deleteById(@Param("orderItemId") Long orderItemId);

	/**
	 * 批量保存
	 *
	 * @param orderItems 订单项列表
	 */
    void saveBatch(@Param("orderItems") List<OrderItem> orderItems);

	/**
	 * 根据订单号获取订单项
	 *
	 * @param orderId 订单id
	 * @return 订单项
	 */
    List<OrderItem> listOrderItemsByOrderId(@Param("orderId") Long orderId);

	/**
	 * 根据订单号获取订单项
	 *
	 * @param orderId 订单id
	 * @return 订单项
	 */
    List<OrderItemVO> listOrderItemAndLangByOrderId(@Param("orderId") Long orderId);

	/**
	 * 计算订单项已结算分销金额
	 *
	 * @param orderIds
	 * @return
	 */
	List<DistributionAmountWithOrderIdBO> sumTotalDistributionAmountByOrderIds(@Param("orderIds") List<Long> orderIds);

	/**
	 * 批量更新
	 *
	 * @param orderItems
	 */
	void updateBatch(@Param("orderItems") List<OrderItem> orderItems);


	/**
	 * 批量更新佣金
	 *
	 * @param orderItems 订单商品信息
	 */
	void updateCommissionBatch(@Param("orderItems") List<OrderItem> orderItems);

//	/**
//	 * 根据订单id获取商品名称
//	 *
//	 * @param orderIdList 订单id
//	 * @param lang 语言id
//	 * @return 商品名称列表
//	 */
//    List<String> getSpuNameListByOrderIds(@Param("orderIdList") long[] orderIdList, @Param("lang") Integer lang);

	/**
	 * 根据订单id获取订单项数量
	 *
	 * @param orderId
	 * @return
	 */
	Integer countByOrderId(@Param("orderId") Long orderId);

	Integer allCountByOrderId(@Param("orderId") Long orderId);

	/**
	 * 更新订单项发货数量信息
	 *
	 * @param orderItem    发货订单项
	 * @param deliveryType 发货方式
	 */
	void updateByDelivery(@Param("orderItem") DeliveryOrderItemDTO orderItem, @Param("deliveryType") Integer deliveryType);

	/**
	 * 计算未发货商品数量
	 *
	 * @param orderId
	 * @return
	 */
	int countUnDeliveryNumByOrderId(@Param("orderId") Long orderId);

	/**
	 * 减少订单项待发货数量
	 *
	 * @param orderItemId
	 * @param count
	 */
    void reduceUnDeliveryNumByOrderItemId(@Param("orderItemId") Long orderItemId, @Param("count") Integer count);

	/**
	 * 更新订单项退款状态
	 *
	 * @param orderId
	 * @param refundStatus
	 */
    void updateRefundStatusByOrderId(@Param("orderId") Long orderId, @Param("refundStatus") Integer refundStatus);

	/**
	 * 根据订单数量生成商品排行
	 *
	 * @param shopId    店铺ID
	 * @param startTime 起始时间
	 * @param endTime   结束时间
	 * @param limit     排行榜取几条数据
	 * @return
	 */
    List<OrderOverviewVO> listSpuRankingByOrderCount(@Param("shopId") Long shopId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("limit") int limit);

	/**
	 * 根据支付金额生成店铺排行
	 *
	 * @param startTime
	 * @param endTime
	 * @param limit
	 * @return
	 */
    List<OrderOverviewVO> listShopRankingByPayActual(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("limit") int limit);

	/**
	 * 根据订单id查询订单项、退款详情
	 *
	 * @param orderId 订单id
	 * @param lang    语言
	 * @return 订单项、退款详情
	 */
	List<OrderItemDetailVO> listDetailByOrderId(@Param("orderId") Long orderId, @Param("lang") Integer lang);

	/**
	 * 根据订单项id获取订单项中的商品信息
	 *
	 * @param orderItemId
	 * @param lang
	 * @return
	 */
    OrderItemVO getSpuInfoByOrderItemId(@Param("orderItemId") Long orderItemId, @Param("lang") Integer lang);

	/**
	 * 根据订单号获取订单项的物流类型
	 *
	 * @param orderItemId 订单号
	 * @return 物流类型
	 */
	int getDevTypeByOrderItemId(@Param("orderItemId") Long orderItemId);

	/**
	 * 根据订单号获取订单项
	 *
	 * @param orderId 订单号
	 * @return 订单id以及发货方式
	 */
	List<Integer> getDevTypeByOrderId(@Param("orderId") Long orderId);

	/**
	 * 获取订单项列表
	 *
	 * @param orderItemIds 订单项id
	 * @return 订单项
	 */
	List<OrderItemVO> getOrderItems(@Param("orderItemIds") List<Long> orderItemIds);

	/**
	 * 批量修改订单项的分销金额
	 *
	 * @param orderItems 订单项
	 * @return 受影响行数
	 */
    int updateBatchDistributionAmount(@Param("orderItems") List<EsOrderItemBO> orderItems);

	/**
	 * 获取指定的订单项
	 * @param orderIds
	 * @return
	 */
	List<OrderItemVO> listOrderItemByOrderIds(@Param("orderIds") List<Long> orderIds);


	List<OrderItemVO> listOrderItems(@Param("orderItem")OrderItem orderItem);

	/**
	 * 根据订单项id返回订单项信息
	 * @param orderItemIds
	 * @return
	 */
	List<OrderItemVO> listOrderItemByIds(@Param("orderItems") List<Long> orderItemIds);

	List<OrderItemPriceErrorVO> listOrderItemsIsPay(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

	OrderItemVO getOrderItemByOrderNumberAndSkuId(@Param("orderNumber") String orderNumber, @Param("skuId") Long skuId);

    int jointVentureCommissionOrderItemSettled(@Param("orderIds") List<Long> orderIds,
                                               @Param("jointVentureCommissionStatus") Integer jointVentureCommissionStatus,
                                               @Param("jointVentureRefundStatus") Integer jointVentureRefundStatus);

    int updateDistributionRefundStatusBatchByOrderItemId(@Param("orderItemIds") List<Long> orderItemIds, @Param("distributionRefundStatus") Integer distributionRefundStatus);

    int updateDistributionRefundStatusByOrderId(@Param("orderId") Long orderId, @Param("distributionRefundStatus") Integer distributionRefundStatus);
	
	/**
	 * 根据订单id集合返回纸质券核销订单项信息
	 * @param orderNoList
	 * @return
	 */
	List<PaperCouponOrderItemVO> listPaperCouponOrderItemByIds(@Param("orderNoList") List<Long> orderNoList);
}
