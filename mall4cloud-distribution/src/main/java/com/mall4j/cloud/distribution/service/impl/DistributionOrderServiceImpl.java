package com.mall4j.cloud.distribution.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.coupon.feign.TCouponFeignClient;
import com.mall4j.cloud.api.distribution.bo.DistributionSpuBO;
import com.mall4j.cloud.api.distribution.constant.CommissionChangeTypeEnum;
import com.mall4j.cloud.api.distribution.dto.DistributionCommissionRateDTO;
import com.mall4j.cloud.api.distribution.vo.DistributionCommissionRateVO;
import com.mall4j.cloud.api.distribution.vo.DistributionSpuVO;
import com.mall4j.cloud.api.multishop.bo.ShopWalletBO;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.bo.EsOrderItemBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.platform.feign.TentacleContentFeignClient;
import com.mall4j.cloud.api.platform.vo.DistributionConfigApiVO;
import com.mall4j.cloud.common.constant.Constant;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.bo.PayNotifyBO;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.distribution.bo.DistributionNotifyOrderAndShopBO;
import com.mall4j.cloud.distribution.constant.*;
import com.mall4j.cloud.distribution.model.*;
import com.mall4j.cloud.distribution.service.*;
import com.mall4j.cloud.distribution.vo.DistributionAwardDataVO;
import com.mall4j.cloud.distribution.vo.DistributionUserVO;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cl
 * @date 2021-08-18 08:40:26
 */
@Service
public class DistributionOrderServiceImpl implements DistributionOrderService {

    private static final Logger logger = LoggerFactory.getLogger(DistributionOrderServiceImpl.class);

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
    private DistributionSpuService distributionSpuService;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private OnsMQTemplate distributionNotifyOrderShopTemplate;
    @Autowired
    private MapperFacade mapperFacade;

    @Autowired
    private DistributionCommissionAccountService distributionCommissionAccountService;

    @Autowired
    private DistributionCommissionService distributionCommissionService;

    @Autowired
    private TentacleContentFeignClient tentacleContentFeignClient;

    @Autowired
    private TCouponFeignClient couponFeignClient;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payNotifyDistributionOrder(PayNotifyBO payNotifyBO) {
        // OrderNotifyConsumer 通知来源类
        // 获取分销设置
        DistributionConfigApiVO distributionConfig = distributionConfigService.getDistributionConfig();
        if (Objects.isNull(distributionConfig) || !Objects.equals(1, distributionConfig.getDistributionSwitch())) {
            logger.info("分销未开启，结束分销");
            return;
        }
        // 获取订单数据
        List<Long> orderIds = payNotifyBO.getOrderIds();
        ServerResponseEntity<List<EsOrderBO>> esOrderResponse = orderFeignClient.getEsOrderList(orderIds);
        if (!esOrderResponse.isSuccess()) {
            logger.error(esOrderResponse.getMsg());
            throw new LuckException(esOrderResponse.getMsg());
        }
        List<EsOrderBO> esOrderBOList = esOrderResponse.getData();
        if (CollUtil.isEmpty(esOrderBOList)) {
            logger.info("未查询到对应的订单，结束分销 orderIds： " + orderIds.toString());
            return;
        }
        // 过滤掉shopId = 0的订单，以及积分订单（分销订单不包含积分订单）
        List<EsOrderBO> orders = esOrderBOList.stream()
                .filter(item -> !Objects.equals(item.getShopId(), Constant.PLATFORM_SHOP_ID)
                        && !Objects.equals(item.getOrderType(), OrderType.SCORE.value()))
                .collect(Collectors.toList());
        if (CollUtil.isEmpty(orders)) {
            logger.info("没有可以进行分销的订单，结束分销 orderIds： " + orderIds.toString());
            return;
        }
        // 获取订所有的订单项
        List<EsOrderItemBO> orderItems = new ArrayList<>();
        orders.forEach(item -> {
            orderItems.addAll(item.getOrderItems());
        });
        // 订单所有的商品
        if (CollUtil.isEmpty(orderItems)) {
            logger.info("未获取到订单项，停止分销");
            return;
        }
        List<Long> spuIds = orderItems.stream().map(EsOrderItemBO::getSpuId).collect(Collectors.toList());
        // 查询出上架的所有分销商品信息（如何有商品没有在分销活动中上架，那么此商品就不进行分销）
        List<DistributionSpu> distributionSpuList = distributionSpuService.getByStateAndSpuIds(DistributionSpuStatus.PUT_SHELF.value(), spuIds);
        if (CollUtil.isEmpty(distributionSpuList)) {
            logger.info("未获取到订单商品，停止分销");
            return;
        }
        // 开始进行处理订单分销
        handleDistributionOrder(distributionConfig, orders, orderItems, distributionSpuList);
    }


