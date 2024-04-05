package com.mall4j.cloud.delivery.mapper;

import com.mall4j.cloud.delivery.model.Transcity;
import com.mall4j.cloud.delivery.vo.TranscityVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 运费项和运费城市关联信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public interface TranscityMapper {

	/**
	 * 获取运费项和运费城市关联信息列表
	 * @return 运费项和运费城市关联信息列表
	 */
	List<TranscityVO> list();

	/**
	 * 根据运费项和运费城市关联信息id获取运费项和运费城市关联信息
	 *
	 * @param transcityId 运费项和运费城市关联信息id
	 * @return 运费项和运费城市关联信息
	 */
	TranscityVO getByTranscityId(@Param("transcityId") Long transcityId);

	/**
	 * 保存运费项和运费城市关联信息
	 * @param transcity 运费项和运费城市关联信息
	 */
	void save(@Param("transcity") Transcity transcity);

	/**
	 * 更新运费项和运费城市关联信息
	 * @param transcity 运费项和运费城市关联信息
	 */
	void update(@Param("transcity") Transcity transcity);

	/**
	 * 根据运费项和运费城市关联信息id删除运费项和运费城市关联信息
	 * @param transcityId
	 */
	void deleteById(@Param("transcityId") Long transcityId);

	/**
	 * 批量保存运费项关联城市信息
	 * @param transCities 关联信息
	 */
	void saveBatch(@Param("transCities") List<Transcity> transCities);

	/**
	 * 删除所有运费项包含的城市
	 * @param transFeeIds 运费项id
	 */
	void deleteBatchByTransFeeIds(@Param("transFeeIds") List<Long> transFeeIds);
}
