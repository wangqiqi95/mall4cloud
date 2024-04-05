package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.distribution.dto.DistributionWithdrawCashDTO;
import com.mall4j.cloud.distribution.dto.RangeTimeDTO;
import com.mall4j.cloud.distribution.model.DistributionWithdrawCash;
import com.mall4j.cloud.distribution.vo.AppDistributionWithdrawCashVO;
import com.mall4j.cloud.distribution.vo.DistributionWithdrawCashVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 分销员提现记录
 *
 * @author cl
 * @date 2021-08-09 14:14:10
 */
public interface DistributionWithdrawCashMapper {

	/**
	 * 获取分销员提现记录列表
	 * @return 分销员提现记录列表
	 */
	List<DistributionWithdrawCash> list();

	/**
	 * 根据分销员提现记录id获取分销员提现记录
	 *
	 * @param withdrawCashId 分销员提现记录id
	 * @return 分销员提现记录
	 */
	DistributionWithdrawCash getByWithdrawCashId(@Param("withdrawCashId") Long withdrawCashId);

	/**
	 * 保存分销员提现记录
	 * @param distributionWithdrawCash 分销员提现记录
	 */
	void save(@Param("distributionWithdrawCash") DistributionWithdrawCash distributionWithdrawCash);

	/**
	 * 更新分销员提现记录
	 * @param distributionWithdrawCash 分销员提现记录
	 */
	void update(@Param("distributionWithdrawCash") DistributionWithdrawCash distributionWithdrawCash);

	/**
	 * 根据分销员提现记录id删除分销员提现记录
	 * @param withdrawCashId
	 */
	void deleteById(@Param("withdrawCashId") Long withdrawCashId);

	/**
	 * 分页获取分销员提现记录列表
	 * @param userMobile 分销员手机号
	 * @param distributionWithdrawCashDTO 分销提现信息
	 * @return 分销员提现记录列表分页数据
	 */
    List<DistributionWithdrawCashVO> withdrawCashPage(@Param("userMobile") String userMobile, @Param("distributionWithdrawCashDTO") DistributionWithdrawCashDTO distributionWithdrawCashDTO);

	/**
	 * 永久封禁时，根据用户id更新其提现中的记录为拒绝提现
	 * @param distributionUserId 用户id
	 */
	void updateUserByDistributionUserId(@Param("distributionUserId") Long distributionUserId);

	/**
	 * 获取分销员提现申请记录
	 * @param distributionUserId 分销员id
	 * @return 分销员提现申请记录
	 */
    List<AppDistributionWithdrawCashVO> pageByDistributionUserId(@Param("distributionUserId") Long distributionUserId);

	/**
	 * 根据时间区间获取用户的提现次数
	 * @param rangeTime 时间范围
	 * @param distributionUserId 分销员id
	 * @return 分销员提现申请记录数量
	 */
    Integer getCountByRangeTimeAndDistributionUserId(@Param("rangeTime") RangeTimeDTO rangeTime, @Param("distributionUserId") Long distributionUserId);

    /**
	 * 查看分销员总提现金额
	 * @param walletId 钱包id
	 * @return 总提现金额(分)
	 */
	BigDecimal getUserTotalWithdrawCash(@Param("walletId") Long walletId);
}
