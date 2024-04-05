package com.mall4j.cloud.distribution.service;


import com.mall4j.cloud.api.platform.vo.DistributionConfigApiVO;
import com.mall4j.cloud.api.platform.vo.DistributionRecruitConfigApiVO;

/**
 * 分销员配置信息信息
 *
 * @author cl
 * @date 2021-08-12 10:05:31
 */
public interface DistributionConfigService {


	/**
	 * 获取分销基本配置信息
	 * @return 分销基本配置信息
	 */
	DistributionConfigApiVO getDistributionConfig();

	/**
	 * 获取分销推广配置信息
	 * @return 分销推广配置信息
	 */
	DistributionRecruitConfigApiVO getDistributionRecruitConfig();

}