    @Override
    public void paySuccessNotifyDistributionOrder(PayNotifyBO payNotifyBO) {
        // 获取订单数据
        List<Long> orderIds = payNotifyBO.getOrderIds();
        ServerResponseEntity<List<EsOrderBO>> esOrderResponse = orderFeignClient.getEsOrderList(orderIds);
        if (!esOrderResponse.isSuccess()) {
            logger.error(esOrderResponse.getMsg());
            throw new LuckException(esOrderResponse.getMsg());
        }
        List<EsOrderBO> esOrderBOList = esOrderResponse.getData();
        if (CollUtil.isEmpty(esOrderBOList)) {
            logger.info("未查询到对应的订单，结束分销 orderIds： " + orderIds.toString());
            return;
        }



        esOrderBOList.stream().filter(item -> !Objects.equals(item.getOrderType(), OrderType.SCORE.value())).forEach(esOrderBO -> {
            if ((null == esOrderBO.getDistributionUserId() || 0 == esOrderBO.getDistributionUserId())
                    && (null == esOrderBO.getDevelopingUserId() || 0 == esOrderBO.getDevelopingUserId())){
                logger.info("暂无分销信息 order:{}", esOrderBO);
                return;
            }
            boolean isStaff = esOrderBO.getDistributionUserType() == 1;
            List<EsOrderItemBO> orderItems = esOrderBO.getOrderItems();
            if (CollectionUtils.isEmpty(orderItems)) {
                logger.info("未查询到订单商品信息 order:{}", esOrderBO);
                return;
            }

            List<Long> spuIds = orderItems.stream().map(EsOrderItemBO::getSpuId).collect(Collectors.toList());
            DistributionCommissionRateDTO dto = new DistributionCommissionRateDTO();
            dto.setStoreId(esOrderBO.getDistributionStoreId());
            dto.setSpuIdList(spuIds);
            dto.setLimitOrderType(0);
            ServerResponseEntity<Boolean> useEnterpriseCoupon = couponFeignClient.isUseEnterpriseCoupon(esOrderBO.getOrderId());
            if (useEnterpriseCoupon.isSuccess() && null != useEnterpriseCoupon.getData() && useEnterpriseCoupon.getData()){
                logger.info("代购订单 orderId:{}", esOrderBO.getOrderId());
                dto.setLimitOrderType(1);
            }
            Map<Long, DistributionCommissionRateVO> rateMap = distributionCommissionService.getDistributionCommissionRate(dto);
            if (MapUtils.isEmpty(rateMap)) {
                logger.info("暂无参与分佣商品 spuIds:{}", spuIds);
                return;
            }

            orderItems.forEach(esOrderItemBO -> {
                DistributionCommissionRateVO rate = rateMap.get(esOrderItemBO.getSpuId());
                if (null == rate) {
                    return;
                }
                logger.info("商品佣金比例 rate:{}, orderId:{}", rate, esOrderBO.getOrderId());
                if (null != esOrderBO.getDistributionUserId() && esOrderBO.getDistributionUserId() > 0L){
                    if (isStaff) {
                        esOrderItemBO.setDistributionAmountRate(rate.getGuideRate());
                        esOrderItemBO.setDistributionAmount((long) (esOrderItemBO.getActualTotal() * (Optional.ofNullable(rate.getGuideRate()).orElse(new BigDecimal("0")).divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_DOWN).doubleValue())));
                    } else {
                        esOrderItemBO.setDistributionAmountRate(rate.getShareRate());
                        esOrderItemBO.setDistributionAmount((long) (esOrderItemBO.getActualTotal() * (Optional.ofNullable(rate.getShareRate()).orElse(new BigDecimal("0")).divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_DOWN).doubleValue())));
                    }
                } else {
                    esOrderItemBO.setDistributionAmount(0L);
                }
                if (null != esOrderBO.getDevelopingUserId() && esOrderBO.getDevelopingUserId() > 0L) {
                    esOrderItemBO.setDistributionParentAmountRate(rate.getDevelopRate());
                    esOrderItemBO.setDistributionParentAmount((long) (esOrderItemBO.getActualTotal() * (Optional.ofNullable(rate.getDevelopRate()).orElse(new BigDecimal("0")).divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_DOWN).doubleValue())));
                } else {
                    esOrderItemBO.setDistributionParentAmount(0L);
                }
            });

