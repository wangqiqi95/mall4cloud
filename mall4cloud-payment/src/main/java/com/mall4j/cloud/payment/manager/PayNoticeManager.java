package com.mall4j.cloud.payment.manager;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.mall4j.cloud.api.coupon.feign.BuyCouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.BuyCouponLog;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.order.bo.OrderStatusBO;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.user.feign.UserBalanceLogClient;
import com.mall4j.cloud.api.user.feign.UserLevelLogClient;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.constant.PayType;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.bo.PayRefundBO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.payment.bo.PayInfoResultBO;
import com.mall4j.cloud.payment.mapper.PayInfoMapper;
import com.mall4j.cloud.payment.model.PayInfo;
import com.mall4j.cloud.payment.service.PayInfoService;
import com.mall4j.cloud.payment.service.RefundInfoService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author FrozenWatermelon
 * @date 2021/5/11
 */
@Slf4j
@Component
public class PayNoticeManager {
    
    private static final String REDISSON_LOCK_PREFIX = "redisson_lock:";

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private SegmentFeignClient segmentFeignClient;

    @Autowired
    private PayInfoService payInfoService;


    @Autowired
    private RefundInfoService refundInfoService;

    @Autowired
    private UserBalanceLogClient userBalanceLogClient;

    @Autowired
    private UserLevelLogClient userLevelLogClient;
    @Resource
    private BuyCouponFeignClient buyCouponFeignClient;
    @Autowired
    private RedissonClient redissonClient;
    @Resource
    private PayInfoMapper payInfoMapper;


    public ResponseEntity<String> noticeOrder(PayInfoResultBO payInfoResultBO, PayInfo payInfo) {

        // 获取订单之前是不是有进行过支付，如果有进行过支付，那么本次支付将直接退款
        String[] orderIdStrArr = payInfo.getOrderIds().split(StrUtil.COMMA);
        List<Long> orderIdList = new ArrayList<>();
        for (String s : orderIdStrArr) {
            orderIdList.add(Long.valueOf(s));
        }
    
        //收钱吧聚合支付和微信支付分别执行自己的逻辑
        if(Objects.equals(payInfo.getPayType(), PayType.SQB_LITE_POS.value())){
    
            // 执行真正的支付需要分布式锁,预防回调和定时器同时进行数据操作
            RLock redissionKey = redissonClient.getLock(REDISSON_LOCK_PREFIX + payInfo.getOrderIds());
            try {
                //尝试加锁, 最多等待5秒, 5秒后自动解锁
                int lockWait = 5;
                if ( redissionKey.tryLock(lockWait, lockWait, TimeUnit.SECONDS) ) {
                    
                    //获取到锁之后，再判断一次订单是否支付完成
                    PayInfo statusPayInfo = payInfoMapper.getByOrderId(Long.valueOf(payInfo.getOrderIds()));
                    if(Objects.nonNull(statusPayInfo) && statusPayInfo.getPayStatus() == 1){
                        return ResponseEntity.ok(payInfoResultBO.getSuccessString());
                    }
                    
                    //真正的支付成功
                    payInfoService.paySuccess(payInfoResultBO, payInfo, orderIdList);
                }
    
            }catch(Exception e){
                log.info("尝试获取锁失败 {} ,{}", e, e.getMessage());
                throw new LuckException(ResponseEnum.EXCEPTION);
            }finally{
                //解锁
                if (redissionKey.isLocked()) {
                    if (redissionKey.isHeldByCurrentThread()) {
                        redissionKey.unlock();
                    }
                }
            }
            
        }else {
            
            ServerResponseEntity<List<OrderStatusBO>> ordersStatusResponse = orderFeignClient.getOrdersStatus(orderIdList);
        
            if (!ordersStatusResponse.isSuccess()) {
                return ResponseEntity.ok(ResponseEnum.EXCEPTION.value());
            }
        
            List<OrderStatusBO> orderStatusList = ordersStatusResponse.getData();
        
            for (OrderStatusBO orderStatusBO : orderStatusList) {
                // 有其中一个订单已经支付过了
                if (Objects.equals(orderStatusBO.getStatus(), OrderStatus.PAYED.value()) || Objects.equals(orderStatusBO.getStatus(), OrderStatus.WAIT_GROUP.value()) ) {
                    ServerResponseEntity<Long> segmentId = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_REFUND);
                    PayRefundBO payRefundBO = new PayRefundBO();
                    payRefundBO.setRefundId(segmentId.getData());
                    payRefundBO.setRefundAmount(payInfo.getPayAmount());
                    payRefundBO.setOrderId(orderStatusBO.getOrderId());
                    payRefundBO.setPayId(payInfo.getPayId());
                    payRefundBO.setOnlyRefund(1);
                    refundInfoService.doRefund(payRefundBO);
                    // 标记为退款
                    payInfoService.markerRefund(payInfo.getPayId());
                    return ResponseEntity.ok(payInfoResultBO.getSuccessString());
                }
            }
            
            // 真正的支付成功
            payInfoService.paySuccess(payInfoResultBO, payInfo, orderIdList);
        }
        
