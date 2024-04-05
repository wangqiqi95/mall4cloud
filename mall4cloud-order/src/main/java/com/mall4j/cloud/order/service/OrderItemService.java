package com.mall4j.cloud.order.service;

import com.mall4j.cloud.api.delivery.dto.DeliveryOrderItemDTO;
import com.mall4j.cloud.api.order.bo.DistributionAmountWithOrderIdBO;
import com.mall4j.cloud.api.order.bo.EsOrderItemBO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import com.mall4j.cloud.order.model.OrderItem;
import com.mall4j.cloud.order.vo.OrderDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单项
 *
 * @author FrozenWatermelon
 * @date 2020-12-04 11:27:35
 */
public interface OrderItemService {

	/**
	 * 分页获取订单项列表
	 *
	 * @param pageDTO 分页参数
	 * @return 订单项列表分页数据
	 */
	PageVO<OrderItemVO> page(PageDTO pageDTO);

	/**
	 * 根据订单项id获取订单项
	 *
	 * @param orderItemId 订单项id
	 * @return 订单项
	 */
	OrderItemVO getByOrderItemId(Long orderItemId);

	/**
	 * 保存订单项
	 *
	 * @param orderItem 订单项
	 */
	void save(OrderItem orderItem);

	/**
	 * 更新订单项
	 *
	 * @param orderItem 订单项
	 */
	void update(OrderItem orderItem);

	/**
	 * 根据订单项id删除订单项
	 *
	 * @param orderItemId
	 */
	void deleteById(Long orderItemId);

	/**
	 * 根据订单号获取订单项
	 *
	 * @param orderId 订单id
	 * @return 订单项
	 */
    List<OrderItem> listOrderItemsByOrderId(Long orderId);

	/**
	 * 根据订单号获取订单项(包含sku、spu名称)
	 *
	 * @param orderId 订单id
	 * @return 订单项
	 */
    List<OrderItemVO> listOrderItemAndLangByOrderId(Long orderId);

	/**
	 * 计算订单项已结算分销金额
	 *
	 * @param orderIds
	 * @return
	 */
	List<DistributionAmountWithOrderIdBO> sumTotalDistributionAmountByOrderIds(List<Long> orderIds);

	/**
	 * 批量保存订单项信息
	 *
	 * @param orderItems
	 */
	void saveBatch(List<OrderItem> orderItems);

	/**
	 * 批量更新订单项信息
	 *
	 * @param orderItems
	 */
	void updateBatch(List<OrderItem> orderItems);

	/**
	 * 根据订单id获取商品名称
	 *
	 * @param orderIdList 订单id
	 * @return 商品名称列表
	 */
    List<String> getSpuNameListByOrderIds(long[] orderIdList);

	/**
	 * 根据订单id获取订单项数量
	 * @param orderId
	 * @return
	 */
	Integer countByOrderId(@Param("orderId") Long orderId);

	/**
	 * 根据订单id获取订单项数量
	 * @param orderId
	 * @return
	 */
	Integer allCountByOrderId(@Param("orderId") Long orderId);

	/**
	 * 更新订单项发货数量信息
	 *
	 * @param deliveryOrderItems 发货订单项
	 * @param deliveryType 发货方式
     */
    void updateByDeliveries(List<DeliveryOrderItemDTO> deliveryOrderItems, Integer deliveryType);

	/**
	 * 计算未发货商品数量
	 * @param orderId
	 * @return
	 */
	int countUnDeliveryNumByOrderId(Long orderId);

	/**
	 * 减少订单项待发货数量
	 * @param orderItemId
	 * @param count
	 */
    void reduceUnDeliveryNumByOrderItemId(Long orderItemId, Integer count);

	/**
	 * 更新订单项退款状态
	 * @param orderId
	 * @param refundStatus
	 */
	void updateRefundStatusByOrderId(Long orderId, Integer refundStatus);

	/**
	 * 根据订单id查询订单项、退款详情
	 * @param orderId 订单id
	 * @param refundId 退款单号
	 * @return 订单项、退款详情
	 */
	OrderDetailVO listDetailByParam(Long orderId, Long refundId);

	/**
	 * 根据订单项id获取该订单项的商品信息
	 * @param orderItemId
	 * @return
	 */
    OrderItemVO getSpuInfoByOrderItemId(Long orderItemId);

	/**
	 * 根据订单号获取订单项的物流类型
	 * @param orderId 订单号
	 * @return 物流类型
	 */
	boolean getDevTypeByOrderId(Long orderId);

	/**
	 * 获取订单项列表
	 * @param orderItemIds 订单项id
	 * @return  订单项
	 */
	List<OrderItemVO> getOrderItems(List<Long> orderItemIds);

	/**
	 * 获取订单项列表
	 * @param orderIds 订单id
	 * @return  订单项
	 */
	List<OrderItemVO> getOrderItemsByOrderIds(List<Long> orderIds);

	/**
	 * 批量修改订单项的分销金额
	 * @param message 订单项
	 */
    void updateBatchDistributionAmount(List<EsOrderItemBO> message);

    List<OrderItemVO> listOrderItems(OrderItem orderItem);

	/**
	 * 根据订单项id返回订单项信息
	 * @param orderItemIds
	 * @return List<OrderItemVO>
	 */
	List<OrderItemVO> listOrderItemByIds(List<Long> orderItemIds);


	OrderItemVO getOrderItemByOrderNumberAndSkuId(String orderNumber, Long skuId);

	int jointVentureCommissionOrderItemSettled(List<Long> orderIds, Integer jointVentureCommissionStatus, Integer jointVentureRefundStatus);

    int updateDistributionRefundStatusBatchByOrderItemId(List<Long> orderItemIds, Integer distributionRefundStatus);

    int updateDistributionRefundStatusByOrderId(Long orderId, Integer distributionRefundStatus);
}
