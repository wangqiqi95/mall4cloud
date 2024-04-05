package com.mall4j.cloud.discount.mapper;

import com.mall4j.cloud.discount.vo.DiscountVO;
import com.mall4j.cloud.common.order.vo.DiscountOrderVO;
import com.mall4j.cloud.discount.dto.DiscountDTO;
import com.mall4j.cloud.discount.model.Discount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 满减满折优惠
 *
 * @author FrozenWatermelon
 * @date 2020-12-10 13:43:38
 */
public interface DiscountMapper {

	/**
	 * 获取满减满折优惠列表
	 *
	 * @param transportDTO
	 * @return 满减满折优惠列表
	 */
	List<DiscountVO> list(@Param("transportDTO") DiscountDTO transportDTO);

	/**
	 * 满减活动信息
	 *
	 * @param discountId 满减满折优惠id
	 * @param status     满减满折状态
	 * @return 满减满折优惠
	 */
	DiscountVO getDiscountAndSpu(@Param("discountId") Long discountId, @Param("status") Integer status);

	/**
	 * 保存满减满折优惠
	 *
	 * @param discount 满减满折优惠
	 */
	void save(@Param("discount") DiscountDTO discount);

	/**
	 * 更新满减满折优惠
	 *
	 * @param discount 满减满折优惠
	 */
	void update(@Param("discount") DiscountDTO discount);

	/**
	 * 根据满减满折优惠id删除满减满折优惠
	 *
	 * @param discountId
	 */
	void deleteById(@Param("discountId") Long discountId);

	/**
	 * 根据店铺id获取活动信息
	 *
	 * @param shopId 店铺id
	 * @return 活动信息列表
	 */
	List<DiscountOrderVO> getDiscountsAndItemsByShopId(@Param("shopId") Long shopId);

	/**
	 * 根据活动id，删除活动
	 *
	 * @param id     活动id
	 * @param shopId 店铺id
	 * @return 标识
	 */
	int deleteDiscounts(@Param("id") Long id, @Param("shopId") Long shopId);

	/**
	 * 根据商品id获取正在进行的活动信息
	 *
	 * @param shopId 店铺id
	 * @param spuId  商品id
	 * @return 正在进行的活动列表
	 */
	List<DiscountVO> spuDiscountList(@Param("shopId") Long shopId, @Param("spuId") Long spuId);

	/**
	 * 查询活动列表
	 *
	 * @param discount 活动查询参数
	 * @return 活动列表
	 */
	List<Discount> getPlatformDiscountPage(@Param("discount") Discount discount);

	/**
	 * 更新活动状态
	 *
	 * @param discountId 活动id
	 * @param status     更新后的状态
	 * @return 标记
	 */
	int updateStatusByDiscountId(@Param("discountId") Long discountId, @Param("status") Integer status);

	/**
	 * 获取所有正在进行的活动
	 *
	 * @return 正在进行的活动
	 */
	List<DiscountVO> getDiscountList();

    /**
     * 获取所有正在进行的活动
     *
     * @return 正在进行的活动
     */
    List<DiscountVO> getDiscountListByStoreId(@Param("shopId")Long shopId);

	/**
	 * 通过活动id获取促销活动详情
	 *
	 * @param discountId 活动id
	 * @return 促销活动详情
	 */
	Discount getDiscountByDiscountId(@Param("discountId") Long discountId);

	/**
	 * 关闭已经结束的满减折活动
	 */
    void closeDiscountBySetStatus();

	/**
	 * 根据商品id列表，获取满减活动id列表
	 *
	 * @param spuIds 商品id列表
	 * @param shopIds 店铺id列表
	 * @return 满减活动id列表
	 */
	List<DiscountVO> discountIdsBySpuIds(@Param("spuIds") List<Long> spuIds, @Param("shopIds") List<Long> shopIds);

	/**
	 * 根据活动id列表，批量下线活动
	 *
	 * @param discountIds
	 */
    void batchOfflineByDiscountIdsAndStatus(@Param("discountIds") List<Long> discountIds);
}
