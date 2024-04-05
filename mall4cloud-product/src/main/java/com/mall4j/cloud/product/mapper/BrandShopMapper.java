package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.api.product.dto.BrandShopDTO;
import com.mall4j.cloud.product.model.BrandShop;
import com.mall4j.cloud.product.vo.BrandShopVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 品牌店铺关联信息
 *
 * @author FrozenWatermelon
 * @date 2021-05-08 11:27:23
 */
public interface BrandShopMapper {

	/**
	 * 获取品牌店铺关联信息列表
	 * @return 品牌店铺关联信息列表
	 */
	List<BrandShop> list();

	/**
	 * 根据品牌店铺关联信息id获取品牌店铺关联信息
	 *
	 * @param brandShopId 品牌店铺关联信息id
	 * @return 品牌店铺关联信息
	 */
	BrandShop getByBrandShopId(@Param("brandShopId") Long brandShopId);

	/**
	 * 保存品牌店铺关联信息
	 * @param brandShop 品牌店铺关联信息
	 */
	void save(@Param("brandShop") BrandShop brandShop);

	/**
	 * 更新品牌店铺关联信息
	 * @param brandShop 品牌店铺关联信息
	 */
	void update(@Param("brandShop") BrandShop brandShop);

	/**
	 * 根据品牌店铺关联信息id删除品牌店铺关联信息
	 * @param brandShopId
	 */
	void deleteById(@Param("brandShopId") Long brandShopId);

	/**
	 * 根据店铺id删除品牌店铺关联信息
	 * @param shopId
	 */
    void deleteBatchByShopId(@Param("shopId") Long shopId);

	/**
	 * 根据品牌类型批量插入品牌店铺关联信息
	 * @param shopId 店铺id
	 * @param brandShopDTOList 品牌信息列表
	 * @param type 签约类型
	 */
	void insertBatch(@Param("shopId") Long shopId, @Param("brandShopList") List<BrandShopDTO> brandShopDTOList, @Param("type") Integer type);

	/**
	 * 根据店铺id获取该店铺已签约的品牌信息
	 * @param shopId 店铺id
	 * @param type 品牌类型
	 * @param lang 当前语言
	 * @return 品牌信息列表
	 */
    List<BrandShopVO> listByShopIdAndType(@Param("shopId") Long shopId, @Param("type") Integer type, @Param("lang") Integer lang);

	/**
	 * 根据店铺id更新品牌签约类型
	 * @param shopId
	 * @param type
	 */
	void updateTypeByShopId(@Param("shopId") Long shopId, @Param("type") Integer type);

	/**
	 * 根据品牌id删除店铺品牌关联关系
	 * @param brandId
	 */
    void deleteByBrandId(@Param("brandId") Long brandId);

	/**
	 * 根据店铺id与品牌id查询签约数量
	 * @param shopId
	 * @param brandId
	 * @return
	 */
    int countByShopIdAndBrandId(@Param("shopId") Long shopId, @Param("brandId") Long brandId);
}
