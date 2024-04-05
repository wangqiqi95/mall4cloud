package com.mall4j.cloud.platform.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.platform.model.RenoApply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 装修适用门店
 *
 * @author FrozenWatermelon
 * @date 2022-01-27 02:00:54
 */
public interface RenoApplyService {

	/**
	 * 分页获取装修适用门店列表
	 * @param pageDTO 分页参数
	 * @return 装修适用门店列表分页数据
	 */
	PageVO<RenoApply> page(PageDTO pageDTO);

	/**
	 * 根据装修适用门店id获取装修适用门店
	 *
	 * @param id 装修适用门店id
	 * @return 装修适用门店
	 */
	RenoApply getById(Long id);

	/**
	 * 保存装修适用门店
	 * @param renoApply 装修适用门店
	 */
	void save(RenoApply renoApply);

	/**
	 * 更新装修适用门店
	 * @param renoApply 装修适用门店
	 */
	void update(RenoApply renoApply);

	/**
	 * 根据装修适用门店id删除装修适用门店
	 * @param id 装修适用门店id
	 */
	void deleteById(Long id);

	void deleteByRenoIdAndStroreId(Long renoId,Long stroreId);

	void deleteByRenoId(Long renoId);

    List<Long> listByRenoId(Long renovationId);

	void updateStoreId(List<Long> renoApplyStoreList, Long renovationId);


}
