package com.mall4j.cloud.delivery.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.delivery.model.Station;
import com.mall4j.cloud.delivery.vo.StationVO;

/**
 * 自提点信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public interface StationService {

	/**
	 * 分页获取自提点信息列表
	 * @param pageDTO 分页参数
	 * @return 自提点信息列表分页数据
	 */
	PageVO<StationVO> page(PageDTO pageDTO);

	/**
	 * 根据自提点信息id获取自提点信息
	 *
	 * @param stationId 自提点信息id
	 * @return 自提点信息
	 */
	StationVO getByStationId(Long stationId);

	/**
	 * 保存自提点信息
	 * @param station 自提点信息
	 */
	void save(Station station);

	/**
	 * 更新自提点信息
	 * @param station 自提点信息
	 */
	void update(Station station);

	/**
	 * 根据自提点信息id删除自提点信息
	 * @param stationId
	 */
	void deleteById(Long stationId);
}
