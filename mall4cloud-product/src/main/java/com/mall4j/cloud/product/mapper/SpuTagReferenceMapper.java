package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.product.dto.SpuTagReferenceDTO;
import com.mall4j.cloud.product.model.SpuTagReference;
import com.mall4j.cloud.product.vo.SpuTagReferenceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品分组标签关联信息
 *
 * @author lhd
 * @date 2021-02-20 14:28:10
 */
public interface SpuTagReferenceMapper {

	/**
	 * 获取商品分组标签关联信息列表
	 *
	 * @return 商品分组标签关联信息列表
	 */
	List<SpuTagReferenceVO> list();

	/**
	 * 根据商品分组标签关联信息id获取商品分组标签关联信息
	 *
	 * @param referenceId 商品分组标签关联信息id
	 * @return 商品分组标签关联信息
	 */
	SpuTagReferenceVO getByReferenceId(@Param("referenceId") Long referenceId);

	/**
	 * 保存商品分组标签关联信息
	 *
	 * @param spuTagReference 商品分组标签关联信息
	 */
	void save(@Param("spuTagReference") SpuTagReference spuTagReference);

	/**
	 * 更新商品分组标签关联信息
	 *
	 * @param spuTagReference 商品分组标签关联信息
	 */
	void update(@Param("spuTagReference") SpuTagReference spuTagReference);

	/**
	 * 根据商品分组标签关联信息id删除商品分组标签关联信息
	 *
	 * @param referenceId
	 */
	void deleteById(@Param("referenceId") Long referenceId);

	/**
	 * 根据状态和分组id返回分组下的商品数量
	 *
	 * @param status 状态
	 * @param id     分组id
	 * @return 返回商品数量
	 */
    int countByStatusAndTagId(@Param("status") Integer status, @Param("id") Long id);

	/**
	 * 批量保存关联信息
	 *
	 * @param spuTagReferences 商品标签关联列表
	 */
	void saveBatch(@Param("spuTagReferences") List<SpuTagReference> spuTagReferences);

	/**
	 * 根据ids获取商品标签关联信息列表
	 *
	 * @param ids 商品id
	 * @return 返回标签关联信息
	 */
	List<SpuTagReferenceVO> listByIds(@Param("ids") List<Long> ids);

	/**
	 * 修改标签内商品排序
	 *
	 * @param spuTagReferences 商品排序信息
	 */
	void updateSpuSeq(@Param("spuTagReferences") List<SpuTagReference> spuTagReferences);

	/**
	 * 根据ids删除对应的关联信息
	 *
	 * @param prodTagId 商品分组id
	 * @param ids 关联ids
	 */
	void removeByIds(@Param("prodTagId") Long prodTagId, @Param("ids") List<Long> ids);

	/**
	 * 获取商品所属的分组列表
	 *
	 * @param spuId
	 * @return
	 */
    List<Long> tagListBySpuId(@Param("spuId") Long spuId);

	/**
	 * 删除spu关联分组的数据
	 *
	 * @param spuIds
	 */
	void deleteSpuData(@Param("spuIds") List<Long> spuIds);

	/**
	 * 根据分类，删除spu关联分组的数据
	 *
	 * @param shopId
	 * @param categoryIds
	 */
	void deleteSpuTagByShopIdAndCategoryIds(@Param("shopId") Long shopId, @Param("categoryIds") List<Long> categoryIds);

	/**
	 * 根据spuId获取分组id列表
	 *
	 * @param spuIds 商品id列表
	 * @return 分组id列表
	 */
	List<Long> listSpuTagIdBySpuIds(@Param("spuIds") List<Long> spuIds);

	/**
	 * 根据店铺id及分类id列表，获取分组id列表
	 *
	 * @param shopId
	 * @param categoryIds
	 * @return
	 */
	List<Long> getTagIdsByShopIdAndCategoryIds(@Param("shopId") Long shopId, @Param("categoryIds") List<Long> categoryIds);

	/**
	 * 根据分组id，获取分组下的商品id列表
	 *
	 * @param tagId 分组id
	 * @return 商品id列表
	 */
    List<Long> spuIdsByTagId(@Param("tagId") Long tagId);

	/**
	 * 获取spuId列表
	 *
	 * @param spuTagReferenceDTO
	 * @return
	 */
	List<SpuTagReference> getSpuIds(@Param("spuTagReference") SpuTagReferenceDTO spuTagReferenceDTO);

}
