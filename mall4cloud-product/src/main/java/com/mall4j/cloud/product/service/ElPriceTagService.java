package com.mall4j.cloud.product.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.product.model.ElPriceTag;
import com.mall4j.cloud.product.vo.ElPriceProdVO;
import com.mall4j.cloud.product.vo.ElPriceTagVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 电子价签管理
 *
 * @author FrozenWatermelon
 * @date 2022-03-27 22:23:15
 */
public interface ElPriceTagService {

	/**
	 * 分页获取电子价签管理列表
	 * @param pageDTO 分页参数
	 * @return 电子价签管理列表分页数据
	 */
	PageVO<ElPriceTagVO> page(PageDTO pageDTO,String name);

	/**
	 * 价签商品分页列表
	 * @param pageDTO
	 * @return
	 */
	PageVO<ElPriceProdVO> prodPage(PageDTO pageDTO,String elId, String name);

	/**
	 * 根据电子价签管理id获取电子价签管理
	 *
	 * @param id 电子价签管理id
	 * @return 电子价签管理
	 */
	ElPriceTagVO getElPriceTagVOById(String id);

	/**
	 * 保存电子价签管理
	 * @param elPriceTag 电子价签管理
	 */
	void save(ElPriceTag elPriceTag);

	/**
	 * 更新电子价签管理
	 * @param elPriceTag 电子价签管理
	 */
	void update(ElPriceTag elPriceTag);

	/**
	 * 根据电子价签管理id删除电子价签管理
	 * @param id 电子价签管理id
	 */
	void deleteById(String id);
	void deleteByIds(List<String> ids);
}
