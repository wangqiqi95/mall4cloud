package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionMomentsDTO;
import com.mall4j.cloud.distribution.model.DistributionMoments;
import com.mall4j.cloud.distribution.vo.DistributionMomentsVO;

import java.util.List;

/**
 * 分销推广-朋友圈
 *
 * @author ZengFanChang
 * @date 2021-12-22 22:05:57
 */
public interface DistributionMomentsService {

	/**
	 * 分页获取分销推广-朋友圈列表
	 * @param pageDTO 分页参数
	 * @return 分销推广-朋友圈列表分页数据
	 */
	PageVO<DistributionMomentsVO> page(PageDTO pageDTO, DistributionMomentsDTO dto);

	/**
	 * 根据分销推广-朋友圈id获取分销推广-朋友圈
	 *
	 * @param id 分销推广-朋友圈id
	 * @return 分销推广-朋友圈
	 */
	DistributionMomentsDTO getMomentsById(Long id);

	DistributionMoments getById(Long id);

	/**
	 * 保存分销推广-朋友圈
	 * @param dto 分销推广-朋友圈
	 */
	void save(DistributionMomentsDTO dto);

	/**
	 * 更新分销推广-朋友圈
	 * @param dto 分销推广-朋友圈
	 */
	void update(DistributionMomentsDTO dto);

	/**
	 * 根据分销推广-朋友圈id删除分销推广-朋友圈
	 * @param id 分销推广-朋友圈id
	 */
	void deleteById(Long id);

	void updateStatusBatch(List<Long> ids, Integer status);

	void momentsTop(Long id, Integer top);

    PageVO<DistributionMoments> pageEffect(PageDTO pageDTO, DistributionMomentsDTO dto);
}
