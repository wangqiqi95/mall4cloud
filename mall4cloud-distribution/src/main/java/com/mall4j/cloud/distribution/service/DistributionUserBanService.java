package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionUserBanDTO;
import com.mall4j.cloud.distribution.model.DistributionUserBan;
import com.mall4j.cloud.distribution.vo.DistributionUserBanVO;

/**
 * 分销封禁记录
 *
 * @author cl
 * @date 2021-08-09 14:14:08
 */
public interface DistributionUserBanService {

	/**
	 * 分页获取分销封禁记录列表
	 * @param pageDTO 分页参数
	 * @param distributionUserBanDTO
	 * @return 分销封禁记录列表分页数据
	 */
	PageVO<DistributionUserBanVO> page(PageDTO pageDTO, DistributionUserBanDTO distributionUserBanDTO);

	/**
	 * 根据分销封禁记录id获取分销封禁记录
	 *
	 * @param banId 分销封禁记录id
	 * @return 分销封禁记录
	 */
	DistributionUserBan getByBanId(Long banId);

	/**
	 * 保存分销封禁记录
	 * @param distributionUserBan 分销封禁记录
	 */
	void save(DistributionUserBan distributionUserBan);

	/**
	 * 更新分销封禁记录
	 * @param distributionUserBan 分销封禁记录
	 */
	void update(DistributionUserBan distributionUserBan);

	/**
	 * 根据分销封禁记录id删除分销封禁记录
	 * @param banId 分销封禁记录id
	 */
	void deleteById(Long banId);
}
