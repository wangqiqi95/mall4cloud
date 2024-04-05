package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.MallFxCashRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销人员提现记录表
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:30
 */
public interface MallFxCashRequestMapper {

	/**
	 * 获取分销人员提现记录表列表
	 * @return 分销人员提现记录表列表
	 */
	List<MallFxCashRequest> list();

	/**
	 * 根据分销人员提现记录表id获取分销人员提现记录表
	 *
	 * @param id 分销人员提现记录表id
	 * @return 分销人员提现记录表
	 */
	MallFxCashRequest getById(@Param("id") Long id);

	/**
	 * 保存分销人员提现记录表
	 * @param mallFxCashRequest 分销人员提现记录表
	 */
	void save(@Param("mallFxCashRequest") MallFxCashRequest mallFxCashRequest);

	/**
	 * 更新分销人员提现记录表
	 * @param mallFxCashRequest 分销人员提现记录表
	 */
	void update(@Param("mallFxCashRequest") MallFxCashRequest mallFxCashRequest);

	/**
	 * 根据分销人员提现记录表id删除分销人员提现记录表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