            //分销佣金总数
            long totalDistribution = orderItems.stream().mapToLong(EsOrderItemBO::getDistributionAmount).sum();
            //发展佣金总数
            long totalDeveloping = orderItems.stream().mapToLong(EsOrderItemBO::getDistributionParentAmount).sum();

            logger.info("订单分销计算, esOrderBO:{}, totalDistribution:{}, totalDeveloping:{}", esOrderBO, totalDistribution, totalDeveloping);
            if (totalDistribution == 0 && totalDeveloping == 0) {
                return;
            }

            //更新订单商品佣金
//            orderFeignClient.updateOrderItemCommissionBatchById(orderItemVOList);

            DistributionNotifyOrderAndShopBO message = new DistributionNotifyOrderAndShopBO();
            message.setDistributionOrderItems(orderItems);

            SendResult sendResult = distributionNotifyOrderShopTemplate.syncSend(message);

//            SendStatus sendStatus = SendStatus.SEND_OK;

            //处理分销佣金
            processDistributionAmount(esOrderBO.getDistributionUserType(), esOrderBO.getDistributionUserId(), esOrderBO, sendResult, totalDistribution);

            //处理发展佣金
            processDevelopingAmount(1, esOrderBO.getDevelopingUserId(),esOrderBO, sendResult, totalDeveloping);

        });
    }


    private void processDistributionAmount(Integer identityType, Long userId, EsOrderBO esOrderBO, SendResult sendResult, Long totalCommission) {
        if (totalCommission == 0) {
            logger.info("分销佣金为空 esOrderBO:{}, sendStatus:{}, totalCommission:{}", esOrderBO, sendResult, totalCommission);
            return;
        }
        DistributionCommissionAccount account = distributionCommissionAccountService.getByUser(userId, identityType);
        if (null == account){
            logger.info("用户佣金账户不存在 identityType:{}, userId:{}", identityType, userId);
            return;
        }

        if (sendResult != null && sendResult.getMessageId() != null) {
            distributionCommissionAccountService.updateCommission(account, totalCommission, CommissionChangeTypeEnum.ADD_WAIT_SETTLE);
        } else {
            logger.info("订单待结算佣金计算消息发送失败 order:{}", esOrderBO);
        }
    }


    private void processDevelopingAmount(Integer identityType, Long userId, EsOrderBO esOrderBO, SendResult sendResult, Long totalCommission) {
        if (totalCommission == 0) {
            logger.info("发展佣金为空 esOrderBO:{}, sendStatus:{}, totalCommission:{}", esOrderBO, sendResult, totalCommission);
            return;
        }
        DistributionCommissionAccount account = distributionCommissionAccountService.getByUser(userId, identityType);
        if (null == account){
            logger.info("用户佣金账户不存在 identityType:{}, userId:{}", identityType, userId);
            return;
        }

        if (sendResult != null && sendResult.getMessageId() != null) {
            distributionCommissionAccountService.updateCommission(account, totalCommission, CommissionChangeTypeEnum.ADD_WAIT_SETTLE);
        } else {
            logger.info("订单待结算佣金计算消息发送失败 order:{}", esOrderBO);
        }
    }


    private void handleDistributionOrder(DistributionConfigApiVO distributionConfig, List<EsOrderBO> orders, List<EsOrderItemBO> orderItemBOS, List<DistributionSpu> distributionSpuList) {
        // 设置为分销的订单项
        List<EsOrderItemBO> distributionOrderItems = new ArrayList<>();
        List<Long> spuIds = distributionSpuList.stream().map(DistributionSpu::getSpuId).collect(Collectors.toList());
        List<ShopWalletBO> shopWalletBOList = new ArrayList<>();
        for (EsOrderBO order : orders) {
            // 所有的订单项
            List<EsOrderItemBO> orderItems = order.getOrderItems();
            boolean isFirstGetBindUser = true;
            DistributionUser oldDistributionUser = null;
            for (EsOrderItemBO orderItem : orderItems) {
                // 如果没有状态正常的分销商品则不能进行分销
                if (!spuIds.contains(orderItem.getSpuId())) {
                    continue;
                }
                // 用分销流水来保存幂等
                int row = distributionUserIncomeService.countByOrderIdAndOrderItemId(order.getOrderId(), orderItem.getOrderItemId(), null);
                if (row > 0) {
                    continue;
                }
                // 如果该订单项有分销信息，则将该订单项放入distributionOrderItems中
                if (Objects.nonNull(orderItem.getDistributionUserId())) {
                    doDistribution(oldDistributionUser, order, orderItem, distributionConfig);
                    distributionOrderItems.add(orderItem);
                    continue;
                }
                // 初始化用户的旧绑定分销员
                if (isFirstGetBindUser) {
                    isFirstGetBindUser = false;
                    // 查询该用户以前绑定的分享人
                    DistributionUserBind distributionUserBind = distributionUserBindService.getUserBindByUserIdAndState(order.getUserId(), BindStateEnum.VALID.value());
                    // 并且该用户以前也没有绑定分享人
                    if (distributionUserBind != null) {
                        // 查询以前的绑定的用户信息
                        oldDistributionUser = distributionUserService.getByDistributionUserId(distributionUserBind.getDistributionUserId());
                    }
                }
                if (Objects.isNull(oldDistributionUser)) {
                    continue;
                }
                if (!Objects.equals(oldDistributionUser.getState(), DistributionUserStateEnum.NORMAL.value())) {
                    continue;
                }
                // 将订单项设为分销项
                orderItem.setDistributionUserId(oldDistributionUser.getDistributionUserId());
                doDistribution(oldDistributionUser, order, orderItem, distributionConfig);
                distributionOrderItems.add(orderItem);
            }
            if (CollUtil.isEmpty(distributionOrderItems)) {
                continue;
            }
            // 分销订单需要在商家的未结算金额扣除用户分销金额
            // 我们需要发一个top来同时更新商家店铺钱包和订单项分销金额, 那么这里需要批量更新了
            ShopWalletBO shopWalletBO = new ShopWalletBO();
            shopWalletBO.setShopId(order.getShopId());
            shopWalletBO.setOrderId(order.getOrderId());
            // 订单分销金额
            ServerResponseEntity<Long> orderResponse = orderFeignClient.sumTotalDistributionAmountByOrderItem(distributionOrderItems);
            if (!orderResponse.isSuccess()) {
                logger.error(orderResponse.getMsg());
                throw new LuckException(orderResponse.getMsg());
            }
            long totalDistributionAmount = orderResponse.getData();
            // 商家待结算改变金额 = - 订单分销佣金 （商家待结算金额 = 商家待结算金额 - 订单分销佣金）
            shopWalletBO.setUnsettledAmount(-totalDistributionAmount);
            shopWalletBOList.add(shopWalletBO);
        }

        // 发送消息, 去修改订单分销金额和店铺钱包待结算金额
        DistributionNotifyOrderAndShopBO message = new DistributionNotifyOrderAndShopBO();
        message.setDistributionOrderItems(distributionOrderItems);
        message.setShopWalletBOList(shopWalletBOList);
        SendResult sendResult = distributionNotifyOrderShopTemplate.syncSend(message);
        if (sendResult == null || sendResult.getMessageId() == null) {
            // 消息发不出去就抛异常
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }


    /**
     * 进行分销操作
     *
     * @param oldShareUser       用户已绑定的分销员
     * @param order
     * @param orderItem          订单项
     * @param distributionConfig 分销设置
     */
    private void doDistribution(DistributionUser oldShareUser, EsOrderBO order, EsOrderItemBO orderItem, DistributionConfigApiVO distributionConfig) {
        DistributionUser shareUser;
        DistributionUser bindUser;

        if (oldShareUser != null && Objects.equals(orderItem.getDistributionUserId(), oldShareUser.getDistributionUserId())) {
            shareUser = oldShareUser;
            bindUser = oldShareUser;
        } else {
            shareUser = distributionUserService.getByDistributionUserId(orderItem.getDistributionUserId());
            // 判断是否可以进行分销操作
            if (checkAmountOver(orderItem)) {
                return;
            }
            // 绑定绑定销售员信息
            ServerResponseEntity<DistributionUser> serverResponse = distributionUserBindService.bindDistribution(shareUser, orderItem.getUserId(), 1);
            if (!serverResponse.isSuccess()) {
                logger.info("绑定销售员信息失败，跳过分销流程,订单号：{}, 订单项Id：{}", order.getOrderId(), orderItem.getOrderItemId());
            }
            // 绑定的用户
            bindUser = serverResponse.getData();
        }
        // 看看自己是不是分销员
        DistributionUserVO distributionUser = distributionUserService.getByUserId(orderItem.getUserId());
        // 下单的用户以前申请过分销员，但现在又不是了，所以就将分销员信息置空
        if (distributionUser != null && !Objects.equals(distributionUser.getState(), DistributionUserStateEnum.NORMAL.value())) {
            distributionUser = null;
        }
        //推广人若为永久封禁
        if (Objects.equals(shareUser.getState(), DistributionUserStateEnum.PER_BAN.value()) || Objects.equals(shareUser.getState(), DistributionUserStateEnum.BAN.value())) {
            logger.info("推广员已被暂时封禁或永久封禁");
            return;
        }
        // 添加收入流水记录并将收入添加到待结算金额到钱包中
        createIncomeByOrderItem(distributionConfig, order, shareUser, bindUser, orderItem);
    }

    /**
     * 创建订单项收入记录添加待结算金额到钱包中
     *
     * @param distributionConfig 分销配置
     * @param order              订单
     * @param shareUser          分销员
     * @param bindUser           绑定的分销员
     * @param orderItem          订单项
     */
    private void createIncomeByOrderItem(DistributionConfigApiVO distributionConfig, EsOrderBO order, DistributionUser shareUser, DistributionUser bindUser, EsOrderItemBO orderItem) {
        //判断是否会产生分销业绩
        if (Objects.isNull(bindUser) && Objects.isNull(shareUser)) {
            logger.error("没有分享人，没有关系人，不产生业绩");
            return;
        }
        // 获取该分销商品的设置数据
        DistributionSpuVO distributionSpuVo = distributionSpuService.getBySpuId(orderItem.getSpuId());
        DistributionSpuBO distributionSpuBO = mapperFacade.map(distributionSpuVo, DistributionSpuBO.class);

        // 这件商品原本是分销商品被加入了购物车，后来又不是了
        if (distributionSpuBO == null) {
            return;
        } else {
            distributionSpuBO.setShopId(orderItem.getShopId());
            // 计算佣金，用实付金额计算
            distributionSpuBO.setPrice(orderItem.getActualTotal());
        }
        //定义一个最终的业绩归属人变量
        DistributionUser finalDistributionUser;

        //判断是分享人优先还是关系链优先 0 允许绑定,关系优先,1,不绑定 分享人优先
        if (distributionConfig.getAttribution() == 0) {
            //关系链优先
            logger.info("关系链优先,关系人获得业绩");
            finalDistributionUser = bindUser;
        } else {
            //分享人优先
            logger.info("分享人优先,分享人获得业绩");
            finalDistributionUser = shareUser;
        }
        if (finalDistributionUser == null) {
            logger.info("没有找到业绩归属者");
            return;
        }
        // 判断是否可以进行分销操作
        if (checkAmountOver(orderItem)) {
            return;
        }
        DistributionUserIncome distributionUserIncome = new DistributionUserIncome();
        distributionUserIncome.setOrderId(order.getOrderId());
        distributionUserIncome.setOrderItemId(orderItem.getOrderItemId());
        // 如果系统设置为交易完毕后结算，则记录为结算状态，否则为未结算状态
        distributionUserIncome.setState(DistributionUserIncomeStateEnum.UN_COMMISSION.value());
        distributionUserIncome.setShopId(orderItem.getShopId());
        distributionUserIncome.setSpuId(orderItem.getSpuId());
        //计算佣金金额
        DistributionAwardDataVO distributionAwardDataVO = getAwardDataVO(distributionSpuBO, orderItem);
        //创建插入业绩记录对象
        if (!Objects.equals(distributionAwardDataVO.getAwardNumber(), Constant.ZERO_LONG)) {
            // 直推奖励
            insertAwardNumber(orderItem, finalDistributionUser, distributionAwardDataVO, distributionUserIncome);
        } else {
            logger.info("默认奖励为0，不结算");
        }
        //是否开启了上级奖励(0 关闭 1开启)
        if (!Objects.equals(distributionSpuBO.getParentAwardSet(), 1)) {
            logger.info("没有开启上级奖励");
            return;
        }
        //获取上级id
        if (Objects.isNull(finalDistributionUser.getParentId())) {
            logger.info("无法获取推广员的上级id");
            return;
        }
        if (Arith.isEquals(distributionAwardDataVO.getParentAwardNumber(), 0.0)) {
            logger.info("商品上级奖励为0，不结算");
            return;
        }
        logger.info("进入上级奖励流程");
        // 间推奖励
        insertParentAwardNumber(orderItem, finalDistributionUser, distributionAwardDataVO, distributionUserIncome);

    }

    private void insertParentAwardNumber(EsOrderItemBO orderItem, DistributionUser finalDistributionUser, DistributionAwardDataVO distributionAwardDataVO, DistributionUserIncome distributionUserIncome) {
        // 添加二级佣金给上级邀请人
        DistributionUserWallet parentDistributionUserWallet = distributionUserWalletService.getByDistributionUserId(finalDistributionUser.getParentId());
        // 如果上级不为空,则创建上级奖励业绩记录
        distributionUserIncome.setIncomeId(null);
        //类型为二级奖励
        distributionUserIncome.setIncomeType(DistributionUserIncomeTypeEnum.AWARD_TWO.value());
        distributionUserIncome.setDistributionUserId(finalDistributionUser.getParentId());
        distributionUserIncome.setIncomeAmount(distributionAwardDataVO.getParentAwardNumber());
        distributionUserIncome.setWalletId(parentDistributionUserWallet.getWalletId());
        distributionUserIncomeService.save(distributionUserIncome);

        //增加钱包流水记录
        distributionUserWalletBillService.save(new DistributionUserWalletBill(parentDistributionUserWallet, "间推奖励", "Second-generation rewards", distributionAwardDataVO.getParentAwardNumber(), 0L, 0L, 0L, 0));

        // 订单项添加上级奖励
        orderItem.setDistributionParentAmount(distributionAwardDataVO.getParentAwardNumber());

        // 给邀请人钱包增加待结算金额
        DistributionUserWallet updateWallet = new DistributionUserWallet();
        updateWallet.setWalletId(parentDistributionUserWallet.getWalletId());
        updateWallet.setUnsettledAmount(distributionAwardDataVO.getParentAwardNumber());
        // 更新邀请人钱包 给钱包增加待结算金额
        if (distributionUserWalletService.updateWalletAmount(updateWallet) < 1) {
            // 更新推广员钱包信息失败
            throw new LuckException("更新推广员钱包信息失败");
        }
    }

    /**
     * 直推奖励
     *
     * @param orderItem               订单项
     * @param finalDistributionUser   分销员
     * @param distributionAwardDataVO 奖励参数
     * @param distributionUserIncome  收入参数
     */
    private void insertAwardNumber(EsOrderItemBO orderItem, DistributionUser finalDistributionUser, DistributionAwardDataVO distributionAwardDataVO, DistributionUserIncome distributionUserIncome) {
        // 查询该推广员钱包信息
        DistributionUserWallet distributionUserWallet = distributionUserWalletService.getByDistributionUserId(finalDistributionUser.getDistributionUserId());
        distributionUserIncome.setDistributionUserId(finalDistributionUser.getDistributionUserId());
        distributionUserIncome.setWalletId(distributionUserWallet.getWalletId());
        distributionUserIncome.setIncomeAmount(distributionAwardDataVO.getAwardNumber());

        // 收入类型为一级奖励
        distributionUserIncome.setIncomeType(DistributionUserIncomeTypeEnum.AWARD_ONE.value());
        distributionUserIncomeService.save(distributionUserIncome);

        // 订单项添加分佣奖励
        orderItem.setDistributionAmount(distributionAwardDataVO.getAwardNumber());

        distributionUserWalletBillService.save(new DistributionUserWalletBill(distributionUserWallet, "客户支付获取待结算金额", "Customer payment to obtain the amount to be settled", distributionAwardDataVO.getAwardNumber(), 0L, 0L, 0L, 0));
        // 更新钱包
        DistributionUserWallet updateWallet = new DistributionUserWallet();
        updateWallet.setWalletId(distributionUserWallet.getWalletId());
        updateWallet.setUnsettledAmount(distributionAwardDataVO.getAwardNumber());
        // 给钱包增加待结算金额
        if (distributionUserWalletService.updateWalletAmount(updateWallet) < 1) {
            // 更新推广员钱包信息失败
            throw new LuckException("更新推广员钱包信息失败");
        }
    }

    /**
     * 检查分销金额是否超出订单项金额
     *
     * @param orderItem
     * @return
     */
    private boolean checkAmountOver(EsOrderItemBO orderItem) {
        // 获取该分销商品的设置数据
        DistributionSpuVO distributionSpuVo = distributionSpuService.getBySpuId(orderItem.getSpuId());
        DistributionSpuBO distributionSpuBO = mapperFacade.map(distributionSpuVo, DistributionSpuBO.class);
        // 这件商品原本是分销商品被加入了购物车，后来又不是了
        if (distributionSpuBO == null) {
            return true;
        } else {
            distributionSpuBO.setShopId(orderItem.getShopId());
            // 计算佣金，用实付金额计算
            distributionSpuBO.setPrice(orderItem.getActualTotal());
        }
        //计算佣金金额
        DistributionAwardDataVO distributionAwardDataVO = getAwardDataVO(distributionSpuBO, orderItem);
        logger.error(distributionAwardDataVO.toString() + "计算好的佣金金额");
        // 分销总金额
        long totalDistributionAmount = new BigDecimal(distributionAwardDataVO.getAwardNumber().toString())
                .add(new BigDecimal(distributionAwardDataVO.getParentAwardNumber().toString())).longValue();
        if (totalDistributionAmount >= orderItem.getActualTotal()) {
            logger.info("分销金额比实付金额要大，无法完成分销的操作");
            return true;
        }
        return false;
    }

    /**
     * 根据分销商品设置、分享人id，计算奖励数据
     *
     * @param distributionSpuBO 分销商品
     * @param orderItem         订单项
     * @return
     */
    private DistributionAwardDataVO getAwardDataVO(DistributionSpuBO distributionSpuBO, EsOrderItemBO orderItem) {
        DistributionAwardDataVO distributionAwardDataVO = new DistributionAwardDataVO();
        distributionAwardDataVO.setAwardNumber(Constant.ZERO_LONG);
        distributionAwardDataVO.setParentAwardNumber(Constant.ZERO_LONG);
        // distributionConfig
        // 奖励比例或数额
        distributionAwardDataVO.setAwardMode(distributionSpuBO.getAwardMode());
        if (Objects.isNull(distributionSpuBO.getAwardMode())) {
            return distributionAwardDataVO;
        }
        // 上级奖励设置(0 关闭 1开启)
        Integer parentAwardSet = distributionSpuBO.getParentAwardSet();
        Long price = distributionSpuBO.getPrice();
        BigDecimal awardNumbers = new BigDecimal(distributionSpuBO.getAwardNumbers().toString());
        BigDecimal parentAwardNumbers = new BigDecimal(distributionSpuBO.getParentAwardNumbers().toString());
        // 如果计算为按比例计算，则转化为具体金额
        if (Objects.equals(distributionAwardDataVO.getAwardMode(), 0)) {
            // 根据商品价格,计算得到的佣金 订单实际金额 * (奖励金额 / 100) 奖励比例
            // DistributionSpu.awardNumbers 查看解释，这个比例还需要除以100（awardNumbers = 1 则实际的奖励比例为 0.01%）
            awardNumbers = awardNumbers
                    .divide(new BigDecimal(PriceUtil.ONE_HUNDRED), 2, BigDecimal.ROUND_HALF_UP)
                    .divide(new BigDecimal(PriceUtil.ONE_HUNDRED), 4, BigDecimal.ROUND_HALF_UP);
            // 1000 * 900
            long awardAmount = new BigDecimal(price.toString()).multiply(awardNumbers).longValue();
            distributionAwardDataVO.setAwardNumber(awardAmount);
            // 查看是否开启邀请人奖励 订单实际金额 * (奖励金额 / 100) 奖励比例
            if (Objects.equals(1, parentAwardSet)) {
                // 邀请人奖励
                // DistributionSpu.parentAwardNumbers 查看解释，这个比例还需要除以100（parentAwardNumbers = 1 则实际的奖励比例为 0.01%）
                parentAwardNumbers = parentAwardNumbers
                        .divide(new BigDecimal(PriceUtil.ONE_HUNDRED), 0, BigDecimal.ROUND_HALF_UP)
                        .divide(new BigDecimal(PriceUtil.ONE_HUNDRED), 0, BigDecimal.ROUND_HALF_UP);

                long parentAwardNumber = new BigDecimal(price.toString()).multiply(parentAwardNumbers).longValue();
                distributionAwardDataVO.setParentAwardNumber(parentAwardNumber);
            }
        } else {
            // 按照固定值奖励
            distributionAwardDataVO.setAwardNumber(awardNumbers.multiply(new BigDecimal(orderItem.getCount())).longValue());
            if (Objects.equals(1, parentAwardSet)) {
                distributionAwardDataVO.setParentAwardNumber(parentAwardNumbers.multiply(new BigDecimal(orderItem.getCount())).longValue());
            }
        }
        return distributionAwardDataVO;
    }

}
