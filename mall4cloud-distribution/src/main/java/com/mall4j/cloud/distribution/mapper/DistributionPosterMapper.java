package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionPosterDTO;
import com.mall4j.cloud.distribution.model.DistributionPoster;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销推广-推广海报
 *
 * @author ZengFanChang
 * @date 2021-12-20 20:26:44
 */
public interface DistributionPosterMapper {

	/**
	 * 获取分销推广-推广海报列表
	 * @return 分销推广-推广海报列表
	 */
	List<DistributionPoster> list(@Param("distributionPosterDTO") DistributionPosterDTO distributionPosterDTO);

	/**
	 * 根据分销推广-推广海报id获取分销推广-推广海报
	 *
	 * @param id 分销推广-推广海报id
	 * @return 分销推广-推广海报
	 */
	DistributionPoster getById(@Param("id") Long id);

	/**
	 * 保存分销推广-推广海报
	 * @param distributionPoster 分销推广-推广海报
	 */
	void save(@Param("distributionPoster") DistributionPoster distributionPoster);

	/**
	 * 更新分销推广-推广海报
	 * @param distributionPoster 分销推广-推广海报
	 */
	void update(@Param("distributionPoster") DistributionPoster distributionPoster);

	/**
	 * 根据分销推广-推广海报id删除分销推广-推广海报
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	List<DistributionPoster> pageEffect(@Param("distributionPosterDTO") DistributionPosterDTO distributionPosterDTO);

}
