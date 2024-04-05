package com.mall4j.cloud.distribution.service.impl;

import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.distribution.constant.CommissionChangeTypeEnum;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.bo.EsOrderItemBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.feign.OrderRefundFeignClient;
import com.mall4j.cloud.api.order.vo.OrderRefundVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.distribution.bo.DistributionNotifyOrderAndShopBO;
import com.mall4j.cloud.distribution.model.DistributionCommissionAccount;
import com.mall4j.cloud.distribution.model.DistributionOrderCommissionLog;
import com.mall4j.cloud.distribution.service.DistributionCommissionAccountService;
import com.mall4j.cloud.distribution.service.DistributionOrderCommissionLogService;
import com.mall4j.cloud.distribution.service.DistributionSettlementService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分销订单结算service
 *
 * @Author ZengFanChang
 * @Date 2021/12/12
 */
@Service
public class DistributionSettlementServiceImpl implements DistributionSettlementService {

    private static final Logger logger = LoggerFactory.getLogger(DistributionSettlementServiceImpl.class);

    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private OrderRefundFeignClient orderRefundFeignClient;
    @Autowired
    private OnsMQTemplate distributionNotifyOrderShopTemplate;
    @Autowired
    private DistributionCommissionAccountService distributionCommissionAccountService;
    @Autowired
    private DistributionOrderCommissionLogService distributionOrderCommissionLogService;