        orderFeignClient.zhlsApiUtilAddOrder(orderIdList);
        return ResponseEntity.ok(payInfoResultBO.getSuccessString());
    }

    public ResponseEntity<String> noticeRecharge(PayInfoResultBO payInfoResultBO, PayInfo payInfo) {


        // 获取订单之前是不是有进行过支付，如果有进行过支付，那么本次支付将直接退款
        Long balanceLogId = Long.valueOf(payInfo.getOrderIds());

        ServerResponseEntity<Integer> ordersStatusResponse = userBalanceLogClient.getIsPay(balanceLogId);

        if (!ordersStatusResponse.isSuccess()) {
            return ResponseEntity.ok(ResponseEnum.EXCEPTION.value());
        }

        Integer isPay = ordersStatusResponse.getData();

        // 有其中一个订单已经支付过了
        if (Objects.equals(isPay, 1)) {
            ServerResponseEntity<Long> segmentId = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_REFUND);
            PayRefundBO payRefundBO = new PayRefundBO();
            payRefundBO.setRefundId(segmentId.getData());
            payRefundBO.setRefundAmount(payInfo.getPayAmount());
            payRefundBO.setOrderId(balanceLogId);
            payRefundBO.setPayId(payInfoResultBO.getPayId());
            payRefundBO.setOnlyRefund(1);
            refundInfoService.doRefund(payRefundBO);
            // 标记为退款
            payInfoService.markerRefund(payInfo.getPayId());
            return ResponseEntity.ok(payInfoResultBO.getSuccessString());
        }

        // 支付成功
        payInfoService.rechargeSuccess(payInfoResultBO, PayType.instance(payInfo.getPayType()),balanceLogId);
        return ResponseEntity.ok(payInfoResultBO.getSuccessString());
    }

    public ResponseEntity<String> noticeBuyVip(PayInfoResultBO payInfoResultBO, PayInfo payInfo) {
        // 获取订单之前是不是有进行过支付，如果有进行过支付，那么本次支付将直接退款
        Long userLevelLogId = Long.valueOf(payInfo.getOrderIds());

        ServerResponseEntity<Integer> ordersStatusResponse = userLevelLogClient.getIsPay(userLevelLogId);

        if (!ordersStatusResponse.isSuccess()) {
            return ResponseEntity.ok(ResponseEnum.EXCEPTION.value());
        }

        Integer isPay = ordersStatusResponse.getData();

        // 有其中一个订单已经支付过了
        if (Objects.equals(isPay, 1)) {
            ServerResponseEntity<Long> segmentId = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_REFUND);
            PayRefundBO payRefundBO = new PayRefundBO();
            payRefundBO.setRefundId(segmentId.getData());
            payRefundBO.setRefundAmount(payInfo.getPayAmount());
            payRefundBO.setOrderId(userLevelLogId);
            payRefundBO.setPayId(payInfoResultBO.getPayId());
            payRefundBO.setOnlyRefund(1);
            refundInfoService.doRefund(payRefundBO);
            // 标记为退款
            payInfoService.markerRefund(payInfo.getPayId());
            return ResponseEntity.ok(payInfoResultBO.getSuccessString());
        }

        // 支付成功
        payInfoService.buyVipSuccess(payInfoResultBO, PayType.instance(payInfo.getPayType()),userLevelLogId);
        return ResponseEntity.ok(payInfoResultBO.getSuccessString());
    }

    public ResponseEntity<String> noticeCouponPack(PayInfoResultBO payInfoResultBO, PayInfo payInfo) {
        // 获取订单之前是不是有进行过支付，如果有进行过支付，那么本次支付将直接退款
        Long couponOrderId = Long.valueOf(payInfo.getOrderIds());

        ServerResponseEntity<BuyCouponLog> buyCouponDetail = buyCouponFeignClient.getBuyCouponDetail(couponOrderId);
        if (!buyCouponDetail.isSuccess()) {
            throw new LuckException(buyCouponDetail.getMsg());
        }

        Integer isPay = buyCouponDetail.getData().getPayStatus();

        // 有其中一个订单已经支付过了
        if (Objects.equals(isPay, 0)) {
            log.info("订单信息为：{}", JSONObject.toJSONString(buyCouponDetail));
            ServerResponseEntity<Long> segmentId = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_REFUND);
            PayRefundBO payRefundBO = new PayRefundBO();
            payRefundBO.setRefundId(segmentId.getData());
            payRefundBO.setRefundAmount(payInfo.getPayAmount());
            payRefundBO.setOrderId(couponOrderId);
            payRefundBO.setPayId(payInfoResultBO.getPayId());
            payRefundBO.setOnlyRefund(1);
            refundInfoService.doRefund(payRefundBO);
            // 标记为退款
            payInfoService.markerRefund(payInfo.getPayId());
            return ResponseEntity.ok(payInfoResultBO.getSuccessString());
        }

        // 支付成功
        payInfoService.couponSuccess(payInfoResultBO, PayType.instance(payInfo.getPayType()),couponOrderId);
        return ResponseEntity.ok(payInfoResultBO.getSuccessString());
    }
}
