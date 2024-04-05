package com.mall4j.cloud.payment.feign;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.SendResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.mall4j.cloud.api.leaf.feign.SegmentFeignClient;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.bo.OrderStatusBO;
import com.mall4j.cloud.api.order.constant.OrderStatus;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.payment.bo.LiveStoreOrderCancelInfoVO;
import com.mall4j.cloud.api.payment.bo.LiveStorePayInfoVO;
import com.mall4j.cloud.api.payment.bo.SQBOrderPaySuccessBO;
import com.mall4j.cloud.api.payment.feign.PayInfoFeignClient;
import com.mall4j.cloud.api.payment.vo.GetPayInfoByOrderIdsAndPayTypeVO;
import com.mall4j.cloud.api.payment.vo.PayInfoFeignVO;
import com.mall4j.cloud.api.user.bo.UserScoreBO;
import com.mall4j.cloud.common.constant.DistributedIdKey;
import com.mall4j.cloud.common.constant.PayType;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.bo.PayNotifyBO;
import com.mall4j.cloud.common.order.constant.OrderType;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.payment.config.WxConfig;
import com.mall4j.cloud.payment.bo.PayInfoResultBO;
import com.mall4j.cloud.payment.constant.PayEntry;
import com.mall4j.cloud.payment.constant.PayStatus;
import com.mall4j.cloud.payment.manager.PayNoticeManager;
import com.mall4j.cloud.payment.mapper.PayInfoMapper;
import com.mall4j.cloud.payment.model.PayInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
public class PayInfoFeignClientController  implements PayInfoFeignClient {

    @Autowired
    PayInfoMapper payInfoMapper;
    @Autowired
    OrderFeignClient orderFeignClient;
    @Autowired
    SegmentFeignClient segmentFeignClient;
    @Autowired
    private OnsMQTemplate orderNotifyTemplate;
    @Autowired
    private PayNoticeManager payNoticeManager;
    @Autowired
    private WxConfig wxConfig;


    @Override
    public ServerResponseEntity<List<PayInfoFeignVO>> getPayInfoByOrderids(List<Long> orderIds) {
        List<PayInfoFeignVO> list = payInfoMapper.payedListByOrderIds(orderIds);
        return ServerResponseEntity.success(list);
    }
    
    @Override
    public ServerResponseEntity<List<PayInfoFeignVO>> getNotPayPayInfoByOrderids(List<Long> orderIds) {
        List<PayInfoFeignVO> list = payInfoMapper.getNotPayPayInfoByOrderids(orderIds);
        return ServerResponseEntity.success(list);
    }
    
    @Override
    public ServerResponseEntity<Boolean> cancelWechatPayOrder(String orderNumber) {
        WxPayService wxPayService = wxConfig.getWxPayService(PayType.WECHATPAY);
        try {
            WxPayOrderCloseResult result =  wxPayService.closeOrder(orderNumber);
            log.info("取消订单支付，参数：{},执行结果{}",orderNumber,JSONObject.toJSONString(result));
        }catch (Exception e){
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("取消订单支付失败。");
        }

        return ServerResponseEntity.success(true);
    }

