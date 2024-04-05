package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.dto.SpuTagDTO;
import com.mall4j.cloud.product.model.SpuTag;
import com.mall4j.cloud.product.vo.SpuTagVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品分组表
 *
 * @author lhd
 * @date 2021-02-20 14:28:10
 */
public interface SpuTagService {


	/**
	 * 根据商品分组表id获取商品分组表
	 *
	 * @param id 商品分组表id
	 * @return 商品分组表
	 */
	SpuTagVO getById(Long id);

	/**
	 * 保存商品分组表
	 *
	 * @param spuTag 商品分组表
	 */
	void save(SpuTag spuTag);

	/**
	 * 更新商品分组表
	 *
	 * @param spuTag 商品分组表
	 */
	void update(SpuTag spuTag);

	/**
	 * 根据商品分组表id删除商品分组表
	 *
	 * @param id
	 */
	void deleteById(Long id);

	/**
	 * 根据标签名称分页获取商品分组表列表
	 *
	 * @param pageDTO   分页参数
	 * @param spuTagDTO 筛选参数
	 * @return 商品分组表列表分页数据
	 */
    PageVO<SpuTagVO> pageByTitle(PageDTO pageDTO, SpuTagDTO spuTagDTO);

	/**
	 * 根据标签名称获取商品分组表列表
	 *
	 * @param spuTagDTO 筛选参数
	 * @return 商品分组列表数据
	 */
	List<SpuTagVO> listByTitle(SpuTagDTO spuTagDTO);

	/**
	 * 修改标签商品数量
	 *
	 * @param id 标签信息
	 */
	void updateProdCountById(Long id);

	/**
	 * 批量修改标签商品数量
	 *
	 * @param ids 标签信息
	 */
	void batchUpdateProdCountById(List<Long> ids);

//	/**
//	 * 修改分组商品数量
//	 * @param prodTagId 标签id
//	 * @param count 修改的数量
//	 */
//	void reduceProdCountById(Long prodTagId, Integer count);

	/**
	 * 根据店铺id获取分组信息
	 *
	 * @param shopId 店铺id
	 * @return 分组信息
	 */
    List<SpuTagVO> listByShopId(Long shopId);

	/**
	 * 移除分组换成
	 *
	 * @param shopId 店铺id
	 */
	void removeCacheByShopId(Long shopId);
}
