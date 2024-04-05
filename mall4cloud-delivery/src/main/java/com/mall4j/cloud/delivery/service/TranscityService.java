package com.mall4j.cloud.delivery.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.delivery.model.Transcity;
import com.mall4j.cloud.delivery.vo.TranscityVO;

/**
 * 运费项和运费城市关联信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public interface TranscityService {

	/**
	 * 分页获取运费项和运费城市关联信息列表
	 * @param pageDTO 分页参数
	 * @return 运费项和运费城市关联信息列表分页数据
	 */
	PageVO<TranscityVO> page(PageDTO pageDTO);

	/**
	 * 根据运费项和运费城市关联信息id获取运费项和运费城市关联信息
	 *
	 * @param transcityId 运费项和运费城市关联信息id
	 * @return 运费项和运费城市关联信息
	 */
	TranscityVO getByTranscityId(Long transcityId);

	/**
	 * 保存运费项和运费城市关联信息
	 * @param transcity 运费项和运费城市关联信息
	 */
	void save(Transcity transcity);

	/**
	 * 更新运费项和运费城市关联信息
	 * @param transcity 运费项和运费城市关联信息
	 */
	void update(Transcity transcity);

	/**
	 * 根据运费项和运费城市关联信息id删除运费项和运费城市关联信息
	 * @param transcityId
	 */
	void deleteById(Long transcityId);
}
