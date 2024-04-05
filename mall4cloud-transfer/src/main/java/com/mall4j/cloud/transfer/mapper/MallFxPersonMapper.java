package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.MallFxPerson;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 22:33:31
 */
public interface MallFxPersonMapper {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<MallFxPerson> list();

	/**
	 * 根据id获取
	 *
	 * @param id id
	 * @return 
	 */
	MallFxPerson getById(@Param("id") Long id);

	/**
	 * 保存
	 * @param mallFxPerson 
	 */
	void save(@Param("mallFxPerson") MallFxPerson mallFxPerson);

	/**
	 * 更新
	 * @param mallFxPerson 
	 */
	void update(@Param("mallFxPerson") MallFxPerson mallFxPerson);

	/**
	 * 根据id删除
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
