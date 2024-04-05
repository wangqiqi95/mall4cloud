package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionUserWallet;
import com.mall4j.cloud.distribution.vo.DistributionUserWalletVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销员钱包信息
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
public interface DistributionUserWalletMapper {

	/**
	 * 获取分销员钱包信息列表
	 * @return 分销员钱包信息列表
	 */
	List<DistributionUserWallet> list();

	/**
	 * 根据分销员钱包信息id获取分销员钱包信息
	 *
	 * @param walletId 分销员钱包信息id
	 * @return 分销员钱包信息
	 */
	DistributionUserWallet getByWalletId(@Param("walletId") Long walletId);

	/**
	 * 保存分销员钱包信息
	 * @param distributionUserWallet 分销员钱包信息
	 */
	void save(@Param("distributionUserWallet") DistributionUserWallet distributionUserWallet);

	/**
	 * 更新分销员钱包信息
	 * @param distributionUserWallet 分销员钱包信息
	 */
	void update(@Param("distributionUserWallet") DistributionUserWallet distributionUserWallet);

	/**
	 * 根据分销员钱包信息id删除分销员钱包信息
	 * @param walletId
	 */
	void deleteById(@Param("walletId") Long walletId);

	/**
	 * 分页获取分销员钱包
	 * @param userMobile 分销员手机号
	 * @return
	 */
    List<DistributionUserWalletVO> walletPage(@Param("userMobile") String userMobile);

	/**
	 * 永久封禁时根据分销员id更改金额为0
	 * @param distributionUserId 分销员id
	 */
	void updateAmountByDistributionUserId(@Param("distributionUserId")Long distributionUserId);

	/**
	 * 根据分销员id 获取分销员钱包信息
	 * @param distributionUserId 分销员id
	 * @return 分销员钱包信息
	 */
    DistributionUserWallet getByDistributionUserId(@Param("distributionUserId") Long distributionUserId);

	/**
	 * 更新分销员钱包
	 * @param updateWallet 分销员钱包
	 * @return 受影响行数
	 */
    int updateWalletAmount(@Param("updateWallet") DistributionUserWallet updateWallet);
}
