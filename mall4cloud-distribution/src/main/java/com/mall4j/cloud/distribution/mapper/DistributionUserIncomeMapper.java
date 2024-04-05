package com.mall4j.cloud.distribution.mapper;

import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionUserIncomeDTO;
import com.mall4j.cloud.distribution.model.DistributionUserIncome;
import com.mall4j.cloud.distribution.vo.DistributionOrderVO;
import com.mall4j.cloud.distribution.vo.DistributionUserIncomeOrderVO;
import com.mall4j.cloud.distribution.vo.DistributionUserIncomeVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 分销收入记录信息
 *
 * @author cl
 * @date 2021-08-09 14:14:09
 */
public interface DistributionUserIncomeMapper {

	/**
	 * 获取分销收入记录信息列表
	 * @return 分销收入记录信息列表
	 */
	List<DistributionUserIncome> list();

	List<DistributionUserIncomeVO> getDistributionUserIncomeList(@Param("distributionUserId") Long distributionUserId);

	/**
	 * 根据分销收入记录信息id获取分销收入记录信息
	 *
	 * @param incomeId 分销收入记录信息id
	 * @return 分销收入记录信息
	 */
	DistributionUserIncome getByIncomeId(@Param("incomeId") Long incomeId);

	/**
	 * 保存分销收入记录信息
	 * @param distributionUserIncome 分销收入记录信息
	 */
	void save(@Param("distributionUserIncome") DistributionUserIncome distributionUserIncome);

	/**
	 * 更新分销收入记录信息
	 * @param distributionUserIncome 分销收入记录信息
	 */
	void update(@Param("distributionUserIncome") DistributionUserIncome distributionUserIncome);

	/**
	 * 根据分销收入记录信息id删除分销收入记录信息
	 * @param incomeId
	 */
	void deleteById(@Param("incomeId") Long incomeId);

	/**
	 * 分页获取分销推广效果列表
	 * @param distributionUserIncomeDTO 查询参数
	 * @param userMobile 分销员手机号
	 * @param shopId 店铺id
	 * @return
	 */
    List<DistributionUserIncomeVO> effectPage(@Param("distributionUserIncomeDTO") DistributionUserIncomeDTO distributionUserIncomeDTO, @Param("userMobile") String userMobile, @Param("shopId") Long shopId);

	/**
	 * 获取销售记录
	 * @param distributionUserIncomeDTO
	 * @return
	 */
	List<DistributionUserIncomeVO> listSalesRecord(@Param("distributionUserIncomeDTO") DistributionUserIncomeDTO distributionUserIncomeDTO);

	/**
	 * 根据分销员id更新收入状态
	 * @param distributionUserId 分销员id
	 * @param state 收入状态
	 */
	void updateStateByDistributionUserId(@Param("distributionUserId") Long distributionUserId, @Param("state") Integer state);

	/**
	 * 分销员推广订单信息
	 * @param distributionUserId 分销员id
	 * @return 分销员推广订单信息
	 */
    List<DistributionOrderVO> getDistributionOrderByDistributionUserId(@Param("distributionUserId") Long distributionUserId);

	/**
	 * 统计分销员某时间段收入
	 * @param distributionUserId 分销员id
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return 收入金额
	 */
	Double statisticsDisUserIncome(@Param("distributionUserId") Long distributionUserId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

	/**
	 * 通过状态获取我的推广订单(0:待支付 1:待结算 2:已结算 -1:订单失效)
	 * @param distributionUserId
	 * @param state 收入状态 0:待支付 1:待结算 2:已结算 -1:订单失效
	 * @return
	 */
	List<DistributionUserIncomeOrderVO> getMyPromotionOrderByState(@Param("distributionUserId") Long distributionUserId, @Param("state") Integer state);

	/**
	 * 统计分销流水记录
	 * @param orderId 订单号
	 * @param orderItemId 订单项id
	 * @param state 状态
	 * @return 数量
	 */
    int countByOrderIdAndOrderItemId(@Param("orderId") Long orderId, @Param("orderItemId") Long orderItemId, @Param("state") Integer state);

    /**
	 * 分销流水列表
	 * @param orderId 订单号
	 * @return 分销流水列表
	 */
    List<DistributionUserIncome> getByOrderId(@Param("orderId") Long orderId);

	/**
	 * 批量修改分销流水状态
	 * @param incomeIds 分销流水id
	 * @param state 状态
	 * @return 受影响行数
	 */
    int updateStateByIncomeIds(@Param("incomeIds") List<Long> incomeIds, @Param("state") Integer state);

	/**
	 * 查询待结算的分销流水记录列表
	 * @return 待结算的分销流水记录
	 */
	List<DistributionUserIncome> listWaitCommissionSettlement();

}
