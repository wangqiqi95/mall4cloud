package com.mall4j.cloud.coupon.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.openservices.ons.api.SendResult;
import com.google.common.collect.Lists;
import com.mall4j.cloud.api.coupon.constant.UserCouponStatus;
import com.mall4j.cloud.api.coupon.dto.LockCouponDTO;
import com.mall4j.cloud.api.order.bo.OrderStatusBO;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.rocketmq.config.RocketMqConstant;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.coupon.mapper.CouponLockMapper;
import com.mall4j.cloud.coupon.mapper.CouponUserMapper;
import com.mall4j.cloud.coupon.model.CouponLock;
import com.mall4j.cloud.coupon.service.CouponLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 优惠券使用记录
 *
 * @author FrozenWatermelon
 * @date 2020-12-28 10:04:50
 */
@Service
public class CouponLockServiceImpl implements CouponLockService {

    @Autowired
    private CouponLockMapper couponLockMapper;
    @Autowired
    private CouponUserMapper couponUserMapper;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private OnsMQTemplate couponMqTemplate;

    @Override
    public PageVO<CouponLock> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> couponLockMapper.list());
    }

    @Override
    public CouponLock getById(Long id) {
        return couponLockMapper.getById(id);
    }

    @Override
    public void save(CouponLock couponLock) {
        couponLockMapper.save(couponLock);
    }

    @Override
    public void update(CouponLock couponLock) {
        couponLockMapper.update(couponLock);
    }

    @Override
    public void deleteById(Long couponUseRecordId) {
        couponLockMapper.deleteById(couponUseRecordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Void> lockCoupon(List<LockCouponDTO> lockCouponParams) {
        List<CouponLock> couponLocks = Lists.newArrayList();
        Long userId = AuthUserContext.get().getUserId();
        List<Long> userCouponIds = new ArrayList<>();
        Set<Long> orderIds = new HashSet<>();
        for (LockCouponDTO lockCouponParam : lockCouponParams) {
            CouponLock couponLock = new CouponLock();
            couponLock.setAmount(lockCouponParam.getReduceAmount());
            // 获取用户优惠券id
            couponLock.setCouponUserId(lockCouponParam.getCouponUserId());
            userCouponIds.add(lockCouponParam.getCouponUserId());
            couponLock.setUserId(userId);
            couponLock.setOrderIds(lockCouponParam.getOrderIds());
            if (couponLock.getOrderIds().contains(StrUtil.COMMA)) {
                for (String orderId : couponLock.getOrderIds().split(StrUtil.COMMA)) {
                    orderIds.add(Long.valueOf(orderId));
                }
            } else {
                orderIds.add(Long.valueOf(couponLock.getOrderIds()));
            }
            //优惠券记录为冻结状态
            couponLock.setStatus(0);

            couponLocks.add(couponLock);
        }
        // 批量保存使用记录
        couponLockMapper.saveBatch(couponLocks);
        // 批量将用户的优惠券变成已使用状态
        couponUserMapper.batchUpdateUserCouponStatus(UserCouponStatus.USED.getValue(), userCouponIds);

        // 发送消息一个小时后解锁优惠券(包括哪些订单)
        SendResult sendResult = couponMqTemplate.syncSend(orderIds,RocketMqConstant.CANCEL_ORDER_DELAY_LEVEL + 1);
        if (sendResult == null || sendResult.getMessageId() == null) {
            // 消息发不出去就抛异常，发的出去无所谓
            throw new LuckException(ResponseEnum.EXCEPTION);
        }

        return ServerResponseEntity.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlockCoupon(List<Long> orderIds) {
        ServerResponseEntity<List<OrderStatusBO>> ordersStatusResponse = orderFeignClient.getOrdersStatus(orderIds);
        if (!ordersStatusResponse.isSuccess()) {
            throw new LuckException(ordersStatusResponse.getMsg());
        }
        List<OrderStatusBO> orderStatusList = ordersStatusResponse.getData();
        List<Long> needCouponUserIds = new ArrayList<>();
        for (OrderStatusBO orderStatusBO : orderStatusList) {
            // 该订单没有下单成功，或订单已取消，赶紧解锁库存
            if (orderStatusBO.getStatus() == null || Objects.equals(orderStatusBO.getStatus(), OrderStatus.CLOSE.value())) {
                List<Long> couponUserIds = couponLockMapper.listCouponUserIdsByOrderId(orderStatusBO.getOrderId());
                needCouponUserIds.addAll(couponUserIds);
            }
        }

        if (CollectionUtil.isEmpty(needCouponUserIds)) {
            return;
        }

        // 批量将用户的优惠券变成未使用状态
        couponUserMapper.batchUpdateUserCouponStatus(UserCouponStatus.EFFECTIVE.getValue(), needCouponUserIds);

        // 将锁定状态标记为已解锁
        int updateStatus = couponLockMapper.unLockByIds(needCouponUserIds);
        if (updateStatus == 0) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markerCouponUse(List<Long> orderIds) {
        couponLockMapper.markerCouponUse(orderIds);
    }

    @Override
    public void reductionCoupon(Long orderId) {
        Long userCouponId = couponLockMapper.getUserCouponIdByOrderId(String.valueOf(orderId));
        if (userCouponId != null) {
            couponUserMapper.reductionCoupon(userCouponId);
        }
    }

}
