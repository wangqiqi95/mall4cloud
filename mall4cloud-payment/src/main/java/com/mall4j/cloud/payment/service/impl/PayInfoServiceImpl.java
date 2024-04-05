package com.mall4j.cloud.payment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.coupon.dto.PayCouponDTO;
import com.mall4j.cloud.api.coupon.feign.BuyCouponFeignClient;
import com.mall4j.cloud.api.coupon.vo.BuyCouponLog;
import com.mall4j.cloud.api.docking.skq_sqb.config.SQBParams;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.vo.OrderAmountVO;
import com.mall4j.cloud.api.user.bo.BuyVipNotifyBO;
import com.mall4j.cloud.api.user.bo.RechargeNotifyBO;
import com.mall4j.cloud.api.user.bo.UserScoreBO;
import com.mall4j.cloud.api.user.feign.UserBalanceLogClient;
import com.mall4j.cloud.api.user.feign.UserLevelLogClient;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.constant.PayType;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.bo.PayNotifyBO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.payment.bo.PayInfoBO;
import com.mall4j.cloud.payment.bo.PayInfoResultBO;
import com.mall4j.cloud.payment.constant.PayEntry;
import com.mall4j.cloud.payment.constant.PayStatus;
import com.mall4j.cloud.payment.dto.BuyVipPayInfoDTO;
import com.mall4j.cloud.payment.dto.CouponPayInfoDTO;
import com.mall4j.cloud.payment.dto.PayInfoDTO;
import com.mall4j.cloud.payment.dto.RechargePayInfoDTO;
import com.mall4j.cloud.payment.mapper.PayInfoMapper;
import com.mall4j.cloud.payment.model.PayInfo;
import com.mall4j.cloud.payment.service.PayInfoService;
import com.mall4j.cloud.payment.vo.AccountDetailVO;
import com.mall4j.cloud.payment.vo.OrderPayInfoVO;
import com.mall4j.cloud.payment.vo.PayInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 订单支付记录
 *
 * @author FrozenWatermelon
 * @date 2020-12-25 09:50:59
 */
@Slf4j
@Service
public class PayInfoServiceImpl implements PayInfoService {

    @Resource
    private PayInfoMapper payInfoMapper;

    @Autowired
    private SegmentFeignClient segmentFeignClient;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private OnsMQTemplate orderNotifyTemplate;

    @Autowired
    private OnsMQTemplate rechargeNotifyTemplate;

    @Autowired
    private UserBalanceLogClient userBalanceLogClient;

    @Autowired
    private UserLevelLogClient userLevelLogClient;

    @Autowired
    private OnsMQTemplate buyVipNotifyTemplate;

    @Autowired
    private OnsMQTemplate userScoreTemplate;

