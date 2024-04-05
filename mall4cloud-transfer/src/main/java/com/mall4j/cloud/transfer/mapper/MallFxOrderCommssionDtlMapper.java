package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.MallFxOrderCommssionDtl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销订单佣金详情表
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:31
 */
public interface MallFxOrderCommssionDtlMapper {

	/**
	 * 获取分销订单佣金详情表列表
	 * @return 分销订单佣金详情表列表
	 */
	List<MallFxOrderCommssionDtl> list();

	/**
	 * 根据分销订单佣金详情表id获取分销订单佣金详情表
	 *
	 * @param id 分销订单佣金详情表id
	 * @return 分销订单佣金详情表
	 */
	MallFxOrderCommssionDtl getById(@Param("id") Long id);

	/**
	 * 保存分销订单佣金详情表
	 * @param mallFxOrderCommssionDtl 分销订单佣金详情表
	 */
	void save(@Param("mallFxOrderCommssionDtl") MallFxOrderCommssionDtl mallFxOrderCommssionDtl);

	/**
	 * 更新分销订单佣金详情表
	 * @param mallFxOrderCommssionDtl 分销订单佣金详情表
	 */
	void update(@Param("mallFxOrderCommssionDtl") MallFxOrderCommssionDtl mallFxOrderCommssionDtl);

	/**
	 * 根据分销订单佣金详情表id删除分销订单佣金详情表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
