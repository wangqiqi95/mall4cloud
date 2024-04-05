package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.EtoGiftsRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-04-14 13:57:21
 */
public interface EtoGiftsRecordMapper {

	/**
	 * 获取列表
	 * @return 列表
	 */
	List<EtoGiftsRecord> list();

    List<EtoGiftsRecord> listByGiftsId(@Param("giftId")Long giftId);

	/**
	 * 根据id获取
	 *
	 * @param id id
	 * @return 
	 */
	EtoGiftsRecord getById(@Param("id") Long id);

	/**
	 * 保存
	 * @param etoGiftsRecord 
	 */
	void save(@Param("etoGiftsRecord") EtoGiftsRecord etoGiftsRecord);

	/**
	 * 更新
	 * @param etoGiftsRecord 
	 */
	void update(@Param("etoGiftsRecord") EtoGiftsRecord etoGiftsRecord);

	/**
	 * 根据id删除
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
