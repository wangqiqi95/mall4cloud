package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionSpuBind;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户商品绑定信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
public interface DistributionSpuBindMapper {

	/**
	 * 获取用户商品绑定信息列表
	 * @return 用户商品绑定信息列表
	 */
	List<DistributionSpuBind> list();

	/**
	 * 根据用户商品绑定信息id获取用户商品绑定信息
	 *
	 * @param id 用户商品绑定信息id
	 * @return 用户商品绑定信息
	 */
	DistributionSpuBind getById(@Param("id") Long id);

	/**
	 * 保存用户商品绑定信息
	 * @param distributionSpuBind 用户商品绑定信息
	 */
	void save(@Param("distributionSpuBind") DistributionSpuBind distributionSpuBind);

	/**
	 * 更新用户商品绑定信息
	 * @param distributionSpuBind 用户商品绑定信息
	 */
	void update(@Param("distributionSpuBind") DistributionSpuBind distributionSpuBind);

	/**
	 * 根据用户商品绑定信息id删除用户商品绑定信息
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 根据分销员id修改状态（将商品分享记录设为失效）
	 * @param distributionUserId 分销员id
	 * @param state 状态
	 */
	void updateStateByDistributionUserId(@Param("distributionUserId") Long distributionUserId,@Param("state") Integer state);
}
