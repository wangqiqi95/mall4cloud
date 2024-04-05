package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.model.DistributionUserWalletBill;
import com.mall4j.cloud.distribution.vo.DistributionUserWalletBillVO;

import java.util.List;

/**
 * 分销员钱包流水记录
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
public interface DistributionUserWalletBillService {

	/**
	 * 分页获取分销员钱包流水记录列表
	 * @param pageDTO 分页参数
	 * @return 分销员钱包流水记录列表分页数据
	 */
	PageVO<DistributionUserWalletBill> page(PageDTO pageDTO);

	/**
	 * 根据分销员钱包流水记录id获取分销员钱包流水记录
	 *
	 * @param id 分销员钱包流水记录id
	 * @return 分销员钱包流水记录
	 */
	DistributionUserWalletBill getById(Long id);

	/**
	 * 保存分销员钱包流水记录
	 * @param distributionUserWalletBill 分销员钱包流水记录
	 */
	void save(DistributionUserWalletBill distributionUserWalletBill);

	/**
	 * 更新分销员钱包流水记录
	 * @param distributionUserWalletBill 分销员钱包流水记录
	 */
	void update(DistributionUserWalletBill distributionUserWalletBill);

	/**
	 * 根据分销员钱包流水记录id删除分销员钱包流水记录
	 * @param id 分销员钱包流水记录id
	 */
	void deleteById(Long id);

	/**
	 * 获取分销员钱包流水记录列表
	 * @param pageDTO 分页参数
	 * @param userMobile 分销员手机号码
	 * @return
	 */
    PageVO<DistributionUserWalletBillVO> walletBillPage(PageDTO pageDTO, String userMobile);

	/**
	 * 批量保存分销员钱包流水
	 * @param distributionUserWalletBills 分销员钱包流水
	 */
	void saveBatch(List<DistributionUserWalletBill> distributionUserWalletBills);

}
