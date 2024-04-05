package com.mall4j.cloud.distribution.service;

import cn.hutool.core.date.DateTime;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.distribution.dto.DistributionUserIncomeDTO;
import com.mall4j.cloud.distribution.model.DistributionUserIncome;
import com.mall4j.cloud.distribution.vo.DistributionOrderVO;
import com.mall4j.cloud.distribution.vo.DistributionUserIncomeOrderVO;
import com.mall4j.cloud.distribution.vo.DistributionUserIncomeVO;
import com.mall4j.cloud.distribution.vo.StatisticsDisUserIncomeVO;

import java.util.Date;
import java.util.List;

/**
 * 分销收入记录信息
 *
 * @author cl
 * @date 2021-08-09 14:14:09
 */
public interface DistributionUserIncomeService {

    /**
     * 分页获取分销收入记录信息列表
     *
     * @param pageDTO 分页参数
     * @return 分销收入记录信息列表分页数据
     */
    PageVO<DistributionUserIncome> page(PageDTO pageDTO);

    /**
     * 查询分销员收入记录
     *
     * @param pageDTO            分页参数
     * @param distributionUserId 分销员id
     * @return 收入记录
     */
    PageVO<DistributionUserIncomeVO> getDistributionUserIncomePage(PageDTO pageDTO, Long distributionUserId);

    /**
     * 根据分销收入记录信息id获取分销收入记录信息
     *
     * @param incomeId 分销收入记录信息id
     * @return 分销收入记录信息
     */
    DistributionUserIncome getByIncomeId(Long incomeId);

    /**
     * 保存分销收入记录信息
     *
     * @param distributionUserIncome 分销收入记录信息
     */
    void save(DistributionUserIncome distributionUserIncome);

    /**
     * 更新分销收入记录信息
     *
     * @param distributionUserIncome 分销收入记录信息
     */
    void update(DistributionUserIncome distributionUserIncome);

    /**
     * 根据分销收入记录信息id删除分销收入记录信息
     *
     * @param incomeId 分销收入记录信息id
     */
    void deleteById(Long incomeId);

    /**
     * 分页获取分销推广效果列表
     *
     * @param pageDTO                   分页参数
     * @param distributionUserIncomeDTO 收入
     * @param userMobile                分销员手机号
     * @param shopId                    店铺id
     * @return
     */
    PageVO<DistributionUserIncomeVO> effectPage(PageDTO pageDTO, DistributionUserIncomeDTO distributionUserIncomeDTO, String userMobile, Long shopId);

    /**
     * 分页获取销售记录
     *
     * @param pageDTO
     * @param distributionUserIncomeDTO
     * @return
     */
    PageVO<DistributionUserIncomeVO> pageSalesRecord(PageDTO pageDTO, DistributionUserIncomeDTO distributionUserIncomeDTO);

    /**
     * 分销员推广订单信息
     *
     * @param pageDTO            分页参数
     * @param distributionUserId 分销员id
     * @return 分销员推广订单信息
     */
    PageVO<DistributionOrderVO> getDistributionOrderByDistributionUserId(PageDTO pageDTO, Long distributionUserId);

    /**
     * 统计分销员当日收入
     *
     * @param distributionUserId 分销员id
     * @return 收入金额
     */
    StatisticsDisUserIncomeVO statisticsDistributionUserIncome(Long distributionUserId);

    /**
     * 通过状态获取我的推广订单(0:待支付 1:待结算 2:已结算 -1:订单失效)
     *
     * @param pageDTO
     * @param distributionUserId
     * @param state 收入状态 0:待支付 1:待结算 2:已结算 -1:订单失效
     * @return
     */
    PageVO<DistributionUserIncomeOrderVO> getMyPromotionOrderByState(PageDTO pageDTO, Long distributionUserId, Integer state);

	/**
	 * 统计分销流水记录
	 *
	 * @param orderId 订单号
	 * @param orderItemId 订单项id
	 * @param state 状态
	 * @return 数量
	 */
    int countByOrderIdAndOrderItemId(Long orderId, Long orderItemId, Integer state);

    /**
     * 分销流水列表
     * @param orderId 订单号
     * @return 分销流水列表
     */
    List<DistributionUserIncome> getByOrderId(Long orderId);

    /**
     * 批量修改分销流水状态
     * @param incomeIds 分销流水id
     * @param state 状态
     * @return 受影响行数
     */
    int updateStateByIncomeIds(List<Long> incomeIds, Integer state);

    /**
     * 分销员佣金结算处理
     * @param date 最后结算时间
     */
    void commissionSettlementHandle(Date date);
}
