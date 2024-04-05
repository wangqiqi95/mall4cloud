package com.mall4j.cloud.distribution.service;

import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.AppDistributionWithdrawCashDTO;
import com.mall4j.cloud.distribution.dto.DistributionWithdrawCashDTO;
import com.mall4j.cloud.distribution.dto.RangeTimeDTO;
import com.mall4j.cloud.distribution.model.DistributionWithdrawCash;
import com.mall4j.cloud.distribution.vo.AppDistributionWithdrawCashVO;
import com.mall4j.cloud.distribution.vo.DistributionUserVO;
import com.mall4j.cloud.distribution.vo.DistributionWithdrawCashVO;

import java.math.BigDecimal;

/**
 * 分销员提现记录
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
public interface DistributionWithdrawCashService {

	/**
	 * 分页获取分销员提现记录列表
	 * @param pageDTO 分页参数
	 * @param userMobile 分销员手机号
	 * @param distributionWithdrawCashDTO 分销提现信息
	 * @return 分销员提现记录列表分页数据
	 */
	PageVO<DistributionWithdrawCashVO> page(PageDTO pageDTO, String userMobile, DistributionWithdrawCashDTO distributionWithdrawCashDTO);

	/**
	 * 根据分销员提现记录id获取分销员提现记录
	 *
	 * @param withdrawCashId 分销员提现记录id
	 * @return 分销员提现记录
	 */
	DistributionWithdrawCash getByWithdrawCashId(Long withdrawCashId);

	/**
	 * 保存分销员提现记录
	 * @param distributionWithdrawCash 分销员提现记录
	 */
	void save(DistributionWithdrawCash distributionWithdrawCash);

	/**
	 * 更新分销员提现记录
	 * @param distributionWithdrawCash 分销员提现记录
	 */
	void update(DistributionWithdrawCash distributionWithdrawCash);

	/**
	 * 根据分销员提现记录id删除分销员提现记录
	 * @param withdrawCashId 分销员提现记录id
	 */
	void deleteById(Long withdrawCashId);

	/**
	 * 分页获取分销员提现申请记录
	 * @param pageDTO 分页参数
	 * @param distributionUserId 分销员id
	 * @return 分销员提现申请记录
	 */
	PageVO<AppDistributionWithdrawCashVO> pageByDistributionUserId(PageDTO pageDTO, Long distributionUserId);

	/**
	 * 发起提现申请
	 * @param distributionWithdrawCashDTO 提现金额信息
	 * @param distributionUserVO 分销员信息
	 */
	void apply(AppDistributionWithdrawCashDTO distributionWithdrawCashDTO, DistributionUserVO distributionUserVO);

	/**
	 * 根据时间区间获取用户的提现次数
	 * @param rangeTimeDTO 时间范围
	 * @param distributionUserId 分销员id
	 * @return 分销员提现申请记录数量
	 */
    Integer getCountByRangeTimeAndDistributionUserId(RangeTimeDTO rangeTimeDTO, Long distributionUserId);

	/**
	 * 查看分销员总提现金额
	 * @param walletId 钱包id
	 * @return 总提现金额
	 */
	BigDecimal getUserTotalWithdrawCash(Long walletId);
}
