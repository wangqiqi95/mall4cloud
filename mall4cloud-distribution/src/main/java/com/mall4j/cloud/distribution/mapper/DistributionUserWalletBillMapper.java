package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.model.DistributionUserWalletBill;
import com.mall4j.cloud.distribution.vo.DistributionUserWalletBillVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分销员钱包流水记录
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
public interface DistributionUserWalletBillMapper {

	/**
	 * 获取分销员钱包流水记录列表
	 * @return 分销员钱包流水记录列表
	 */
	List<DistributionUserWalletBill> list();

	/**
	 * 根据分销员钱包流水记录id获取分销员钱包流水记录
	 *
	 * @param id 分销员钱包流水记录id
	 * @return 分销员钱包流水记录
	 */
	DistributionUserWalletBill getById(@Param("id") Long id);

	/**
	 * 保存分销员钱包流水记录
	 * @param distributionUserWalletBill 分销员钱包流水记录
	 */
	void save(@Param("distributionUserWalletBill") DistributionUserWalletBill distributionUserWalletBill);

	/**
	 * 更新分销员钱包流水记录
	 * @param distributionUserWalletBill 分销员钱包流水记录
	 */
	void update(@Param("distributionUserWalletBill") DistributionUserWalletBill distributionUserWalletBill);

	/**
	 * 根据分销员钱包流水记录id删除分销员钱包流水记录
	 * @param id
	 */
	void deleteById(@Param("id") Long id);

	/**
	 * 获取分销员钱包流水记录列表
	 * @param userMobile 分销员手机号码
	 * @return
	 */
    List<DistributionUserWalletBillVO> walletBillPage(@Param("userMobile") String userMobile);

	/**
	 * 批量保存分销员钱包流水
	 * @param distributionUserWalletBills 分销员钱包流水
	 */
    void saveBatch(@Param("distributionUserWalletBills") List<DistributionUserWalletBill> distributionUserWalletBills);

}
