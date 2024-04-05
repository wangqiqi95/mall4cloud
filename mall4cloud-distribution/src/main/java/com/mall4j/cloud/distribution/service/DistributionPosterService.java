package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionPosterDTO;
import com.mall4j.cloud.distribution.model.DistributionPoster;

/**
 * 分销推广-推广海报
 *
 * @author ZengFanChang
 * @date 2021-12-20 20:26:44
 */
public interface DistributionPosterService {

	/**
	 * 分页获取分销推广-推广海报列表
	 * @param pageDTO 分页参数
	 * @return 分销推广-推广海报列表分页数据
	 */
	PageVO<DistributionPoster> page(PageDTO pageDTO, DistributionPosterDTO distributionPosterDTO);

	/**
	 * 根据分销推广-推广海报id获取分销推广-推广海报
	 *
	 * @param id 分销推广-推广海报id
	 * @return 分销推广-推广海报
	 */
	DistributionPoster getById(Long id);

	/**
	 * 保存分销推广-推广海报
	 * @param distributionPoster 分销推广-推广海报
	 */
	void save(DistributionPosterDTO distributionPoster);

	/**
	 * 更新分销推广-推广海报
	 * @param distributionPoster 分销推广-推广海报
	 */
	void update(DistributionPosterDTO distributionPoster);

	/**
	 * 根据分销推广-推广海报id删除分销推广-推广海报
	 * @param id 分销推广-推广海报id
	 */
	void deleteById(Long id);

    PageVO<DistributionPoster> pageEffect(PageDTO pageDTO, DistributionPosterDTO distributionPosterDTO);
}
