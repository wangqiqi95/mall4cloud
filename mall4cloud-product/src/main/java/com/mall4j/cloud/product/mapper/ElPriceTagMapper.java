package com.mall4j.cloud.product.mapper;

import com.mall4j.cloud.product.model.ElPriceTag;
import com.mall4j.cloud.product.vo.ElPriceTagVO;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 电子价签管理
 *
 * @author FrozenWatermelon
 * @date 2022-03-27 22:23:15
 */
public interface ElPriceTagMapper extends BaseMapper<ElPriceTag> {

	/**
	 * 获取电子价签管理列表
	 * @return 电子价签管理列表
	 */
	List<ElPriceTagVO> getList(@Param("name") String name);


	/**
	 * 保存电子价签管理
	 * @param elPriceTag 电子价签管理
	 */
	void save(@Param("elPriceTag") ElPriceTag elPriceTag);

	/**
	 * 更新电子价签管理
	 * @param elPriceTag 电子价签管理
	 */
	void update(@Param("elPriceTag") ElPriceTag elPriceTag);

	/**
	 * 根据电子价签管理id删除电子价签管理
	 * @param id
	 */
	void deleteById(@Param("id") String id);
	void deleteByIds(@Param("ids") List<String> ids);
}