    @Override
    public void processSettlement(List<Long> orderIds) {
        ServerResponseEntity<List<EsOrderBO>> esOrderResponse = orderFeignClient.getEsOrderList(orderIds);
        if (!esOrderResponse.isSuccess()) {
            logger.error(esOrderResponse.getMsg());
            throw new LuckException(esOrderResponse.getMsg());
        }
        esOrderResponse.getData().forEach(esOrderBO -> {
            if ((null == esOrderBO.getDistributionUserId() || 0 == esOrderBO.getDistributionUserId())
                    && (null == esOrderBO.getDevelopingUserId() || 0 == esOrderBO.getDevelopingUserId())) {
                logger.info("暂无分销信息 order:{}", esOrderBO);
                return;
            }

            ServerResponseEntity<Integer> returnProcessingItemCountResp = orderRefundFeignClient.countReturnProcessingItemByOrderId(esOrderBO.getOrderId());
            if (returnProcessingItemCountResp.isFail()) {
                logger.error("佣金结算失败：{}", returnProcessingItemCountResp.getMsg());
                return;
            }
            Integer processingRefundOrderCount = returnProcessingItemCountResp.getData();
            if (processingRefundOrderCount > 0) {
                logger.info("跳过本次结算，存在正在进行的退款订单 esOrderBO:{} processingRefundOrderCount:{}", esOrderBO, processingRefundOrderCount);
                return;
            }

            ServerResponseEntity<List<OrderRefundVO>> orderRefundList = orderRefundFeignClient.getOrderRefundByOrderIdAndRefundStatus(esOrderBO.getOrderId(), 5);
            if (!orderRefundList.isSuccess()) {
                logger.error("佣金结算失败：{}", orderRefundList.getMsg());
                return;
            }

            // 订单分销佣金
            long totalDistributionAmount = esOrderBO.getOrderItems().stream().mapToLong(EsOrderItemBO::getDistributionAmount).sum();
            // 订单发展佣金
            long totalDevelopingAmount = esOrderBO.getOrderItems().stream().mapToLong(EsOrderItemBO::getDistributionParentAmount).sum();

            logger.info("佣金结算 esOrderBO:{}, totalDistributionAmount:{}, totalDevelopingAmount:{}", esOrderBO, totalDistributionAmount, totalDevelopingAmount);

            boolean hasRefund = CollectionUtils.isNotEmpty(orderRefundList.getData());
            long totalRefundDistributionAmount = 0L;
            long totalRefundDevelopingAmount = 0L;
            if (hasRefund) {
                // 根据订单项id为key聚合的订单项Map
                Map<Long, EsOrderItemBO> groupByOrderItemIdOrderItemMap = esOrderBO.getOrderItems()
                        .stream()
                        .collect(
                                Collectors.toMap(EsOrderItemBO::getOrderItemId, esOrderItemBO -> esOrderItemBO)
                        );
                // 根据订单项id为key聚合的退款数量Map
                Map<Long, Integer> groupByOrderItemIdRefundCountMap = orderRefundList.getData()
                        .stream()
                        .collect(
                                Collectors.groupingBy(OrderRefundVO::getOrderItemId, Collectors.summingInt(OrderRefundVO::getRefundCount)
                                )
                        );

                for (Map.Entry<Long, Integer> orderItemIdRefundCountEntry : groupByOrderItemIdRefundCountMap.entrySet()) {
                    logger.info("退单成功商品 orderItemId:{}, refundCount:{}", orderItemIdRefundCountEntry.getKey(), orderItemIdRefundCountEntry.getValue());
                    // 全部退单
                    if (orderItemIdRefundCountEntry.getKey() == 0) {
                        esOrderBO.getOrderItems().forEach(esOrderItemBO -> esOrderItemBO.setDistributionAmount(0L));
                        esOrderBO.getOrderItems().forEach(esOrderItemBO -> esOrderItemBO.setDistributionParentAmount(0L));
                        totalRefundDistributionAmount = totalDistributionAmount;
                        totalRefundDevelopingAmount = totalDevelopingAmount;
                        break;
                    } else {
                        EsOrderItemBO orderItemBO = groupByOrderItemIdOrderItemMap.get(orderItemIdRefundCountEntry.getKey());
                        // 当前商品全部退
                        if (orderItemIdRefundCountEntry.getValue() == 0) {
                            totalRefundDistributionAmount += orderItemBO.getDistributionAmount();
                            totalRefundDevelopingAmount += orderItemBO.getDistributionParentAmount();
                            orderItemBO.setDistributionAmount(0L);
                            orderItemBO.setDistributionParentAmount(0L);
                        } else if (orderItemIdRefundCountEntry.getValue().equals(orderItemBO.getCount())) {
                            totalRefundDistributionAmount += orderItemBO.getDistributionAmount();
                            totalRefundDevelopingAmount += orderItemBO.getDistributionParentAmount();
                            orderItemBO.setDistributionAmount(0L);
                            orderItemBO.setDistributionParentAmount(0L);
                        } else {
                            long refundDistributionAmount = orderItemBO.getDistributionAmount() / orderItemBO.getCount() * orderItemIdRefundCountEntry.getValue();
                            long refundDistributionParentAmount = orderItemBO.getDistributionParentAmount() / orderItemBO.getCount() * orderItemIdRefundCountEntry.getValue();
                            orderItemBO.setDistributionAmount(orderItemBO.getDistributionAmount() - refundDistributionAmount);
                            orderItemBO.setDistributionParentAmount(orderItemBO.getDistributionParentAmount() - refundDistributionParentAmount);
                            totalRefundDistributionAmount += refundDistributionAmount;
                            totalRefundDevelopingAmount += refundDistributionParentAmount;
                        }
                        groupByOrderItemIdOrderItemMap.put(orderItemIdRefundCountEntry.getKey(), orderItemBO);
                    }
                }
                esOrderBO.setOrderItems(new ArrayList<>(groupByOrderItemIdOrderItemMap.values()));

                DistributionNotifyOrderAndShopBO message = new DistributionNotifyOrderAndShopBO();
                message.setDistributionOrderItems(esOrderBO.getOrderItems());
                SendResult sendResult = distributionNotifyOrderShopTemplate.syncSend(message);
                if (sendResult == null || sendResult.getMessageId() == null) {
                    return;
                }
            }
            // 重算分销、发展佣金总数
            totalDistributionAmount = esOrderBO.getOrderItems().stream().mapToLong(EsOrderItemBO::getDistributionAmount).sum();
            totalDevelopingAmount = esOrderBO.getOrderItems().stream().mapToLong(EsOrderItemBO::getDistributionParentAmount).sum();

            logger.info("结算计算完成 esOrderBO:{}, totalDistributionAmount:{}, totalDevelopingAmount:{}, totalRefundDistributionAmount:{}, totalRefundDevelopingAmount:{}",
                    esOrderBO, totalDistributionAmount, totalDevelopingAmount, totalRefundDistributionAmount, totalRefundDevelopingAmount);

            if (esOrderBO.getDistributionStatus() == 0) {
                settlementDistributionAmount(esOrderBO.getDistributionUserType(), esOrderBO.getDistributionUserId(), esOrderBO, totalDistributionAmount);
                refundWaitSettlementDistributionAmountIfNecessary(esOrderBO.getDistributionUserType(), esOrderBO.getDistributionUserId(), esOrderBO, totalRefundDistributionAmount);
            } else {
                logger.info("跳过本次分销佣金结算，该订单分销佣金已被结算过 orderNumber:{}, distributionStatus:{}", esOrderBO.getOrderNumber(), esOrderBO.getDistributionStatus());
            }

            if (esOrderBO.getDevelopingStatus() == 0) {
                settlementDevelopingAmount(1, esOrderBO.getDevelopingUserId(), esOrderBO, totalDevelopingAmount);
                refundWaitSettlementDevelopingAmount(1, esOrderBO.getDevelopingUserId(), esOrderBO, totalRefundDevelopingAmount);
            } else {
                logger.info("跳过本次发展佣金结算，该订单发展佣金已被结算过 orderNumber:{}, developingStatus:{}", esOrderBO.getOrderNumber(), esOrderBO.getDevelopingStatus());
            }
        });
    }

