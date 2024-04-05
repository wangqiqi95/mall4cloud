package com.mall4j.cloud.delivery.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.delivery.model.Transfee;
import com.mall4j.cloud.delivery.vo.TransfeeVO;

/**
 * 运费项
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:01
 */
public interface TransfeeService {

	/**
	 * 分页获取运费项列表
	 * @param pageDTO 分页参数
	 * @return 运费项列表分页数据
	 */
	PageVO<TransfeeVO> page(PageDTO pageDTO);

	/**
	 * 根据运费项id获取运费项
	 *
	 * @param transfeeId 运费项id
	 * @return 运费项
	 */
	TransfeeVO getByTransfeeId(Long transfeeId);

	/**
	 * 保存运费项
	 * @param transfee 运费项
	 */
	void save(Transfee transfee);

	/**
	 * 更新运费项
	 * @param transfee 运费项
	 */
	void update(Transfee transfee);

	/**
	 * 根据运费项id删除运费项
	 * @param transfeeId
	 */
	void deleteById(Long transfeeId);
}
