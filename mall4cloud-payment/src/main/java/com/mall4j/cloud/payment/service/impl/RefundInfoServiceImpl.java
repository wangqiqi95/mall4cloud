package com.mall4j.cloud.payment.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.SendResult;
import com.mall4j.cloud.api.biz.dto.channels.response.EcBaseResponse;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import com.mall4j.cloud.api.biz.feign.ChannlesFeignClient;
import com.mall4j.cloud.api.biz.feign.LiveStoreClient;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.order.feign.OrderRefundFeignClient;
import com.mall4j.cloud.api.order.vo.OrderRefundVO;
import com.mall4j.cloud.api.payment.bo.AftersaleMerchantHandleRefundOverdueRequest;
import com.mall4j.cloud.common.constant.PayType;
import com.mall4j.cloud.common.database.dto.PageDTO;
import com.mall4j.cloud.common.database.util.PageUtil;
import com.mall4j.cloud.common.database.vo.PageVO;
import com.mall4j.cloud.common.exception.Assert;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.order.bo.PayRefundBO;
import com.mall4j.cloud.common.order.bo.RefundNotifyBO;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.rocketmq.OnsMQTemplate;
import com.mall4j.cloud.common.util.Arith;
import com.mall4j.cloud.payment.bo.RefundInfoBO;
import com.mall4j.cloud.payment.constant.RefundStatus;
import com.mall4j.cloud.payment.manager.PayManager;
import com.mall4j.cloud.payment.mapper.PayInfoMapper;
import com.mall4j.cloud.payment.mapper.RefundInfoMapper;
import com.mall4j.cloud.payment.model.PayInfo;
import com.mall4j.cloud.payment.model.RefundInfo;
import com.mall4j.cloud.payment.service.RefundInfoService;
import com.mall4j.cloud.payment.vo.AccountDetailVO;
import com.mall4j.cloud.payment.vo.RefundInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 退款信息
 *
 * @author FrozenWatermelon
 * @date 2021-03-11 14:45:01
 */
@Slf4j
@Service
@RefreshScope
public class RefundInfoServiceImpl implements RefundInfoService {

    @Autowired
    private RefundInfoMapper refundInfoMapper;
    @Autowired
    private PayInfoMapper payInfoMapper;
    @Autowired
    private PayManager payManager;
    @Value("${application.domainUrl}")
    private String domainUrl;
    @Autowired
    private OnsMQTemplate orderRefundSuccessTemplate;
    @Autowired
    OrderFeignClient orderFeignClient;
    @Autowired
    LiveStoreClient liveStoreClient;
    @Autowired
    OrderRefundFeignClient orderRefundFeignClient;
    @Autowired
    ChannlesFeignClient channlesFeignClient;

    @Override
    public PageVO<RefundInfo> page(PageDTO pageDTO) {
        return PageUtil.doPage(pageDTO, () -> refundInfoMapper.list());
    }

    @Override
    public RefundInfo getByRefundId(Long refundId) {
        return refundInfoMapper.getByRefundId(refundId);
    }

    @Override
    public RefundInfo getByRefundNumber(String refundNumber) {
        return refundInfoMapper.getByRefundNumber(refundNumber);
    }

    @Override
    public void save(RefundInfo refundInfo) {
        refundInfoMapper.save(refundInfo);
    }

    @Override
    public void update(RefundInfo refundInfo) {
        refundInfoMapper.update(refundInfo);
    }

