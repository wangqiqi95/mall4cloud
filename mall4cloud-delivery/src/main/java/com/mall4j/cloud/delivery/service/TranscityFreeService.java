package com.mall4j.cloud.delivery.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.delivery.model.TranscityFree;
import com.mall4j.cloud.delivery.vo.TranscityFreeVO;

/**
 * 指定条件包邮城市项
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public interface TranscityFreeService {

	/**
	 * 分页获取指定条件包邮城市项列表
	 * @param pageDTO 分页参数
	 * @return 指定条件包邮城市项列表分页数据
	 */
	PageVO<TranscityFreeVO> page(PageDTO pageDTO);

	/**
	 * 根据指定条件包邮城市项id获取指定条件包邮城市项
	 *
	 * @param transcityFreeId 指定条件包邮城市项id
	 * @return 指定条件包邮城市项
	 */
	TranscityFreeVO getByTranscityFreeId(Long transcityFreeId);

	/**
	 * 保存指定条件包邮城市项
	 * @param transcityFree 指定条件包邮城市项
	 */
	void save(TranscityFree transcityFree);

	/**
	 * 更新指定条件包邮城市项
	 * @param transcityFree 指定条件包邮城市项
	 */
	void update(TranscityFree transcityFree);

	/**
	 * 根据指定条件包邮城市项id删除指定条件包邮城市项
	 * @param transcityFreeId
	 */
	void deleteById(Long transcityFreeId);
}
