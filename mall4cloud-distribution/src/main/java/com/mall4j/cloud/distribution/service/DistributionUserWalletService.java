package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionUserWalletDTO;
import com.mall4j.cloud.distribution.model.DistributionUserWallet;
import com.mall4j.cloud.distribution.vo.DistributionUserWalletVO;

/**
 * 分销员钱包信息
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
public interface DistributionUserWalletService {

	/**
	 * 分页获取分销员钱包信息列表
	 * @param pageDTO 分页参数
	 * @return 分销员钱包信息列表分页数据
	 */
	PageVO<DistributionUserWallet> page(PageDTO pageDTO);

	/**
	 * 根据分销员钱包信息id获取分销员钱包信息
	 *
	 * @param walletId 分销员钱包信息id
	 * @return 分销员钱包信息
	 */
	DistributionUserWallet getByWalletId(Long walletId);

	/**
	 * 保存分销员钱包信息
	 * @param distributionUserWallet 分销员钱包信息
	 */
	void save(DistributionUserWallet distributionUserWallet);

	/**
	 * 更新分销员钱包信息
	 * @param distributionUserWallet 分销员钱包信息
	 * @return 受影响行数
	 */
	void update(DistributionUserWallet distributionUserWallet);

	/**
	 * 根据分销员钱包信息id删除分销员钱包信息
	 * @param walletId 分销员钱包信息id
	 */
	void deleteById(Long walletId);

	/**
	 * 分页获取分销员钱包
	 * @param pageDTO 分页参数
	 * @param userMobile 分销员手机号
	 * @return
	 */
    PageVO<DistributionUserWalletVO> walletPage(PageDTO pageDTO, String userMobile);

	/**
	 * 更新分销员钱包信息
     * @param distributionUserWalletDTO 分销员钱包信息
     * @param userId
     */
	void updateWallet(DistributionUserWalletDTO distributionUserWalletDTO, Long userId);

	/**
	 * 根据分销员id 获取分销员钱包信息
	 * @param distributionUserId 分销员id
	 * @return 分销员钱包信息
	 */
    DistributionUserWallet getByDistributionUserId(Long distributionUserId);

	/**
	 * 更新分销员钱包
	 * @param updateWallet 分销员钱包
	 * @return 受影响行数
	 */
    int updateWalletAmount(DistributionUserWallet updateWallet);
}