    /**
     * 回退待结算发展佣金
     */
    private void refundWaitSettlementDevelopingAmount(int identityType, Long userId, EsOrderBO esOrderBO, long totalRefundDevelopingAmount) {
//        if (totalRefundDevelopingAmount == 0) {
//            logger.info("发展佣金为空 esOrderBO:{}, userId:{}, identityType:{}", esOrderBO, userId, identityType);
//            return;
//        }
        DistributionCommissionAccount account = null;
        if (userId > 0) {
            account = getAndCheckDistributionCommissionAccount(userId, identityType);
            if (account == null) {
                logger.error("发展佣金结算回退失败 用户发展佣金账户不存在 userId:{}, identityType:{}", userId, identityType);
                return;
            }
            if (totalRefundDevelopingAmount > account.getWaitCommission()) {
                logger.error("回退待结算发展佣金不足 totalRefundDevelopingAmount:{}, account:{}", totalRefundDevelopingAmount, account);
                return;
            }
        }
        List<Long> orderIds = new ArrayList<>();
        orderIds.add(esOrderBO.getOrderId());
        orderFeignClient.updateDevelopingStatusBatchById(orderIds, 1, new Date(), null);

        if (totalRefundDevelopingAmount > 0 && account != null) {
            distributionCommissionAccountService.updateCommission(account, totalRefundDevelopingAmount, CommissionChangeTypeEnum.REDUCE_WAIT_SETTLE);
        }
    }

    /**
     * 回退待结算分销佣金
     */
    private void refundWaitSettlementDistributionAmountIfNecessary(Integer identityType, Long userId, EsOrderBO esOrderBO, long totalRefundDistributionAmount) {
//        if (totalRefundDistributionAmount == 0) {
//            logger.info("回退待结算分销佣金为空 esOrderBO:{}, userId:{}, identityType:{}", esOrderBO, userId, identityType);
//            return;
//        }
        DistributionCommissionAccount account = null;
        if (userId > 0) {
            account = getAndCheckDistributionCommissionAccount(userId, identityType);
            if (account == null) {
                logger.error("分销佣金结算回退失败 用户分销佣金账户不存在 userId:{}, identityType:{}", userId, identityType);
                return;
            }
            if (totalRefundDistributionAmount > account.getWaitCommission()) {
                logger.error("回退待结算分销佣金不足 totalRefundDistributionAmount:{}, account:{}", totalRefundDistributionAmount, account);
                return;
            }
        }
        List<Long> orderIds = new ArrayList<>();
        orderIds.add(esOrderBO.getOrderId());
        orderFeignClient.updateDistributionStatusBatchById(orderIds, 1, new Date(), null);
        if (totalRefundDistributionAmount > 0 && account != null) {
            distributionCommissionAccountService.updateCommission(account, totalRefundDistributionAmount, CommissionChangeTypeEnum.REDUCE_WAIT_SETTLE);
        }
    }

