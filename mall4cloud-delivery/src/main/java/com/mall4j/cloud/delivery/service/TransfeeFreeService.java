package com.mall4j.cloud.delivery.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.delivery.vo.TransfeeFreeVO;

/**
 * 指定条件包邮项
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public interface TransfeeFreeService {

	/**
	 * 分页获取指定条件包邮项列表
	 * @param pageDTO 分页参数
	 * @return 指定条件包邮项列表分页数据
	 */
	PageVO<TransfeeFreeVO> page(PageDTO pageDTO);

	/**
	 * 根据指定条件包邮项id获取指定条件包邮项
	 *
	 * @param transfeeFreeId 指定条件包邮项id
	 * @return 指定条件包邮项
	 */
	TransfeeFreeVO getByTransfeeFreeId(Long transfeeFreeId);

}
