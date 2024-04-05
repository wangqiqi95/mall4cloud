package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.bo.EsBrandBO;
import com.mall4j.cloud.common.product.bo.EsProductBO;
import com.mall4j.cloud.common.product.vo.BrandVO;
import com.mall4j.cloud.common.product.vo.app.BrandAppVO;
import com.mall4j.cloud.product.dto.BrandDTO;
import com.mall4j.cloud.product.model.BrandLang;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 品牌信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface BrandService {

	void dbMasterTest();

	void dbSlaveTest();

	/**
	 * 分页获取品牌信息列表
	 * @param pageDTO 分页参数
	 * @param brandDTO
	 * @return 品牌信息列表分页数据
	 */
	PageVO<BrandVO> page(PageDTO pageDTO, BrandDTO brandDTO);

	/**
	 * 根据品牌信息id获取品牌信息
	 *
	 * @param brandId 品牌信息id
	 * @return 品牌信息
	 */
	BrandVO getByBrandId(Long brandId);

	/**
	 * 根据品牌信息id获取品牌信息
	 *
	 * @param brandId 品牌信息id
	 * @return 品牌信息
	 */
	BrandVO getInfo(Long brandId);

	/**
	 * 保存品牌信息
	 * @param brandDTO 品牌信息
	 */
	void save(BrandDTO brandDTO);

	/**
	 * 更新品牌信息
	 * @param brandDTO 品牌信息
	 */
	void update(BrandDTO brandDTO);

	/**
	 * 根据品牌id，删除品牌
	 * @param brandId
	 */
	void deleteById(Long brandId);

	/**
	 * 更新品牌状态（启用或禁用）
	 * @param brandDTO
	 * @return
	 */
	void updateBrandStatus(BrandDTO brandDTO);

	/**
	 * 根据分类id，获取品牌列表
	 * @param categoryId 分类id
	 * @param lang 语言
	 * @return 品牌id
	 */
    List<BrandAppVO> listByCategory(Long categoryId, Integer lang);

	/**
	 * 获取置顶品牌列表
	 * @param lang 语言
	 * @return 品牌列表
	 */
	List<BrandAppVO> topBrandList(Integer lang);

	/**
	 * 清除分类缓存
	 *
	 * @param categoryIds
	 */
	void removeCache(List<Long> categoryIds);

	/**
	 * 更新品牌的商品数量
	 * @param brandId
	 */
	void updateSpuCount(Long brandId);

	/**
	 * 批量更新品牌的商品数量
	 * @param brandIds
	 */
	void updateSpuCountByBrandIds(Collection<Long> brandIds);

	/**
	 * 根据品牌名，获取品牌列表
	 * @param brandNames 品牌名称列表
	 * @param shopId 店铺id
	 * @return 品牌列表
	 */
	List<BrandLang> listBrandLangByBrandNames(Set<String> brandNames, Long shopId);

	/**
	 * 获取前端品牌分页列表
	 * @param pageDTO
	 * @param brandDTO
	 * @return
	 */
	PageVO<BrandAppVO> appPage(PageDTO pageDTO, BrandDTO brandDTO);


	/**
	 * 根据品牌名称获取品牌列表
	 * @param name
	 * @return
	 */
	List<BrandVO> listByName(String name);

	/**
	 * 根据店铺id把自定义品牌更新为平台品牌
	 * @param shopId
	 */
	void updateCustomBrandToPlatformBrandByShopId(Long shopId);

	/**
	 * 根据分类id与品牌名称获取分类下的品牌与店铺签约的品牌
	 * @param categoryId
	 * @param brandName
	 * @param shopId
	 * @return
	 */
    List<BrandVO> listAvailableBrandByCategoryIdAndBrandNameAndShopId(Long categoryId, String brandName, Long shopId);

	/**
	 * 获取品牌信息
	 *
	 * @param esProductBO 商品信息
	 * @return 品牌信息
	 */
	EsBrandBO getEsBrandBO(EsProductBO esProductBO);

	/**
	 * 根据商品id列表，获取品牌id列表
	 * @param spuIds 商品id列表
	 * @return 品牌id列表
	 */
	List<Long> listBrandIdBySpuIds(List<Long> spuIds);

	/**
	 * 批量获取品牌信息
	 * @param brandDTO
	 * @return
	 */
    List<BrandVO> listByParams(BrandDTO brandDTO);

	/**
	 * 更新指定商品绑定的品牌商品数量信息
	 * @param spuIds
	 */
	void updateSpuCountBySpuIds(List<Long> spuIds);
}
