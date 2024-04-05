package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.api.distribution.constant.CommissionChangeTypeEnum;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionCommissionAccountDTO;
import com.mall4j.cloud.distribution.model.DistributionCommissionAccount;
import com.mall4j.cloud.distribution.vo.DistributionCommissionAccountVO;

import javax.servlet.http.HttpServletResponse;

/**
 * 佣金管理-佣金账户
 *
 * @author ZengFanChang
 * @date 2021-12-05 19:44:22
 */
public interface DistributionCommissionAccountService {

	/**
	 * 分页获取佣金管理-佣金账户列表
	 * @param pageDTO 分页参数
	 * @return 佣金管理-佣金账户列表分页数据
	 */
	PageVO<DistributionCommissionAccountDTO> page(PageDTO pageDTO, DistributionCommissionAccountDTO distributionCommissionAccountDTO);

	/**
	 * 根据佣金管理-佣金账户id获取佣金管理-佣金账户
	 *
	 * @param id 佣金管理-佣金账户id
	 * @return 佣金管理-佣金账户
	 */
	DistributionCommissionAccount getById(Long id);

	/**
	 * 保存佣金管理-佣金账户
	 * @param distributionCommissionAccount 佣金管理-佣金账户
	 */
	void save(DistributionCommissionAccount distributionCommissionAccount);

	/**
	 * 更新佣金管理-佣金账户
	 * @param distributionCommissionAccount 佣金管理-佣金账户
	 */
	void update(DistributionCommissionAccount distributionCommissionAccount);

	/**
	 * 根据佣金管理-佣金账户id删除佣金管理-佣金账户
	 * @param id 佣金管理-佣金账户id
	 */
	void deleteById(Long id);

    void commissionExcel(HttpServletResponse response, DistributionCommissionAccountDTO dto);

	DistributionCommissionAccount getByUser(Long userId, Integer identityType);

	void updateCommission(DistributionCommissionAccount account, Long value, CommissionChangeTypeEnum changeTypeEnum);

	/**
	 * 佣金账户信息
	 * @param userId 导购or微客用户id
	 * @param identityType 所属身份 1-导购 2-微客
	 * @return
	 */
	DistributionCommissionAccountVO info(Long userId, Integer identityType);

	void staffWithdraw();

}
