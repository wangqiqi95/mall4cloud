package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.MallFxOrderCommssion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销订单佣金表
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:30
 */
public interface MallFxOrderCommssionMapper {

	/**
	 * 获取分销订单佣金表列表
	 * @return 分销订单佣金表列表
	 */
	List<MallFxOrderCommssion> list();

	/**
	 * 根据分销订单佣金表id获取分销订单佣金表
	 *
	 * @param id 分销订单佣金表id
	 * @return 分销订单佣金表
	 */
	MallFxOrderCommssion getById(@Param("id") Long id);

	/**
	 * 保存分销订单佣金表
	 * @param mallFxOrderCommssion 分销订单佣金表
	 */
	void save(@Param("mallFxOrderCommssion") MallFxOrderCommssion mallFxOrderCommssion);

	/**
	 * 更新分销订单佣金表
	 * @param mallFxOrderCommssion 分销订单佣金表
	 */
	void update(@Param("mallFxOrderCommssion") MallFxOrderCommssion mallFxOrderCommssion);

	/**
	 * 根据分销订单佣金表id删除分销订单佣金表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
