package com.mall4j.cloud.platform.mapper;

import com.mall4j.cloud.platform.model.RenoApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 装修适用门店
 *
 * @author FrozenWatermelon
 * @date 2022-01-27 02:00:54
 */
public interface RenoApplyMapper {

	/**
	 * 获取装修适用门店列表
	 * @return 装修适用门店列表
	 */
	List<RenoApply> list();

	/**
	 * 根据装修适用门店id获取装修适用门店
	 *
	 * @param id 装修适用门店id
	 * @return 装修适用门店
	 */
	RenoApply getById(@Param("id") Long id);

	/**
	 * 保存装修适用门店
	 * @param renoApply 装修适用门店
	 */
	void save(@Param("renoApply") RenoApply renoApply);

	/**
	 * 更新装修适用门店
	 * @param renoApply 装修适用门店
	 */
	void update(@Param("renoApply") RenoApply renoApply);

	/**
	 * 根据装修适用门店id删除装修适用门店
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	void deleteByRenoId(@Param("renoId") Long renoId);

	void deleteByRenoIdAndStroreId(@Param("renoId") Long renoId,@Param("storeId") Long storeId);


    List<Long> listByRenoId(@Param("renovationId") Long renovationId);
}
