package com.mall4j.cloud.delivery.mapper;

import com.mall4j.cloud.delivery.model.TranscityFree;
import com.mall4j.cloud.delivery.vo.TranscityFreeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 指定条件包邮城市项
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public interface TranscityFreeMapper {

	/**
	 * 获取指定条件包邮城市项列表
	 * @return 指定条件包邮城市项列表
	 */
	List<TranscityFreeVO> list();

	/**
	 * 根据指定条件包邮城市项id获取指定条件包邮城市项
	 *
	 * @param transcityFreeId 指定条件包邮城市项id
	 * @return 指定条件包邮城市项
	 */
	TranscityFreeVO getByTranscityFreeId(@Param("transcityFreeId") Long transcityFreeId);

	/**
	 * 保存指定条件包邮城市项
	 * @param transcityFree 指定条件包邮城市项
	 */
	void save(@Param("transcityFree") TranscityFree transcityFree);

	/**
	 * 更新指定条件包邮城市项
	 * @param transcityFree 指定条件包邮城市项
	 */
	void update(@Param("transcityFree") TranscityFree transcityFree);

	/**
	 * 根据指定条件包邮城市项id删除指定条件包邮城市项
	 * @param transcityFreeId
	 */
	void deleteById(@Param("transcityFreeId") Long transcityFreeId);

	/**
	 * 批量保存包邮项关联城市信息
	 * @param transCityFrees 关联信息
	 */
    void saveBatch(@Param("transCityFrees") List<TranscityFree> transCityFrees);

	/**
	 * 删除所有指定包邮条件项包含的城市
	 * @param transFeeFreeIds 包邮项id
	 */
	void deleteBatchByTransFeeFreeIds(@Param("transFeeFreeIds") List<Long> transFeeFreeIds);
}
