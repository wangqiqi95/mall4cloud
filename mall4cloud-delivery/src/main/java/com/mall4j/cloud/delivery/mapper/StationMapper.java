package com.mall4j.cloud.delivery.mapper;

import com.mall4j.cloud.delivery.model.Station;
import com.mall4j.cloud.delivery.vo.StationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自提点信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public interface StationMapper {

	/**
	 * 获取自提点信息列表
	 * @return 自提点信息列表
	 */
	List<StationVO> list();

	/**
	 * 根据自提点信息id获取自提点信息
	 *
	 * @param stationId 自提点信息id
	 * @return 自提点信息
	 */
	StationVO getByStationId(@Param("stationId") Long stationId);

	/**
	 * 保存自提点信息
	 * @param station 自提点信息
	 */
	void save(@Param("station") Station station);

	/**
	 * 更新自提点信息
	 * @param station 自提点信息
	 */
	void update(@Param("station") Station station);

	/**
	 * 根据自提点信息id删除自提点信息
	 * @param stationId
	 */
	void deleteById(@Param("stationId") Long stationId);
}