    @Override
    public ServerResponseEntity<Boolean> livestorePay(LiveStorePayInfoVO liveStorePayInfoVO) {
        log.info("视频号支付成功，生成支付记录，参数信息:{}", JSONObject.toJSONString(liveStorePayInfoVO));
        /**
         * 1，保存payinfo记录，状态为支付成功
         * 2，修改订单的状态为支付成功。
         */
        ServerResponseEntity<EsOrderBO> orderBOServerResponseEntity = orderFeignClient.getEsOrderByOrderNumber(liveStorePayInfoVO.getOrderNumber());
        if(orderBOServerResponseEntity.isFail() || orderBOServerResponseEntity.getData()==null){
            Assert.faild(StrUtil.format("视频号支付成功回调，查询订单信息失败。执行参数信息:{}",JSONObject.toJSONString(liveStorePayInfoVO)));
        }
        EsOrderBO orderBO = orderBOServerResponseEntity.getData();
        //如果已经支付成功直接返回支付成功
        if(orderBO.getIsPayed()==1){
            return ServerResponseEntity.success(true);
        }

        PayInfo dbPayInfo = payInfoMapper.getPayInfoByOrderIds(StrUtil.toString(orderBO.getOrderId()));
        if(dbPayInfo != null && dbPayInfo.getPayStatus()==1){
            return ServerResponseEntity.success(true);
        }


        // 支付单号
        ServerResponseEntity<Long> segmentIdResponse = segmentFeignClient.getSegmentId(DistributedIdKey.MALL4CLOUD_PAY);
        if (!segmentIdResponse.isSuccess()) {
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
        Long payId = segmentIdResponse.getData();
        PayInfo payInfo = new PayInfo();
        payInfo.setPayId(payId);
        payInfo.setBizPayNo(liveStorePayInfoVO.getTransactionId());
        payInfo.setUserId(orderBO.getUserId());
        payInfo.setPayAmount(orderBO.getActualTotal());
        payInfo.setPayScore(0l);
        payInfo.setPayStatus(PayStatus.PAYED.value());
        payInfo.setSysType(0);
        payInfo.setPayType(1);
        payInfo.setVersion(0);
        payInfo.setCallbackTime(new Date());
        payInfo.setCallbackContent(JSONObject.toJSONString(liveStorePayInfoVO));
        // 保存多个支付订单号
        payInfo.setOrderIds(StrUtil.toString(orderBO.getOrderId()));

        payInfo.setPayEntry(PayEntry.ORDER.value());

        // 保存支付信息
        payInfoMapper.save(payInfo);

        /**
         * 调用支付成功mq 做后续操作
         */
        List<Long> orderIds = new ArrayList<>();
        orderIds.add(orderBO.getOrderId());
        // 发送消息，订单支付成功
        SendResult sendResult = orderNotifyTemplate.syncSend(new PayNotifyBO(orderIds, PayType.instance(1).value(), payInfo.getPayId()));

        if (sendResult == null || sendResult.getMessageId() == null) {
            // 消息发不出去就抛异常，因为订单回调会有多次，几乎不可能每次都无法发送出去，发的出去无所谓因为接口是幂等的
            throw new LuckException(ResponseEnum.EXCEPTION);
        }

        return ServerResponseEntity.success(true);
    }

    @Override
    public ServerResponseEntity<Boolean> livestoreOrderCancel(LiveStoreOrderCancelInfoVO liveStoreOrderCancelInfoVO) {
        log.info("视频号支付成功，生成支付记录，参数信息:{}", JSONObject.toJSONString(liveStoreOrderCancelInfoVO));
        /**
         * 1，保存payinfo记录，状态为支付成功
         * 2，修改订单的状态为支付成功。
         */
        ServerResponseEntity<EsOrderBO> orderBOServerResponseEntity = orderFeignClient.getEsOrderByOrderNumber(liveStoreOrderCancelInfoVO.getOrderNumber());
        if(orderBOServerResponseEntity.isFail() || orderBOServerResponseEntity.getData()==null){
            Assert.faild(StrUtil.format("视频号支付成功回调，查询订单信息失败。执行参数信息:{}",JSONObject.toJSONString(liveStoreOrderCancelInfoVO)));
        }

        EsOrderBO order = orderBOServerResponseEntity.getData();
        if (!Objects.equals(order.getStatus(), OrderStatus.UNPAY.value()) && !Objects.equals(order.getStatus(), OrderStatus.CLOSE.value())) {
            // 订单已支付，无法取消订单
            return ServerResponseEntity.showFailMsg("订单已支付，无法取消订单");
        }

        orderFeignClient.stdCancelOrder(Collections.singletonList(order.getOrderId()));

        return ServerResponseEntity.success(true);
    }

    @Override
    public ServerResponseEntity<Boolean> livestoreOrderConfirm(LiveStorePayInfoVO liveStorePayInfoVO) {
        log.info("视频号支付成功，生成支付记录，参数信息:{}", JSONObject.toJSONString(liveStorePayInfoVO));
        /**
         * 1，保存payinfo记录，状态为支付成功
         * 2，修改订单的状态为支付成功。
         */
        ServerResponseEntity<EsOrderBO> orderBOServerResponseEntity = orderFeignClient.getEsOrderByOrderNumber(liveStorePayInfoVO.getOrderNumber());
        if(orderBOServerResponseEntity.isFail() || orderBOServerResponseEntity.getData()==null){
            Assert.faild(StrUtil.format("视频号支付成功回调，查询订单信息失败。执行参数信息:{}",JSONObject.toJSONString(liveStorePayInfoVO)));
        }
        EsOrderBO orderBO = orderBOServerResponseEntity.getData();
        orderFeignClient.stdOrderConfirm(orderBO.getOrderId());
        return ServerResponseEntity.success(true);
    }

    @Override
    public ServerResponseEntity<GetPayInfoByOrderIdsAndPayTypeVO> getPayInfoByOrderIdsAndPayType(List<Long> orderIds, Integer payType) {
        GetPayInfoByOrderIdsAndPayTypeVO list = payInfoMapper.getPayInfoByOrderIdsAndPayType(orderIds, payType);
        return ServerResponseEntity.success(list);
    }
    
    @Override
    public ServerResponseEntity<Boolean> sqbOrderPaySuccess(SQBOrderPaySuccessBO sqbOrderPaySuccessBO) {
        log.info("收钱吧订单支付成功，更新支付记录，参数信息:{}", JSONObject.toJSONString(sqbOrderPaySuccessBO));
        PayInfo dbPayInfo = payInfoMapper.getPayInfoByOrderNumber(sqbOrderPaySuccessBO.getOrderNumber());
        if(Objects.nonNull(dbPayInfo)){
            PayInfoResultBO payInfoResultBO = new PayInfoResultBO();
            payInfoResultBO.setPaySuccess(true);
            payInfoResultBO.setPayId(dbPayInfo.getPayId());
            payInfoResultBO.setCallbackContent(sqbOrderPaySuccessBO.getCallbackContent());
            payInfoResultBO.setPayAmount(dbPayInfo.getPayAmount());
            payInfoResultBO.setBizPayNo(sqbOrderPaySuccessBO.getBizPayNo());
            //调用支付成功接口
            payNoticeManager.noticeOrder(payInfoResultBO, dbPayInfo);
        }
        return ServerResponseEntity.success(true);
    }
}
