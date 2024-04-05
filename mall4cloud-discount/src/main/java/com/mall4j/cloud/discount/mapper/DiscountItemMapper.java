package com.mall4j.cloud.discount.mapper;

import com.mall4j.cloud.discount.dto.DiscountItemDTO;
import com.mall4j.cloud.discount.model.DiscountSpu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 满减满折优惠项
 *
 * @author FrozenWatermelon
 * @date 2020-12-10 13:43:39
 */
public interface DiscountItemMapper {

	/**
	 * 根据满减满折id删除满减满折优惠项
	 * @param discountId
	 */
	void deleteByDiscountId(@Param("discountId") Long discountId);

	/**
	 * 批量插入满减项
	 * @param discountItems 满减项列表
	 */
	void insertDiscountItems(@Param("discountItems") List<DiscountItemDTO> discountItems);

	/**
	 * 查询出所有为可用商品类型的满减活动的，包含需要处理商品id，进行删除
	 * @param spuIds 商品ids
	 * @return 在活动中的商品
	 */
	List<DiscountSpu> listDiscountBySpuIds(@Param("spuIds") List<Long> spuIds);
}
