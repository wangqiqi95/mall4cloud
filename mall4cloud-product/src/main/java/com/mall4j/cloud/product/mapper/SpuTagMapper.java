package com.mall4j.cloud.product.mapper;

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
public interface SpuTagMapper {

	/**
	 * 根据标签名称分页获取商品分组表列表
	 *
	 * @param spuTagDTO 筛选参数
	 * @return 商品分组表列表分页数据
	 */
	List<SpuTagVO> list(@Param("spuTag") SpuTagDTO spuTagDTO);

	/**
	 * 根据商品分组表id获取商品分组表
	 *
	 * @param id 商品分组表id
	 * @return 商品分组表
	 */
	SpuTagVO getById(@Param("id") Long id);

	/**
	 * 保存商品分组表
	 *
	 * @param spuTag 商品分组表
	 */
	void save(@Param("spuTag") SpuTag spuTag);

	/**
	 * 更新商品分组表
	 *
	 * @param spuTag 商品分组表
	 */
	void update(@Param("spuTag") SpuTag spuTag);

	/**
	 * 根据商品分组表id删除商品分组表
	 *
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 修改标签商品数量
	 *
	 * @param id 标签信息
	 */
	void updateProdCountById(@Param("id") Long id);

	/**
	 * 批量修改标签商品数量
	 *
	 * @param ids 标签信息
	 */
	void batchUpdateProdCountById(@Param("ids") List<Long> ids);

//	/**
//	 * 修改分组商品数量
//	 * @param prodTagId
//	 * @param size
//	 */
//	void reduceProdCountById(@Param("prodTagId") Long prodTagId, @Param("count") Integer size);

	/**
	 * 根据店铺id获取分组信息
	 *
	 * @param shopId 店铺id
	 * @return 分组信息
	 */
    List<SpuTagVO> listByShopId(@Param("shopId") Long shopId);

}
