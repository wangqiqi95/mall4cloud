package com.mall4j.cloud.delivery.mapper;

import com.mall4j.cloud.delivery.dto.TransfeeFreeDTO;
import com.mall4j.cloud.delivery.model.TransfeeFree;
import com.mall4j.cloud.delivery.vo.TransfeeFreeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 指定条件包邮项
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public interface TransfeeFreeMapper {

	/**
	 * 获取指定条件包邮项列表
	 * @return 指定条件包邮项列表
	 */
	List<TransfeeFreeVO> list();

	/**
	 * 根据指定条件包邮项id获取指定条件包邮项
	 *
	 * @param transfeeFreeId 指定条件包邮项id
	 * @return 指定条件包邮项
	 */
	TransfeeFreeVO getByTransfeeFreeId(@Param("transfeeFreeId") Long transfeeFreeId);

	/**
	 * 更新指定条件包邮项
	 * @param transfeeFree 指定条件包邮项
	 */
	void update(@Param("transfeeFree") TransfeeFree transfeeFree);

	/**
	 *  批量插入运费包邮条件项项
	 * @param transFeeFrees 包邮条件
	 */
    void saveBatch(@Param("transFeeFrees") List<TransfeeFreeDTO> transFeeFrees);

	/**
	 * 根据运费id删除所有的指定包邮条件项
	 * @param transportId 运费id
	 */
	void deleteTransFeeFreesByTransportId(@Param("transportId") Long transportId);
}