    /**
     * 结算分销佣金
     */
    private void settlementDistributionAmount(Integer identityType, Long userId, EsOrderBO esOrderBO, Long totalDistributionAmount) {
//        if (totalDistributionAmount == 0) {
//            logger.info("分销佣金为空 esOrderBO:{}, userId:{}, identityType:{}", esOrderBO, userId, identityType);
//            return;
//        }
        DistributionCommissionAccount account = null;
        if (userId > 0) {
            account = getAndCheckDistributionCommissionAccount(userId, identityType);
            if (account == null) {
                logger.error("分销佣金结算失败 用户分销佣金账户不存在 userId:{}, identityType:{}", userId, identityType);
                return;
            }
            if (totalDistributionAmount > account.getWaitCommission()) {
                logger.error("待结算分销佣金不足 totalDistributionAmount:{}, account:{}", totalDistributionAmount, account);
                return;
            }
        }
        List<Long> orderIds = new ArrayList<>();
        orderIds.add(esOrderBO.getOrderId());
        orderFeignClient.updateDistributionStatusBatchById(orderIds, 1, new Date(), null);

        if (totalDistributionAmount > 0 && account != null) {
            distributionCommissionAccountService.updateCommission(account, totalDistributionAmount, CommissionChangeTypeEnum.ADD_CAN_WITHDRAW);
        }
        if (account != null) {
            DistributionOrderCommissionLog log = new DistributionOrderCommissionLog();
            log.setCommissionAmount(totalDistributionAmount);
            log.setCommissionStatus(1);
            log.setCommissionType(1);
            log.setOrderId(esOrderBO.getOrderId());
            log.setIdentityType(account.getIdentityType());
            log.setUserId(account.getUserId());
            log.setStoreId(account.getStoreId());
            distributionOrderCommissionLogService.save(log);
        }
    }

    /**
     * 结算发展佣金
     */
    private void settlementDevelopingAmount(Integer identityType, Long userId, EsOrderBO esOrderBO, Long totalDevelopingAmount) {
//        if (totalDevelopingAmount == 0) {
//            logger.info("发展佣金为空 esOrderBO:{}, userId:{}, identityType:{}", esOrderBO, userId, identityType);
//            return;
//        }
        DistributionCommissionAccount account = null;
        if (userId > 0) {
            account = getAndCheckDistributionCommissionAccount(userId, identityType);
            if (account == null) {
                logger.error("发展佣金结算失败 用户发展佣金账户不存在 userId:{}, identityType:{}", userId, identityType);
                return;
            }
            if (totalDevelopingAmount > account.getWaitCommission()) {
                logger.error("待结算发展佣金不足 totalDevelopingAmount:{}, account:{}", totalDevelopingAmount, account);
                return;
            }
        }
        List<Long> orderIds = new ArrayList<>();
        orderIds.add(esOrderBO.getOrderId());
        orderFeignClient.updateDevelopingStatusBatchById(orderIds, 1, new Date(), null);

        if (totalDevelopingAmount > 0 && account != null) {
            distributionCommissionAccountService.updateCommission(account, totalDevelopingAmount, CommissionChangeTypeEnum.ADD_CAN_WITHDRAW);
        }

        if (account != null) {
            DistributionOrderCommissionLog log = new DistributionOrderCommissionLog();
            log.setCommissionAmount(totalDevelopingAmount);
            log.setCommissionStatus(1);
            log.setCommissionType(2);
            log.setOrderId(esOrderBO.getOrderId());
            log.setIdentityType(account.getIdentityType());
            log.setUserId(account.getUserId());
            log.setStoreId(account.getStoreId());
            distributionOrderCommissionLogService.save(log);
        }
    }

    /**
     * 获取并校验分销佣金账户
     */
    private DistributionCommissionAccount getAndCheckDistributionCommissionAccount(Long userId, Integer identityType) {
        DistributionCommissionAccount account = distributionCommissionAccountService.getByUser(userId, identityType);
        if (null == account) {
            logger.info("用户佣金账户不存在 identityType:{}, userId:{}", identityType, userId);
            return null;
        }
//        if (account.getStatus() == 0) {
//            logger.info("{}用户佣金已禁用 account:{}",
//                    identityType == 1 ? "发展" : "分销",
//                    account);
//            return null;
//        }
        return account;
    }
}