    @Override
    public void deleteById(Long refundId) {
        refundInfoMapper.deleteById(refundId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doRefund(PayRefundBO payRefundBO) {
        log.info("执行退款，参数信息：{}", JSONObject.toJSONString(payRefundBO));
        if (refundInfoMapper.countByRefundId(payRefundBO.getRefundId()) >0) {
            log.info("退款单id：{}，已经执行过退款，不再次执行退款。", JSONObject.toJSONString(payRefundBO));
            return;
        }

        ServerResponseEntity<EsOrderBO> serverResponse = orderFeignClient.getEsOrder(payRefundBO.getOrderId());
        if(serverResponse==null || serverResponse.isFail() || serverResponse.getData()==null){
            Assert.faild(StrUtil.format("执行退款操作，订单id不存在，参数信息：{}",JSONObject.toJSONString(payRefundBO)));
        }
        EsOrderBO orderBO = serverResponse.getData();
        //视频号订单退款调用视频号的退款逻辑
        if(StrUtil.isNotEmpty(orderBO.getTraceId())){
            ServerResponseEntity<BaseResponse> baseResponseServerResponse = liveStoreClient.ecaftersaleAcceptrefund(payRefundBO.getRefundId());
            if(baseResponseServerResponse==null || baseResponseServerResponse.getData()==null ||baseResponseServerResponse.getData().getErrcode()>0){
                Assert.faild(StrUtil.format("视频号同意退款失败。请求参数信息:{},微信返回信息:{}",payRefundBO.getRefundId(),JSONObject.toJSONString(baseResponseServerResponse)));
            }
            return;
        }
        //视频号4.0 订单退款调用视频号4.0的退款逻辑
        if(orderBO.getOrderSource() ==3){
            ServerResponseEntity<OrderRefundVO> refundResponse = orderRefundFeignClient.getOrderRefundByRefundId(payRefundBO.getRefundId());
            if(refundResponse==null || refundResponse.isFail() || refundResponse.getData()==null){
                Assert.faild(StrUtil.format("退单查询失败，退单id:{}",payRefundBO.getRefundId()));
            }
            OrderRefundVO orderRefund = refundResponse.getData();
            ServerResponseEntity<EcBaseResponse> response = channlesFeignClient.ecaftersaleAcceptrefund(orderRefund.getAftersaleId());
            if(response==null || response.getData()==null || response.getData().getErrcode()>0){
                Assert.faild(StrUtil.format("视频号4同意退款失败。请求参数信息:{},微信返回信息:{}",orderRefund.getAftersaleId(),JSONObject.toJSONString(response)));
            }
            return;
        }

        //先按照视频号3和视频号4的逻辑走，都不是执行下面逻辑。
        PayInfo payInfo = payInfoMapper.getByPayId(payRefundBO.getPayId());
        if (payInfo == null) {
            log.info("退款单id：{}，payid支付记录为空，不执行退款操作。", JSONObject.toJSONString(payRefundBO));
            return;
        }


        RefundInfo refundInfo = new RefundInfo();
        refundInfo.setRefundId(payRefundBO.getRefundId());
        refundInfo.setRefundNumber(payRefundBO.getRefundNumber());
        refundInfo.setRefundAmount(payRefundBO.getRefundAmount());
        refundInfo.setOrderId(payRefundBO.getOrderId());
        refundInfo.setPayId(payRefundBO.getPayId());
        refundInfo.setPayType(payInfo.getPayType());


        if(payRefundBO.getUnSuccessGroupOrder() == null){
            refundInfo.setUnSuccessGroupOrder(0);
        }else{
            refundInfo.setUnSuccessGroupOrder(payRefundBO.getUnSuccessGroupOrder());
        }
        refundInfo.setUserId(payInfo.getUserId());
        // 保存退款信息 refund_id是唯一的，所以能确保只进行一次退款
        refundInfoMapper.save(refundInfo);



        RefundInfoBO refundInfoBO = new RefundInfoBO();
        refundInfoBO.setOrderNumber(orderBO.getOrderNumber());
        refundInfoBO.setBizPayNo(payInfo.getBizPayNo());
        refundInfoBO.setRefundId(payRefundBO.getRefundId());
        refundInfoBO.setRefundNumber(payRefundBO.getRefundNumber());
        refundInfoBO.setRefundAmount(payRefundBO.getRefundAmount());
        refundInfoBO.setPayType(PayType.instance(payInfo.getPayType()));
        refundInfoBO.setPayId(payRefundBO.getPayId());

        refundInfoBO.setPayAmount(payInfo.getPayAmount());
        refundInfoBO.setNotifyUrl(domainUrl + "/ua/notice/refund/order/" + payInfo.getPayType());
        refundInfoBO.setUserId(payInfo.getUserId());
        refundInfoBO.setStoreId(orderBO.getStoreId());
        refundInfoBO.setSalesTime(orderBO.getCreateTime());
        // 支付宝支付，直接退款都不需要回调的
        boolean refundSuccess = payManager.doRefund(refundInfoBO);

        // 是否直接进行退款，不需要任何回调
        if (!Objects.equals(payRefundBO.getOnlyRefund(), 1) && refundSuccess) {
            refundSuccess(refundInfo);
        }
    }

    @Override
    public void liveStoreAftersaleMerchantHandleRefundOverdue(AftersaleMerchantHandleRefundOverdueRequest refundOverdueRequest) {

        ServerResponseEntity<OrderRefundVO> serverResponse = orderRefundFeignClient.getOrderRefundByAftersaleId(refundOverdueRequest.getAftersaleId());
        if(serverResponse.isFail() || serverResponse.getData()==null){
            Assert.faild(StrUtil.format("视频号退单查询不到对应的售后记录，视频号售后单id:{}",refundOverdueRequest.getAftersaleId()));
        }
        OrderRefundVO orderRefundVO = serverResponse.getData();

        PayInfo payInfo = payInfoMapper.getByOrderId(orderRefundVO.getOrderId());
        if (payInfo == null) {
            log.info("退款单id：{}，payid支付记录为空，不执行退款操作。", JSONObject.toJSONString(payInfo));
            return;
        }
        RefundInfo dbRefundInfo = refundInfoMapper.getByRefundId(orderRefundVO.getRefundId());
        if(dbRefundInfo!=null ){
            log.info("退款单id：{}，记录已经存在。", JSONObject.toJSONString(dbRefundInfo));
            return;
        }

        RefundInfo refundInfo = new RefundInfo();
        refundInfo.setRefundId(orderRefundVO.getRefundId());
        refundInfo.setRefundNumber(orderRefundVO.getRefundNumber());
        refundInfo.setRefundAmount(orderRefundVO.getRefundAmount());
        refundInfo.setOrderId(orderRefundVO.getOrderId());
        refundInfo.setPayId(payInfo.getPayId());
        refundInfo.setPayType(payInfo.getPayType());

        refundInfo.setUnSuccessGroupOrder(0);
        refundInfo.setUserId(payInfo.getUserId());
        // 保存退款信息 refund_id是唯一的，所以能确保只进行一次退款
        refundInfoMapper.save(refundInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundSuccess(RefundInfo refundInfo) {
        // 已经退款
        if (Objects.equals(refundInfo.getRefundStatus(), RefundStatus.REFUNDED.value())) {
            return;
        }
        refundInfo.setCallbackContent(refundInfo.getCallbackContent());
        refundInfo.setCallbackTime(new Date());
        refundInfo.setRefundStatus(RefundStatus.REFUNDED.value());
        refundInfoMapper.update(refundInfo);

        RefundNotifyBO refundNotifyBO = new RefundNotifyBO();
        refundNotifyBO.setRefundId(refundInfo.getRefundId());
        refundNotifyBO.setRefundNumber(refundInfo.getRefundNumber());
        refundNotifyBO.setRefundAmount(refundInfo.getRefundAmount());
        refundNotifyBO.setOrderId(refundInfo.getOrderId());
        refundNotifyBO.setPayId(refundInfo.getPayId());
        refundNotifyBO.setUnSuccessGroupOrder(refundInfo.getUnSuccessGroupOrder());
        // 通知订单将订单的退款状态改变
        SendResult sendResult = orderRefundSuccessTemplate.syncSend(refundNotifyBO);
        if (sendResult == null || sendResult.getMessageId() == null) {
            // 这个回调方法会多次进行回调
            throw new LuckException(ResponseEnum.EXCEPTION);
        }
    }

    @Override
    public AccountDetailVO getRefundAccountDetail(Date startTime, Date endTime) {
        AccountDetailVO accountDetailVO = refundInfoMapper.getRefundAccountDetail(startTime, endTime);
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
    public PageVO<RefundInfoVO> getRefundInfoPage(PageDTO pageDTO, Date startTime, Date endTime) {
        PageVO<RefundInfoVO> pageVO = PageUtil.doPage(pageDTO, () -> refundInfoMapper.getRefundInfoVO(startTime, endTime));
        long index = (long)pageDTO.getPageSize() * (pageDTO.getPageNum() - 1);
        for (RefundInfoVO refundInfoVO : pageVO.getList()) {
            refundInfoVO.setIndex(index);
            index++;
        }
        return pageVO;
    }
}
