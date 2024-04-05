package com.mall4j.cloud.product.service;

import com.mall4j.cloud.api.product.dto.BrandShopDTO;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.dto.BrandSigningDTO;
import com.mall4j.cloud.product.model.BrandShop;
import com.mall4j.cloud.product.vo.BrandSigningVO;

import java.util.List;

/**
 * 品牌店铺关联信息
 *
 * @author FrozenWatermelon
 * @date 2021-04-30 13:21:10
 */
public interface BrandShopService {

	/**
	 * 分页获取品牌店铺关联信息列表
	 * @param pageDTO 分页参数
	 * @return 品牌店铺关联信息列表分页数据
	 */
	PageVO<BrandShop> page(PageDTO pageDTO);

	/**
	 * 根据品牌店铺关联信息id获取品牌店铺关联信息
	 *
	 * @param brandShopId 品牌店铺关联信息id
	 * @return 品牌店铺关联信息
	 */
	BrandShop getByBrandShopId(Long brandShopId);

	/**
	 * 保存品牌店铺关联信息
	 * @param brandShop 品牌店铺关联信息
	 */
	void save(BrandShop brandShop);

	/**
	 * 更新品牌店铺关联信息
	 * @param brandShop 品牌店铺关联信息
	 */
	void update(BrandShop brandShop);

	/**
	 * 根据品牌店铺关联信息id删除品牌店铺关联信息
	 * @param brandShopId 品牌店铺关联信息id
	 */
	void deleteById(Long brandShopId);

	/**
	 * 根据店铺id获取品牌签约信息
	 * @param shopId 店铺ID
	 * @return 品牌签约信息列表
	 */
	BrandSigningVO listSigningByShopId(Long shopId);

	/**
	 * 根据店铺id签约品牌信息
	 * @param brandSigningDTO
	 * @param shopId
	 */
    void signingBrands(BrandSigningDTO brandSigningDTO, Long shopId);

	/**
	 * 根据店铺id更新店铺品牌签约类型
	 * @param shopId
	 * @param type
	 */
	void updateTypeByShopId(Long shopId, Integer type);

	/**
	 * 根据品牌id删除店铺品牌关联关系
	 * @param brandId
	 */
    void deleteByBrandId(Long brandId);

	/**
	 * 根据店铺id批量保存品牌签约信息
	 * @param brandShopDTOList
	 * @param shopId
	 */
	void insertBatchByShopId(List<BrandShopDTO> brandShopDTOList, Long shopId);

	/**
	 * 根据店铺id与品牌id查询签约数量
	 * @param shopId
	 * @param brandId
	 * @return
	 */
    int countByShopIdAndBrandId(Long shopId, Long brandId);
}
