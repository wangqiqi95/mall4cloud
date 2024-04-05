package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionCommissionUnity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 佣金配置-统一佣金
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
public interface DistributionCommissionUnityMapper {

	/**
	 * 根据佣金配置-统一佣金id获取佣金配置-统一佣金
	 *
	 * @return 佣金配置-统一佣金
	 */
	DistributionCommissionUnity get();

	/**
	 * 保存佣金配置-统一佣金
	 * @param distributionCommissionUnity 佣金配置-统一佣金
	 */
	void save(@Param("distributionCommissionUnity") DistributionCommissionUnity distributionCommissionUnity);

	/**
	 * 更新佣金配置-统一佣金
	 * @param distributionCommissionUnity 佣金配置-统一佣金
	 */
	void update(@Param("distributionCommissionUnity") DistributionCommissionUnity distributionCommissionUnity);

	/**
	 * 根据佣金配置-统一佣金id删除佣金配置-统一佣金
	 * @param id
	 */
	void deleteById(@Param("id") Long id);
}