    @Resource
    private BuyCouponFeignClient buyCouponFeignClient;
    @Autowired
    private SQBParams sqbParams;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayInfoBO pay(Long userId, PayInfoDTO payParam) {


        List<Long> orderIds = payParam.getOrderIds();
        ServerResponseEntity<OrderAmountVO> ordersAmountAndIfNoCancelResponse = orderFeignClient.getOrdersAmountAndIfNoCancel(orderIds);
        // 如果订单已经关闭了，此时不能够支付了
        if (!ordersAmountAndIfNoCancelResponse.isSuccess()) {
            throw new LuckException(ordersAmountAndIfNoCancelResponse.getMsg());
        }
        OrderAmountVO orderAmount = ordersAmountAndIfNoCancelResponse.getData();

        //如果金额小于0.01 支付积分大于0则为纯积分支付
        if(orderAmount.getPayAmount() < 1 && orderAmount.getPayScore() > 0 && !Objects.equals(PayType.SCOREPAY.value(), payParam.getPayType())){
            throw new LuckException("订单金额有误，无法进行支付");
        }
        // 金额小于0.01且支付方式不为积分支付
        else if (orderAmount.getPayAmount() < 1 && !Objects.equals(PayType.SCOREPAY.value(), payParam.getPayType())) {
            throw new LuckException("订单金额有误，无法进行支付");
        }


        ServerResponseEntity<EsOrderBO> orderBOServerResponseEntity = orderFeignClient.getEsOrder(orderIds.get(0));
        if(!orderBOServerResponseEntity.isSuccess()){
            log.error("订单不存在，请检查订单id：{}",orderIds.get(0));
            Assert.faild("订单不存在，请检查订单id参数");
        }
        String orderNumber = orderBOServerResponseEntity.getData().getOrderNumber();
        Date createTime = orderBOServerResponseEntity.getData().getCreateTime();
    
        /**
         * 判断payinfo是否存在，如果支付单号存在则直接返回。
         * 确保一笔订单只有一个支付单
         */
        //只有一笔订单的支付方式
        String strOrderIds = StrUtil.toString(orderIds.get(0));
        PayInfo dbPayInfo = payInfoMapper.getPayInfoByOrderIds(strOrderIds);
        
        if(Objects.nonNull(dbPayInfo) ){
            //订单未支付,直接返回支付信息
            if(Objects.equals(PayStatus.UNPAY.value(),dbPayInfo.getPayStatus())){
                PayInfoBO payInfoBo = new PayInfoBO();
                payInfoBo.setOrderNumber(orderNumber);
                payInfoBo.setOrderIds(strOrderIds);
                payInfoBo.setBody("商城支付订单");
                payInfoBo.setPayAmount(orderAmount.getPayAmount());
                payInfoBo.setPayId(dbPayInfo.getPayId());
                payInfoBo.setOrderToken(dbPayInfo.getOrderToken());
                payInfoBo.setCreateTime(createTime);
                return payInfoBo;
            }
            
            //收钱吧订单已取消,提示订单已取消
            if(Objects.equals(PayStatus.CANCELED_OR_CANCELED.value(),dbPayInfo.getPayStatus())){
                log.error("收钱吧订单已取消，请检查订单id：{}",strOrderIds);
                Assert.faild("订单已取消，请重新下单");
            }
        }

        // 支付单号
        ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_PAY);
        if (!segmentIdResponse.isSuccess()) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        Long payId = segmentIdResponse.getData();
        PayInfo payInfo = new PayInfo();
        payInfo.setPayId(payId);
        payInfo.setUserId(userId);
        payInfo.setPayAmount(orderAmount.getPayAmount());
        payInfo.setPayScore(orderAmount.getPayScore());
        payInfo.setPayStatus(PayStatus.UNPAY.value());
        payInfo.setSysType(AuthUserContext.get().getSysType());
        payInfo.setPayType(payParam.getPayType());
        payInfo.setVersion(0);
        // 保存多个支付订单号
        payInfo.setOrderIds(StrUtil.join(StrUtil.COMMA,orderIds));
        payInfo.setOrderNumber(orderNumber);

        payInfo.setPayEntry(PayEntry.ORDER.value());
        // 保存预支付信息
        payInfoMapper.save(payInfo);

