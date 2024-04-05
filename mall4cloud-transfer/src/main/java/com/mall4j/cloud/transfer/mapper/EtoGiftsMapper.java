package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.EtoGifts;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-04-14 13:57:20
 */
public interface EtoGiftsMapper {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<EtoGifts> list();

	/**
	 * 根据id获取
	 *
	 * @param id id
	 * @return 
	 */
	EtoGifts getById(@Param("id") Long id);

	/**
	 * 保存
	 * @param etoGifts 
	 */
	void save(@Param("etoGifts") EtoGifts etoGifts);

	/**
	 * 更新
	 * @param etoGifts 
	 */
	void update(@Param("etoGifts") EtoGifts etoGifts);

	/**
	 * 根据id删除
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
