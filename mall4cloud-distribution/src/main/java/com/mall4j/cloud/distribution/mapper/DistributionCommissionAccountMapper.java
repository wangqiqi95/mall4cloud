package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionCommissionAccountDTO;
import com.mall4j.cloud.distribution.model.DistributionCommissionAccount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 佣金管理-佣金账户
 *
 * @author ZengFanChang
 * @date 2021-12-05 19:44:22
 */
public interface DistributionCommissionAccountMapper {

	/**
	 * 获取佣金管理-佣金账户列表
	 * @return 佣金管理-佣金账户列表
	 */
	List<DistributionCommissionAccountDTO> list(@Param("distributionCommissionAccountDTO") DistributionCommissionAccountDTO distributionCommissionAccountDTO);

	/**
	 * 根据佣金管理-佣金账户id获取佣金管理-佣金账户
	 *
	 * @param id 佣金管理-佣金账户id
	 * @return 佣金管理-佣金账户
	 */
	DistributionCommissionAccount getById(@Param("id") Long id);

	/**
	 * 保存佣金管理-佣金账户
	 * @param distributionCommissionAccount 佣金管理-佣金账户
	 */
	void save(@Param("distributionCommissionAccount") DistributionCommissionAccount distributionCommissionAccount);

	/**
	 * 更新佣金管理-佣金账户
	 * @param distributionCommissionAccount 佣金管理-佣金账户
	 */
	void update(@Param("distributionCommissionAccount") DistributionCommissionAccount distributionCommissionAccount);

	/**
	 * 根据佣金管理-佣金账户id删除佣金管理-佣金账户
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

    DistributionCommissionAccount getByUser(@Param("userId") Long userId, @Param("identityType") Integer identityType);

    List<DistributionCommissionAccount> listStaffCommissionAccount();

}