        PayInfoBO payInfoBo = new PayInfoBO();
        payInfoBo.setOrderNumber(orderNumber);
        payInfoBo.setOrderIds(payInfo.getOrderIds());
        payInfoBo.setBody("商城支付订单");
        payInfoBo.setPayAmount(orderAmount.getPayAmount());
        payInfoBo.setPayId(payId);
        payInfoBo.setCreateTime(createTime);
        return payInfoBo;
    }

    @Override
    public PayInfo getByPayId(Long payId) {
        return payInfoMapper.getByPayId(payId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paySuccess(PayInfoResultBO payInfoResult, PayInfo temp, List<Long> orderIds) {
        // 标记为支付成功状态
        PayInfo payInfo = new PayInfo();
        payInfo.setPayId(payInfoResult.getPayId());
        payInfo.setBizPayNo(payInfoResult.getBizPayNo());
        payInfo.setCallbackContent(payInfoResult.getCallbackContent());
        payInfo.setCallbackTime(new Date());
        payInfo.setPayStatus(PayStatus.PAYED.value());
        payInfoMapper.update(payInfo);

        // 发送消息，订单支付成功
        SendResult sendResult = orderNotifyTemplate.syncSend(new PayNotifyBO(orderIds, PayType.instance(temp.getPayType()).value(), payInfo.getPayId()));

        if (sendResult == null || sendResult.getMessageId() == null) {
            // 消息发不出去就抛异常，因为订单回调会有多次，几乎不可能每次都无法发送出去，发的出去无所谓因为接口是幂等的
            throw new LuckException(ResponseEnum.EXCEPTION);
        }

        UserScoreBO userScoreBo = new UserScoreBO();
        userScoreBo.setUserId(temp.getUserId());
        userScoreBo.setPayType(payInfo.getPayType());
        userScoreBo.setOrderIds(orderIds);
        sendResult = userScoreTemplate.syncSend(userScoreBo);
        if (sendResult == null || sendResult.getMessageId() == null) {
            // 消息发不出去就抛异常，发的出去无所谓
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }

    @Override
    public Integer getPayStatusByOrderIds(String orderIds) {
        return payInfoMapper.getPayStatusByOrderIds(orderIds);
    }

    @Override
    public Integer isPay(String orderIds, Long userId, Integer payEntry) {
        return payInfoMapper.isPay(orderIds, userId,payEntry);
    }

    @Override
    public void markerRefund(Long payId) {
        PayInfo payInfo = new PayInfo();
        payInfo.setPayId(payId);
        payInfo.setPayStatus(PayStatus.REFUND.value());
        payInfoMapper.update(payInfo);
    }

    @Override
    public void update(PayInfo payInfo) {
        payInfoMapper.update(payInfo);
    }

    @Override
    public void updateByBizOrderIds(PayInfo payInfo) {
        payInfoMapper.updateByOrderIds(payInfo);
    }

    @Override
    public List<PayInfo> listByOrderIds(List<Long> orderIds) {
        return payInfoMapper.listByOrderIds(orderIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PayInfoBO recharge(Long userId, RechargePayInfoDTO payParam) {

        // 支付单号
        ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_PAY);
        if (!segmentIdResponse.isSuccess()) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        Long payId = segmentIdResponse.getData();
        Long rechargeLogId = payParam.getBalanceLogId();

        // 充值信息
        ServerResponseEntity<Long> payAmountResponse = userBalanceLogClient.getPayAmount(rechargeLogId);
        if (!payAmountResponse.isSuccess()) {
            throw new LuckException(payAmountResponse.getMsg());
        }
        Long payAmount = payAmountResponse.getData();
        if (payAmount == null || payAmount < 1) {
            throw new LuckException("充值金额有误");
        }

        PayInfo payInfo = new PayInfo();
        payInfo.setPayId(payId);
        payInfo.setUserId(userId);
        payInfo.setPayAmount(payAmount);
        payInfo.setPayScore(0L);
        payInfo.setPayStatus(PayStatus.UNPAY.value());
        payInfo.setSysType(AuthUserContext.get().getSysType());
        payInfo.setPayType(payParam.getPayType());
        payInfo.setPayEntry(PayEntry.RECHARGE.value());
        payInfo.setVersion(0);
        // 充值id
        payInfo.setOrderIds(String.valueOf(rechargeLogId));


        // 保存预支付信息
        payInfoMapper.save(payInfo);
        PayInfoBO payInfoDto = new PayInfoBO();
        payInfoDto.setBody("商城充值订单");
        payInfoDto.setPayAmount(payAmount);
        payInfoDto.setPayId(payId);
        return payInfoDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rechargeSuccess(PayInfoResultBO payInfoResult, PayType payType, Long rechargeLogId) {
        // 标记为支付成功状态
        PayInfo payInfo = new PayInfo();
        payInfo.setPayId(payInfoResult.getPayId());
        payInfo.setBizPayNo(payInfoResult.getBizPayNo());
        payInfo.setCallbackContent(payInfoResult.getCallbackContent());
        payInfo.setCallbackTime(new Date());
        payInfo.setPayStatus(PayStatus.PAYED.value());
        payInfoMapper.update(payInfo);

        // 发送消息，订单支付成功
        SendResult sendResult = rechargeNotifyTemplate.syncSend(new RechargeNotifyBO(rechargeLogId, payType.value(), payInfo.getPayId()));
        if (sendResult == null || sendResult.getMessageId() == null) {
            // 消息发不出去就抛异常，因为订单回调会有多次，几乎不可能每次都无法发送出去，发的出去无所谓因为接口是幂等的
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }

    @Override
    public PayInfoBO vipBuy(Long userId, BuyVipPayInfoDTO payParam) {
        // 支付单号
        ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_PAY);
        if (!segmentIdResponse.isSuccess()) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        Long payId = segmentIdResponse.getData();
        Long userLevelLogId = payParam.getUserLevelLogId();

        // 充值信息
        ServerResponseEntity<Long> payAmountResponse = userLevelLogClient.getPayAmount(userLevelLogId);
        if (!payAmountResponse.isSuccess()) {
            throw new LuckException(payAmountResponse.getMsg());
        }
        Long payAmount = payAmountResponse.getData();
        PayInfo payInfo = new PayInfo();
        payInfo.setPayId(payId);
        payInfo.setUserId(userId);
        payInfo.setPayAmount(payAmount);
        payInfo.setPayScore(0L);
        payInfo.setPayStatus(PayStatus.UNPAY.value());
        payInfo.setSysType(AuthUserContext.get().getSysType());
        payInfo.setPayType(payParam.getPayType());
        payInfo.setPayEntry(PayEntry.VIP.value());
        payInfo.setVersion(0);
        // 充值id
        payInfo.setOrderIds(String.valueOf(userLevelLogId));


        // 保存预支付信息
        payInfoMapper.save(payInfo);
        PayInfoBO payInfoDto = new PayInfoBO();
        payInfoDto.setBody("商城VIP订购订单");
        payInfoDto.setPayAmount(payAmount);
        payInfoDto.setPayId(payId);
        return payInfoDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void buyVipSuccess(PayInfoResultBO payInfoResult, PayType payType, Long userLevelLogId) {
        // 标记为支付成功状态
        PayInfo payInfo = new PayInfo();
        payInfo.setPayId(payInfoResult.getPayId());
        payInfo.setBizPayNo(payInfoResult.getBizPayNo());
        payInfo.setCallbackContent(payInfoResult.getCallbackContent());
        payInfo.setCallbackTime(new Date());
        payInfo.setPayStatus(PayStatus.PAYED.value());
        payInfoMapper.update(payInfo);

        // 发送消息，订单支付成功
        SendResult sendResult = buyVipNotifyTemplate.syncSend(new BuyVipNotifyBO(userLevelLogId, payType.value(), payInfo.getPayId()));
        if (sendResult == null || sendResult.getMessageId() == null) {
            // 消息发不出去就抛异常，因为订单回调会有多次，几乎不可能每次都无法发送出去，发的出去无所谓因为接口是幂等的
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }

    @Override
    public AccountDetailVO getIncomeAccountDetail(Date startTime, Date endTime) {
        AccountDetailVO accountDetailVO = payInfoMapper.getIncomeAccountDetail(startTime, endTime);
        if (Objects.isNull(accountDetailVO)){
            AccountDetailVO accountDetail = new AccountDetailVO();
            accountDetail.setWechatAmount(0L);
            accountDetail.setAlipayAmount(0L);
            accountDetail.setBalanceAmount(0L);
            accountDetail.setWechatPercent(0.00);
            accountDetail.setAlipayPercent(0.00);
            accountDetail.setBalancePercent(0.00);
            accountDetail.setTotal(0L);
            return accountDetail;
        } else {
            Long wechatAmount = accountDetailVO.getWechatAmount();
            Long alipayAmount = accountDetailVO.getAlipayAmount();
            Long balanceAmount = accountDetailVO.getBalanceAmount();
            long total = wechatAmount + alipayAmount + balanceAmount;
            accountDetailVO.setTotal(total);
            accountDetailVO.setWechatPercent(Arith.div(wechatAmount.doubleValue(), (double) total,4));
            accountDetailVO.setAlipayPercent(Arith.div(alipayAmount.doubleValue(), (double) total,4));
            accountDetailVO.setBalancePercent(Arith.div(balanceAmount.doubleValue(), (double) total,4));
            return accountDetailVO;
        }
    }

    @Override
    public PageVO<PayInfoVO> getPayInfoPage(PageDTO pageDTO, Date startTime, Date endTime) {
        PageVO<PayInfoVO> pageVO = PageUtil.doPage(pageDTO, () -> payInfoMapper.getPayInfoVO(startTime, endTime));
        long index = (long)pageDTO.getPageSize() * (pageDTO.getPageNum() - 1);
        for (PayInfoVO payInfoVO : pageVO.getList()) {
            payInfoVO.setIndex(index);
            index++;
        }
        return pageVO;
    }

    @Override
    public PayInfoBO coupon(Long userId, CouponPayInfoDTO payParam) {
        // 支付单号
        ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_PAY);
        if (!segmentIdResponse.isSuccess()) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        Long payId = segmentIdResponse.getData();
        Long couponOrderId = payParam.getCouponOrderId();

        // 充值信息
        //查询购买券的充值信息
        ServerResponseEntity<BuyCouponLog> buyCouponDetail = buyCouponFeignClient.getBuyCouponDetail(couponOrderId);
        if (!buyCouponDetail.isSuccess()) {
            throw new LuckException(buyCouponDetail.getMsg());
        }
        Long payAmount = buyCouponDetail.getData().getPrice();
        PayInfo payInfo = new PayInfo();
        payInfo.setPayId(payId);
        payInfo.setUserId(userId);
        payInfo.setPayAmount(payAmount);
        payInfo.setPayScore(0L);
        payInfo.setPayStatus(PayStatus.UNPAY.value());
        payInfo.setSysType(AuthUserContext.get().getSysType());
        payInfo.setPayType(payParam.getPayType());
        payInfo.setPayEntry(PayEntry.VIP.value());
        payInfo.setVersion(0);
        // 充值id
        payInfo.setOrderIds(String.valueOf(couponOrderId));


        // 保存预支付信息
        payInfoMapper.save(payInfo);
        PayInfoBO payInfoDto = new PayInfoBO();
        payInfoDto.setBody("商城优惠券订单");
        payInfoDto.setPayAmount(payAmount);
        payInfoDto.setPayId(payId);
        return payInfoDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void couponSuccess(PayInfoResultBO payInfoResultBO, PayType instance, Long couponOrderId) {
        log.info("====================  购买优惠券支付成功回调:{} ===================", JSONObject.toJSONString(payInfoResultBO));
        PayCouponDTO payCouponDTO = new PayCouponDTO();
        payCouponDTO.setOrderNo(couponOrderId);
        payCouponDTO.setWechatPayNo(payInfoResultBO.getPayId().toString());
        buyCouponFeignClient.payCoupon(payCouponDTO);
    }
    
    @Override
    public ServerResponseEntity<OrderPayInfoVO> queryOrderPayType(Long orderId) {
    
        OrderPayInfoVO orderPayInfoVO = new OrderPayInfoVO();
        //获取nacos配置的支付方式
        orderPayInfoVO.setOrderPayType(sqbParams.getOrderPayType());
    
        //获取支付记录
        PayInfo payInfo = payInfoMapper.getByOrderId(orderId);
        if(Objects.nonNull(payInfo)){
            //替换为生成支付记录的支付方式
            orderPayInfoVO.setOrderPayType(payInfo.getPayType());
        }
        return ServerResponseEntity.success(orderPayInfoVO);
    }
    
    @Override
    public List<PayInfo> listByPayStatusAndPayType(Integer payStatus, Integer payType) {
        return payInfoMapper.listByPayStatusAndPayType(payStatus,payType);
    }
    
    @Override
    public PayInfoVO queryPayInfo(Long orderId) {
        PayInfo payInfo = payInfoMapper.getByOrderId(orderId);
        return BeanUtil.copyProperties(payInfo, PayInfoVO.class);
    }
}
