package com.mall4j.cloud.discount.mapper;

import com.mall4j.cloud.discount.model.DiscountShop;
import com.mall4j.cloud.discount.vo.DiscountShopVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 满减满折活动 商铺
 *
 * @author FrozenWatermelon
 * @date 2022-03-13 14:58:46
 */
public interface DiscountShopMapper {

	/**
	 * 获取满减满折活动 商铺列表
	 * @return 满减满折活动 商铺列表
	 */
	List<DiscountShop> list();

    /**
     * 根据活动id查询支持的商铺
     * @param discountId
     * @return
     */
    List<DiscountShopVO> selectByDiscountId(@Param("discountId") Long discountId);

	/**
	 * 根据满减满折活动 商铺id获取满减满折活动 商铺
	 *
	 * @param id 满减满折活动 商铺id
	 * @return 满减满折活动 商铺
	 */
	DiscountShop getById(@Param("id") Long id);

	/**
	 * 保存满减满折活动 商铺
	 * @param discountShop 满减满折活动 商铺
	 */
	void save(@Param("discountShop") DiscountShop discountShop);

	/**
	 * 更新满减满折活动 商铺
	 * @param discountShop 满减满折活动 商铺
	 */
	void update(@Param("discountShop") DiscountShop discountShop);

	/**
	 * 根据满减满折活动 商铺id删除满减满折活动 商铺
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

    void insertBatch(@Param("list") List<DiscountShop> list);

    /**
     * 根据满减满折活动 商铺id删除满减满折活动 商铺
     * @param discountId
     */
    void deleteByDiscountId(@Param("discountId") Long discountId);
}
