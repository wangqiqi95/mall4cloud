package com.mall4j.cloud.delivery.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.delivery.dto.TransportDTO;
import com.mall4j.cloud.delivery.vo.TransportVO;

import java.util.List;

/**
 * 运费模板
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public interface TransportService {

	/**
	 * 分页获取运费模板列表
	 * @param pageDTO 分页参数
	 * @param transportDTO
     * @return 运费模板列表分页数据
	 */
	PageVO<TransportVO> page(PageDTO pageDTO, TransportDTO transportDTO);

	/**
	 * 根据id获取运费模板详细信息
	 * @param transportId 运费模板id
	 * @return
	 */
	TransportVO getTransportAndAllItemsById(Long transportId);

	/**
	 * 保存运费模板信息
	 * @param transport 运费模板信息
	 */
	void insertTransportAndTransFee(TransportDTO transport);

	/**
	 * 更新运费模板
	 * @param transportDTO 运费模板
	 */
	void updateTransportAndTransFee(TransportDTO transportDTO);

	/**
	 * 根据运费模板ids删除运费模板
	 * @param transportIds 运费模板ids
	 */
	void deleteTransportAndTransFeeAndTransCityById(Long transportIds);

	/**
	 * 根据id清除缓存
	 * @param transportId 运费模板id
	 */
	void removeTransportAndAllItemsCache(Long transportId);

	/**
	 *	根据店铺id获取所有的运费模板列表
	 * @param tenantId
	 * @return
	 */
    List<TransportVO> listTransport(Long tenantId);
	
	/**
	 * 视频号4.0获取运费模板列表
	 * @param pageDTO
	 * @param transportDTO
	 * @return
	 */
	PageVO<TransportVO> transportList(PageDTO pageDTO, TransportDTO transportDTO);
}
