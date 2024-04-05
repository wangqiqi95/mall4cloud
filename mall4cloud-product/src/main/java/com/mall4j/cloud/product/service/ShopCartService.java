package com.mall4j.cloud.product.service;

import com.mall4j.cloud.product.dto.shopcart.ChangeShopCartItemDTO;
import com.mall4j.cloud.product.dto.shopcart.CheckShopCartItemDTO;
import com.mall4j.cloud.product.model.ShopCartItem;
import com.mall4j.cloud.common.order.vo.ShopCartItemVO;

import java.util.List;

/**
 * 购物车
 *
 * @author FrozenWatermelon
 * @date 2020-11-20 15:47:32
 */
public interface ShopCartService {

	/**
	 * 根据购物车项id删除购物车
	 * @param shopCartItemIds 购物车项id
	 * @param userId 用户id
	 */
	void deleteShopCartItemsByShopCartItemIds(Long userId,List<Long> shopCartItemIds);

	/**
	 * 添加购物项
     * @param userId 用户id
     * @param param 购物项
     * @param priceFee 加入购物车时候的价格
     * @param categoryId 分类id
     */
	void addShopCartItem(Long userId, ChangeShopCartItemDTO param, Long priceFee, Long categoryId);

	/**
	 * 更新购物项
	 * @param shopCartItem 购物项
	 * @param userId 用户id
	 */
	void updateShopCartItem(Long userId, ShopCartItem shopCartItem);

	/**
	 * 清空用户购物车
	 * @param userId 用户id
	 */
	void deleteAllShopCartItems(Long userId);

	/**
	 * 获取用户所有未过期的购物项
	 * @return 未过期的购物项
	 * @param storeId
	 */
	List<ShopCartItemVO> getShopCartItems(Long storeId);

	/**
	 * 获取已过期的购物项
	 * @return 已过期的购物项
	 * @param storeId
	 */
	List<ShopCartItemVO> getShopCartExpiryItems(Long storeId);

	/**
	 * 购物项数量，有缓存
	 * @param userId 用户id
	 * @return 购物项数量
	 */
	Integer getShopCartItemCount(Long userId);

	/**
	 * 获取被选中的购物车项
	 * @return 被选中的购物车项
     * @param storeId
	 */
	List<ShopCartItemVO> getCheckedShopCartItems(Long storeId);


	/**
	 * 商品下架 or 变成预售的时候 移除购物车商品数量缓存
	 * @param spuId
	 */
	void removeShopCartItemCache(Long spuId);

	/**
	 * 勾选购物车状态
	 * @param userId 用户id
	 * @param params 参数
	 */
    void checkShopCartItems(Long userId, List<CheckShopCartItemDTO> params);

	/**
	 * 根据店铺id列表更新店铺是否停业状态
	 * @param shopIds
	 * @param isClosed
	 */
	void updateIsClosedByShopIds(List<Long> shopIds, Integer isClosed);
}
