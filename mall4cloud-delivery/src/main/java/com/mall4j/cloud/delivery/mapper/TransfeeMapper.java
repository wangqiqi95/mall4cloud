package com.mall4j.cloud.delivery.mapper;

import com.mall4j.cloud.delivery.dto.TransfeeDTO;
import com.mall4j.cloud.delivery.model.Transfee;
import com.mall4j.cloud.delivery.vo.TransfeeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 运费项
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public interface TransfeeMapper {

	/**
	 * 获取运费项列表
	 * @return 运费项列表
	 */
	List<TransfeeVO> list();

	/**
	 * 根据运费项id获取运费项
	 *
	 * @param transfeeId 运费项id
	 * @return 运费项
	 */
	TransfeeVO getByTransfeeId(@Param("transfeeId") Long transfeeId);

	/**
	 * 保存运费项
	 * @param transfee 运费项
	 */
	void save(@Param("transfee") Transfee transfee);

	/**
	 * 更新运费项
	 * @param transfee 运费项
	 */
	void update(@Param("transfee") Transfee transfee);

	/**
	 * 根据运费项id删除运费项
	 * @param transfeeId
	 */
	void deleteById(@Param("transfeeId") Long transfeeId);

	/**
	 *  批量插入运费项
	 * @param transFees
	 */
    void saveBatch(@Param("transFees") List<TransfeeDTO> transFees);

	/**
	 * 根据运费id删除所有的运费项
	 * @param transportId 运费id
	 */
	void deleteByTransportId(@Param("transportId") Long transportId);
}
