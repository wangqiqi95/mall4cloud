package com.mall4j.cloud.delivery.mapper;

import com.mall4j.cloud.delivery.dto.TransportDTO;
import com.mall4j.cloud.delivery.model.Transport;
import com.mall4j.cloud.delivery.vo.TransfeeFreeVO;
import com.mall4j.cloud.delivery.vo.TransportVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 运费模板
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public interface TransportMapper {

	/**
	 * 获取运费模板列表
	 *
	 * @param transportDTO
	 * @return 运费模板列表
	 */
	List<TransportVO> list(@Param("transportDTO") TransportDTO transportDTO);

	/**
	 * 保存运费模板
	 *
	 * @param transport 运费模板
	 */
	void save(@Param("transport") Transport transport);

	/**
	 * 更新运费模板
	 *
	 * @param transport 运费模板
	 */
	void updateById(@Param("transport") Transport transport);

	/**
	 * 根据运费模板id删除运费模板
	 *
	 * @param transportId
	 */
	void deleteById(@Param("transportId") Long transportId);

	/**
	 * 根据运费模板id获取运费模板和运费项及对应城市
	 *
	 * @param transportId 运费模板id
	 * @return
	 */
	TransportVO getTransportAndTransfeeAndTranscityById(@Param("transportId") Long transportId);

	/**
	 * 根据运费模板id获取运费包邮项及对应城市
	 *
	 * @param transportId 运费模板id
	 * @return
	 */
	List<TransfeeFreeVO> getTransFeeFreeAndTransCityFreeByTransportId(@Param("transportId") Long transportId);

	/**
	 * 根据运费模板ids删除运费模板
	 *
	 * @param transportIds 运费模板ids
	 */
	void deleteByIds(@Param("transportIds") Long[] transportIds);

	/**
	 * 根据店铺id获取所有的运费模板列表
	 *
	 * @param tenantId
	 * @return
	 */
	List<TransportVO> listTransport(@Param("tenantId") Long tenantId);

	/**
	 * 统计该运费模板名称使用的数量
	 * @param shopId 店铺id
	 * @param transName 运费模板名称
	 * @param transportId 运费模板id
	 * @return 使用的数量
	 */
    int countByTransName(@Param("shopId") Long shopId, @Param("transName") String transName, @Param("transportId") Long transportId);

	/**
	 * 统计该店铺下的包邮模板数量
	 * @param isFreeFee
	 * @param shopId
	 * @return
	 */
	int countByFreeAndShopId(@Param("isFreeFee") Integer isFreeFee, @Param("shopId") Long shopId);
}
