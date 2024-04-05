package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.common.database.util.PageAdapter;
import com.mall4j.cloud.common.product.vo.app.BrandAppVO;
import com.mall4j.cloud.product.dto.BrandDTO;
import com.mall4j.cloud.api.product.dto.BrandShopDTO;
import com.mall4j.cloud.product.model.Brand;
import com.mall4j.cloud.common.product.vo.BrandVO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 品牌信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public interface BrandMapper {

	/**
	 * 获取品牌信息列表
	 *
	 * @param pageAdapter 分页信息
	 * @param brandDTO    品牌信息
	 * @return 品牌信息列表
	 */
	List<BrandVO> list(@Param("page") PageAdapter pageAdapter, @Param("brandDTO") BrandDTO brandDTO);

	/**
	 * 获取品牌数量
	 *
	 * @param brandDTO 品牌信息
	 * @return 品牌信息列表
	 */
	Long listTotal(@Param("brandDTO") BrandDTO brandDTO);

	/**
	 * 根据品牌信息id获取品牌信息
	 *
	 * @param brandId 品牌信息id
	 * @return 品牌信息
	 */
	BrandVO getByBrandId(@Param("brandId") Long brandId);

	/**
	 * 保存品牌信息
	 *
	 * @param brand 品牌信息
	 */
	void save(@Param("brand") Brand brand);

	/**
	 * 更新品牌信息
	 *
	 * @param brand 品牌信息
	 */
	void update(@Param("brand") Brand brand);

	/**
	 * 根据品牌信息id删除品牌信息
	 *
	 * @param brandId
	 */
	void deleteById(@Param("brandId") Long brandId);

	/**
	 * 获取品牌在商品中使用的数量
	 *
	 * @param brandId
	 * @param status
	 * @return 使用该品牌的商品数量
	 */
	int getUseNum(@Param("brandId") Long brandId, @Param("status") Integer status);

	/**
	 * 更新品牌状态（启用或禁用）
	 *
	 * @param brandDTO
	 */
	void updateBrandStatus(@Param("brand") BrandDTO brandDTO);

	/**
	 * 根据分类id，获取品牌列表(分类中的推荐品牌)
	 *
	 * @param categoryId
	 * @return
	 */
    List<BrandAppVO> listByCategory(@Param("categoryId") Long categoryId);

	/**
	 * 批量更新品牌的商品数量
	 *
	 * @param brandIds
	 */
    void batchUpdateSpuCount(@Param("brandIds") Collection<Long> brandIds);

	/**
	 * 获取前端品牌分页信息
	 *
	 * @param pageAdapter 分页信息
	 * @param brandDTO    品牌列表筛选条件
	 * @return 品牌列表
	 */
    List<BrandAppVO> brandAppPage(@Param("page") PageAdapter pageAdapter, @Param("brand") BrandDTO brandDTO);

	/**
	 * 获取前端品牌总数
	 *
	 * @param brandDTO 品牌总数筛选条件
	 * @return 品牌总数
	 */
	Long brandAppTotal(@Param("brand") BrandDTO brandDTO);

	/**
	 * 获取前端品牌列表
	 *
	 * @param brandDTO 品牌列表筛选条件
	 * @param limit    品牌列表数量
	 * @return 品牌列表
	 */
    List<BrandAppVO> appList(@Param("brand") BrandDTO brandDTO, @Param("limit") Integer limit);


	/**
	 * 根据品牌名称获取品牌列表
	 *
	 * @param name 品牌名称
	 * @param lang 语言
	 * @return 品牌列表
	 */
	List<BrandVO> listByName(@Param("name") String name, @Param("lang") Integer lang);

	/**
	 * 根据品牌id列表获取品牌列表
	 *
	 * @param brandIdList
	 * @return
	 */
    List<BrandVO> listByIds(@Param("brandIdList") List<Long> brandIdList);

	/**
	 * 根据店铺id删除该店铺下的品牌信息
	 *
	 * @param shopId
	 */
	void deleteBatchByShopId(@Param("shopId") Long shopId);

	/**
	 * 批量插入品牌自定义品牌
	 *
	 * @param brandList
	 */
	void insertBatchByBrandShopList(@Param("brandList") List<BrandShopDTO> brandList);

	/**
	 * 根据店铺id获取店铺下的品牌列表
	 *
	 * @param shopId
	 * @return
	 */
	List<BrandVO> listByShopId(@Param("shopId") Long shopId);

	/**
	 * 根据店铺id更新该店铺下的品牌的店铺id和品牌状态
	 *
	 * @param oldShopId
	 * @param status
	 * @param newShopId
	 */
	void updateShopIdAndStatusByShopId(@Param("oldShopId") Long oldShopId, @Param("status") Integer status, @Param("newShopId") long newShopId);

	/**
	 * 根据店铺id与品牌名称,分类id获取已经签约的品牌列表
	 *
	 * @param shopId
	 * @param brandName
	 * @param categoryId
	 * @return
	 */
    List<BrandVO> listSigningByShopIdAndBrandNameAndCategoryId(@Param("shopId") Long shopId, @Param("brandName") String brandName, @Param("categoryId") Long categoryId);

	/**
	 * 根据商品id列表，获取品牌id列表
	 *
	 * @param spuIds 商品id列表
	 * @return 品牌id列表
	 */
	List<Long> listBrandIdBySpuIds(@Param("spuIds") List<Long> spuIds);

	/**
	 * 根据参数获取品牌列表
	 *
	 * @param brandDTO
	 * @return
	 */
    List<BrandVO> listByParams(@Param("brandDTO") BrandDTO brandDTO);
}
