package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.product.dto.SpuDTO;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.product.dto.SpuTagReferenceDTO;
import com.mall4j.cloud.product.model.SpuTag;
import com.mall4j.cloud.product.model.SpuTagReference;
import com.mall4j.cloud.product.vo.SpuPageVO;
import com.mall4j.cloud.product.vo.SpuTagReferenceVO;

import java.util.List;

/**
 * 商品分组标签关联信息
 *
 * @author lhd
 * @date 2021-02-20 14:28:10
 */
public interface SpuTagReferenceService {

	/**
	 * 分页获取商品分组标签关联信息列表
	 *
	 * @param pageDTO 分页参数
	 * @return 商品分组标签关联信息列表分页数据
	 */
	PageVO<SpuTagReferenceVO> page(PageDTO pageDTO);

	/**
	 * 根据商品分组标签关联信息id获取商品分组标签关联信息
	 *
	 * @param referenceId 商品分组标签关联信息id
	 * @return 商品分组标签关联信息
	 */
	SpuTagReferenceVO getByReferenceId(Long referenceId);

	/**
	 * 保存商品分组标签关联信息
	 *
	 * @param spuTagReference 商品分组标签关联信息
	 */
	void save(SpuTagReference spuTagReference);

	/**
	 * 更新商品分组标签关联信息
	 *
	 * @param spuTagReference 商品分组标签关联信息
	 */
	void update(SpuTagReference spuTagReference);

	/**
	 * 根据商品分组标签关联信息id删除商品分组标签关联信息
	 *
	 * @param referenceId
	 */
	void deleteById(Long referenceId);

	/**
	 * 根据状态和分组id返回分组下的商品数量
	 *
	 * @param status 状态
	 * @param id     分组id
	 * @return 返回商品数量
	 */
    int countByStatusAndTagId(Integer status, Long id);

	/**
	 * 保存商品标签关联信息，修改标签信息
	 *
	 * @param spuTag  标签信息
	 * @param spuList 标签商品关联信息
	 */
	void addProdForTag(SpuTag spuTag, List<SpuDTO> spuList);

	/**
	 * 根据ids获取商品标签关联信息列表
	 *
	 * @param ids 商品id
	 * @return 返回标签关联信息
	 */
	List<SpuTagReferenceVO> listByIds(List<Long> ids);

	/**
	 * 修改标签内商品排序
	 *
	 * @param spuTagReferences 商品排序信息
	 */
	void updateProdSeq(List<SpuTagReferenceDTO> spuTagReferences);

	/**
	 * 移除商品和商品标签的关联
	 *
	 * @param prodTagId 商品标签id
	 * @param spuIds    商品ids
	 */
	void removeByProdId(Long prodTagId, List<Long> spuIds);

	/**
	 * 获取商品所属的分组列表
	 *
	 * @param spuId
	 * @return
	 */
    List<Long> tagListBySpuId(Long spuId);

	/**
	 * 删除spu关联分组的数据
	 *
	 * @param spuIds
	 */
	void deleteSpuData(List<Long> spuIds);

	/**
	 * 根据分类，删除spu关联分组的数据
	 * @param shopId
	 * @param categoryIds
	 */
    void deleteSpuTagByShopIdAndCategoryIds(Long shopId, List<Long> categoryIds);

	/**
	 * 根据分组id，获取分组下的商品id列表
	 *
	 * @param tagId 分组id
	 * @return 商品id列表
	 */
	List<Long> spuIdsByTagId(Long tagId);

	/**
	 * 根据分组id获取商品列表
	 * @param pageDTO 分页信息
	 * @param spuTagReferenceDTO 筛选条件
	 * @return 商品分页信息
	 */
	PageVO<SpuPageVO> pageSpuListByTagId(PageDTO pageDTO, SpuTagReferenceDTO spuTagReferenceDTO);
}
