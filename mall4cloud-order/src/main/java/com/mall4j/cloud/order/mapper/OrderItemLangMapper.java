package com.mall4j.cloud.order.mapper;

import com.mall4j.cloud.order.model.OrderItemLang;
import com.mall4j.cloud.common.order.vo.OrderItemLangVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单项-国际化
 *
 * @author YXF
 * @date 2021-05-17 10:26:54
 */
public interface OrderItemLangMapper {

	/**
	 * 获取订单项-国际化列表
	 *
	 * @return 订单项-国际化列表
	 */
	List<OrderItemLang> list();

	/**
	 * 根据订单项-国际化id获取订单项-国际化
	 *
	 * @param orderItemId 订单项-国际化id
	 * @return 订单项-国际化
	 */
	OrderItemLang getByOrderItemId(@Param("orderItemId") Long orderItemId);

	/**
	 * 保存订单项-国际化
	 *
	 * @param orderItemLang 订单项-国际化
	 */
	void save(@Param("orderItemLang") OrderItemLang orderItemLang);

	/**
	 * 更新订单项-国际化
	 *
	 * @param orderItemLang 订单项-国际化
	 */
	void update(@Param("orderItemLang") OrderItemLang orderItemLang);

	/**
	 * 根据订单项-国际化id删除订单项-国际化
	 *
	 * @param orderItemId
	 */
	void deleteById(@Param("orderItemId") Long orderItemId);

	/**
	 * 批量保存订单项国际化信息
	 *
	 * @param orderItemLangList
	 */
	void saveBatch(@Param("orderItemLangList") List<OrderItemLang> orderItemLangList);

	/**
	 * 获取订单项国际化信息
	 * @param orderIdList 订单id列表
	 * @return 订单项国际化数据列表
	 */
    List<OrderItemLangVO> getLangListByOrderIds(@Param("orderIdList") long[] orderIdList);

	/**
	 * 根据订单项id列表获取订单项国际化信息列表
	 * @param orderItemIds
	 * @param lang
	 * @return
	 */
    List<OrderItemLangVO> listOrderItemLangByIds(@Param("orderItemIds") List<Long> orderItemIds, @Param("lang") Integer lang);
}
