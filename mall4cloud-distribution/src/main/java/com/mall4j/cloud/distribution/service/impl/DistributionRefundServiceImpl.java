package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.mall4j.cloud.api.multishop.bo.OrderChangeShopWalletAmountBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.feign.OrderRefundFeignClient;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.distribution.constant.DistributionUserIncomeStateEnum;
import com.mall4j.cloud.distribution.model.DistributionUserIncome;
import com.mall4j.cloud.distribution.model.DistributionUserWallet;
import com.mall4j.cloud.distribution.model.DistributionUserWalletBill;
import com.mall4j.cloud.distribution.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author cl
 * @date 2021-08-20 15:47:30
 */
@Service
public class DistributionRefundServiceImpl implements DistributionRefundService {

    private static final Logger logger = LoggerFactory.getLogger(DistributionRefundServiceImpl.class);
    @Autowired
    private DistributionUserService distributionUserService;
    @Autowired
    private DistributionUserBindService distributionUserBindService;
    @Autowired
    private DistributionConfigService distributionConfigService;
    @Autowired
    private DistributionUserWalletService distributionUserWalletService;
    @Autowired
    private DistributionUserWalletBillService distributionUserWalletBillService;
    @Autowired
    private DistributionUserIncomeService distributionUserIncomeService;
    @Autowired
    private OrderRefundFeignClient orderRefundFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundDistributionAmount(OrderChangeShopWalletAmountBO message) {
        if (Objects.isNull(message)) {
            return;
        }
        // 回退的分销佣金
        Long orderId = message.getOrderId();
        Long orderItemId = message.getOrderItemId();
        Long distributionAmount = message.getDistributionAmount();

        // 所有的订单分销流水记录查询出来
        List<DistributionUserIncome> distributionUserIncomes = distributionUserIncomeService.getByOrderId(orderId);
        if (CollUtil.isEmpty(distributionUserIncomes)) {
            logger.info("订单无分销信息，不用回退分销佣金");
            return;
        }
        List<DistributionUserIncome> returnAmountList = new ArrayList<>();
        // 整单退款
        if (Objects.isNull(orderItemId) || Objects.equals(Constant.ZERO_LONG,orderItemId)) {
            returnAmountList.addAll(distributionUserIncomes);
        } else {
            // 单个订单项退款
            DistributionUserIncome distributionUserIncome = distributionUserIncomes.stream().filter(item -> Objects.equals(orderItemId, item.getOrderItemId())).findAny().orElse(null);
            if (Objects.nonNull(distributionUserIncome)) {
                returnAmountList.add(distributionUserIncome);
            }
        }
        if (CollUtil.isEmpty(returnAmountList)) {
            return;
        }
        // 处理分销订单
        List<Long> updateIncomeIds = new ArrayList<>();

        DistributionUserWalletBill saveDistributionWalletBill = null;
        DistributionUserWallet updateWallet = null;
        for (DistributionUserIncome distributionUserIncome : returnAmountList) {
            Integer state = distributionUserIncome.getState();
            // 幂等， 已经回退的分销佣金，失效状态，就不用再次回退佣金了
            if (Objects.equals(DistributionUserIncomeStateEnum.INVALID.value(),state)) {
                continue;
            }
            if (Objects.isNull(updateWallet)) {
                DistributionUserWallet distributionUserWallet = distributionUserWalletService.getByDistributionUserId(distributionUserIncome.getDistributionUserId());
                if (distributionUserWallet == null) {
                    // 未找到分销员信息
                    throw new LuckException("未找到分销员信息");
                }
                updateWallet = new DistributionUserWallet();
                updateWallet.setWalletId(distributionUserWallet.getWalletId());
                // 添加钱包变动日志
                distributionUserWallet.setUnsettledAmount(distributionUserWallet.getUnsettledAmount() - distributionAmount);
                distributionUserWallet.setInvalidAmount(distributionUserWallet.getInvalidAmount() + distributionAmount);
                saveDistributionWalletBill = new DistributionUserWalletBill(distributionUserWallet, "分销订单退款失效奖励","Distribution order refund lapse bonus", -distributionAmount, Constant.ZERO_LONG, distributionAmount, Constant.ZERO_LONG, 0);

            }
            // 更新收入状态
            updateIncomeIds.add(distributionUserIncome.getIncomeId());
        }
        // 修改分销员分销钱包
        if (Objects.nonNull(updateWallet)) {
            updateWallet.setUnsettledAmount(-distributionAmount);
            updateWallet.setInvalidAmount(distributionAmount);
            distributionUserWalletService.updateWalletAmount(updateWallet);
            // 添加一条分销钱包流水
            distributionUserWalletBillService.save(saveDistributionWalletBill);
        }
        // 失效分销流水记录
        if (CollUtil.isNotEmpty(updateIncomeIds)) {
            distributionUserIncomeService.updateStateByIncomeIds(updateIncomeIds, DistributionUserIncomeStateEnum.INVALID.value());
        }

    }

    @Override
    public void refundSuccessDistributionAmount(OrderChangeShopWalletAmountBO message) {

    }

}
