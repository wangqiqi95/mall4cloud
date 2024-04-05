package com.mall4j.cloud.transfer.mapper;

import com.mall4j.cloud.transfer.model.ScoreConvert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 积分活动表
 *
 * @author FrozenWatermelon
 * @date 2022-04-14 15:16:46
 */
public interface ScoreConvertMapper {

	/**
	 * 获取积分活动表列表
	 * @return 积分活动表列表
	 */
	List<ScoreConvert> list();

	/**
	 * 根据积分活动表id获取积分活动表
	 *
	 * @param convertId 积分活动表id
	 * @return 积分活动表
	 */
	ScoreConvert getByConvertId(@Param("convertId") Long convertId);

	/**
	 * 保存积分活动表
	 * @param scoreConvert 积分活动表
	 */
	void save(@Param("scoreConvert") ScoreConvert scoreConvert);

	/**
	 * 更新积分活动表
	 * @param scoreConvert 积分活动表
	 */
	void update(@Param("scoreConvert") ScoreConvert scoreConvert);

	/**
	 * 根据积分活动表id删除积分活动表
	 * @param convertId
	 */
	void deleteById(@Param("convertId") Long convertId);
}
