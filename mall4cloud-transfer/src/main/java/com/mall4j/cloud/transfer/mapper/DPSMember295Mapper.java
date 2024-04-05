package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.DPSMember295;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据类型 data.prctvmkt.skechers.Member 的表
 *
 * @author FrozenWatermelon
 * @date 2022-04-05 14:44:13
 */
public interface DPSMember295Mapper {

	/**
	 * 获取数据类型 data.prctvmkt.skechers.Member 的表列表
	 * @return 数据类型 data.prctvmkt.skechers.Member 的表列表
	 */
	List<DPSMember295> list();

	/**
	 * 根据数据类型 data.prctvmkt.skechers.Member 的表id获取数据类型 data.prctvmkt.skechers.Member 的表
	 *
	 * @param id 数据类型 data.prctvmkt.skechers.Member 的表id
	 * @return 数据类型 data.prctvmkt.skechers.Member 的表
	 */
	DPSMember295 getById(@Param("id") String id);

	/**
	 * 保存数据类型 data.prctvmkt.skechers.Member 的表
	 * @param dPSMember295 数据类型 data.prctvmkt.skechers.Member 的表
	 */
	void save(@Param("dPSMember295") DPSMember295 dPSMember295);

	/**
	 * 更新数据类型 data.prctvmkt.skechers.Member 的表
	 * @param dPSMember295 数据类型 data.prctvmkt.skechers.Member 的表
	 */
	void update(@Param("dPSMember295") DPSMember295 dPSMember295);

	/**
	 * 根据数据类型 data.prctvmkt.skechers.Member 的表id删除数据类型 data.prctvmkt.skechers.Member 的表
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
