package com.mall4j.cloud.payment.manager;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayAppOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayMwebOrderResult;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.mall4j.cloud.api.docking.skq_sqb.config.SQBParams;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.ResultNoticeTender;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.RefundTender;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByPurchase;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByResultNotice;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.common.ResultNoticeRequest;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.SQBBodyByRefund;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBPurchaseResp;
import com.mall4j.cloud.api.docking.skq_sqb.dto.response.SQBRefundResp;
import com.mall4j.cloud.api.docking.skq_sqb.feign.LitePosApiFeignClient;
import com.mall4j.cloud.api.docking.skq_sqb.utils.SQBSignUtils;
import com.mall4j.cloud.api.order.bo.EsOrderBO;
import com.mall4j.cloud.api.order.feign.OrderFeignClient;
import com.mall4j.cloud.api.payment.constant.SQBResultNoticeOrderStatus;
import com.mall4j.cloud.api.platform.config.FeignShopConfig;
import com.mall4j.cloud.api.user.bo.BalancePayBO;
import com.mall4j.cloud.api.user.bo.BalanceRefundBO;
import com.mall4j.cloud.api.user.feign.UserBalanceLogClient;
import com.mall4j.cloud.common.bean.WxMiniApp;
import com.mall4j.cloud.common.constant.PayType;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.util.IpHelper;
import com.mall4j.cloud.common.util.Json;
import com.mall4j.cloud.common.util.PriceUtil;
import com.mall4j.cloud.common.util.RandomUtil;
import com.mall4j.cloud.payment.bo.PayInfoBO;
import com.mall4j.cloud.payment.bo.PayInfoResultBO;
import com.mall4j.cloud.payment.bo.RefundInfoBO;
import com.mall4j.cloud.payment.bo.RefundInfoResultBO;
import com.mall4j.cloud.payment.config.AliPayConfig;
import com.mall4j.cloud.payment.config.WxConfig;
import com.mall4j.cloud.payment.constant.PayStatus;
import com.mall4j.cloud.payment.mapper.PayInfoMapper;
import com.mall4j.cloud.payment.model.PayInfo;
import com.mall4j.cloud.payment.service.PayInfoService;
import com.mall4j.cloud.payment.vo.SQBPayInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.*;
/**
 * 统一支付工具
 *
 * @author FrozenWatermelon
 */
@Service
public class PayManager {

    @Autowired
    private AliPayConfig aliPayConfig;

    @Autowired
    private FeignShopConfig feignShopConfig;

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private PayInfoService payInfoService;

    @Autowired
    private PayNoticeManager payNoticeManager;

    @Autowired
    private UserBalanceLogClient userBalanceLogClient;
    @Autowired
    private LitePosApiFeignClient litePosApiFeignClient;
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Resource
    private PayInfoMapper payInfoMapper;
    @Autowired
    private SQBParams sqbParams;


    private static final String WX_SUCCESS_XML = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    private static final String ALI_SUCCESS = "success";
    public static final String SUCCESS = "SUCCESS";
    private static final String TRADE_SUCCESS = "TRADE_SUCCESS";

    private static final String TRADE_STATUS_KEY = "trade_status";

    private static final Logger logger = LoggerFactory.getLogger(PayManager.class);

    public ServerResponseEntity<?> doPay(PayInfoBO payInfo) throws WxPayException, AlipayApiException {
        String notifyUrl = payInfo.getApiNoticeUrl();

        PayType payType = PayType.instance(payInfo.getPayType());
        if (isWxPay(payType)) {
            return doWxPay(payInfo, notifyUrl, payType);
        } else if (Objects.equals(payType, PayType.ALIPAY)) {
            //创建API对应的request
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

            //在公共参数中设置回跳和通知地址
            alipayRequest.setNotifyUrl(notifyUrl);
            alipayRequest.setReturnUrl(payInfo.getReturnUrl());
            AlipayTradePagePayModel alipayTradePagePayModel = new AlipayTradePagePayModel();
            alipayTradePagePayModel.setOutTradeNo(String.valueOf(payInfo.getPayId()));
            alipayTradePagePayModel.setBody(payInfo.getBody());
            alipayTradePagePayModel.setSubject(payInfo.getBody());
            alipayTradePagePayModel.setTotalAmount(PriceUtil.toDecimalPrice(payInfo.getPayAmount()).toString());
            alipayTradePagePayModel.setProductCode("FAST_INSTANT_TRADE_PAY");
            alipayRequest.setBizModel(alipayTradePagePayModel);

            return ServerResponseEntity.success(aliPayConfig.getAlipayClient().pageExecute(alipayRequest).getBody());
        } else if (Objects.equals(payType, PayType.ALIPAY_H5)) {

            AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
            alipayRequest.setNotifyUrl(notifyUrl);
            alipayRequest.setReturnUrl(payInfo.getReturnUrl());

            AlipayTradeWapPayModel alipayTradeWapPayModel = new AlipayTradeWapPayModel();
            alipayTradeWapPayModel.setOutTradeNo(String.valueOf(payInfo.getPayId()));
            alipayTradeWapPayModel.setSubject(payInfo.getBody());
            alipayTradeWapPayModel.setTotalAmount(PriceUtil.toDecimalPrice(payInfo.getPayAmount()).toString());
            alipayTradeWapPayModel.setProductCode("QUICK_WAP_PAY");
            alipayRequest.setBizModel(alipayTradeWapPayModel);
            return ServerResponseEntity.success(aliPayConfig.getAlipayClient().pageExecute(alipayRequest).getBody());
        }
        // 支付宝app支付
        else if (Objects.equals(payType, PayType.ALIPAY_APP)) {

            AlipayTradeAppPayRequest alipayRequest = new AlipayTradeAppPayRequest();
            alipayRequest.setNotifyUrl(notifyUrl);
            AlipayTradeAppPayModel alipayTradeAppPayModel = new AlipayTradeAppPayModel();
            alipayTradeAppPayModel.setOutTradeNo(String.valueOf(payInfo.getPayId()));
            alipayTradeAppPayModel.setSubject(payInfo.getBody());
            alipayTradeAppPayModel.setTotalAmount(PriceUtil.toDecimalPrice(payInfo.getPayAmount()).toString());
            alipayTradeAppPayModel.setProductCode("QUICK_MSECURITY_PAY");
            alipayRequest.setBizModel(alipayTradeAppPayModel);

            String body = aliPayConfig.getAlipayClient().sdkExecute(alipayRequest).getBody();
            return ServerResponseEntity.success(body);
        }
        else if (Objects.equals(payType, PayType.BALANCE)){
            // 创建余额支付记录
            BalancePayBO balancePayBO = new BalancePayBO();
            balancePayBO.setChangeBalance(payInfo.getPayAmount());
            balancePayBO.setPayId(payInfo.getPayId());
            if (Objects.nonNull(payInfo.getIsVip())){
                balancePayBO.setIsVip(payInfo.getIsVip());
            }else{
                balancePayBO.setIsVip(false);
            }
            ServerResponseEntity<Void> insertServerResponseEntity = userBalanceLogClient.insertBalancePayLog(balancePayBO);
            if (!insertServerResponseEntity.isSuccess()) {
                return ServerResponseEntity.transform(insertServerResponseEntity);
            }
            return ServerResponseEntity.success(payInfo.getPayId());
        }
        // 积分支付
        else if (Objects.equals(payType, PayType.SCOREPAY)) {
            PayInfoResultBO payInfoResultBO = new PayInfoResultBO();
            payInfoResultBO.setPaySuccess(true);
            payInfoResultBO.setPayId(payInfo.getPayId());
            payInfoResultBO.setCallbackContent("余额支付成功");
            payInfoResultBO.setPayAmount(payInfo.getPayAmount());
            payInfoResultBO.setBizPayNo(payInfo.getPayId().toString());
            payNoticeManager.noticeOrder(payInfoResultBO, payInfoService.getByPayId(payInfo.getPayId()));
        }
        // 收钱吧轻POS支付
        else if(Objects.equals(payType, PayType.SQB_LITE_POS)){
            
            SQBPayInfoVO sqbPayInfoVO = new SQBPayInfoVO();
            WxMiniApp wxMiniApp = feignShopConfig.getWxMiniApp();
            String orderToken = payInfo.getOrderToken();

            //判断是不是重复提交订单,orderToken有值,代表订单已经传到收钱吧,前端直接唤起收银台就可以了
            if(StrUtil.isNotEmpty(orderToken)){
                sqbPayInfoVO.setAppid(wxMiniApp.getAppId());
                sqbPayInfoVO.setOpenid(payInfo.getBizUserId());
                sqbPayInfoVO.setToken(orderToken);
    
                return ServerResponseEntity.success(sqbPayInfoVO);
            }
            
            //组装购买接口请求参数
            SQBBodyByPurchase sqbBodyByPurchase = new SQBBodyByPurchase();
    
            sqbBodyByPurchase.setRequest_id(RandomUtil.getUniqueKey(true));
            sqbBodyByPurchase.setBrand_code(sqbParams.getBrand_code());
            sqbBodyByPurchase.setStore_sn(sqbParams.getStore_sn());
            sqbBodyByPurchase.setWorkstation_sn("0");
            sqbBodyByPurchase.setCheck_sn(payInfo.getOrderNumber());
            sqbBodyByPurchase.setScene("5");
            sqbBodyByPurchase.setSales_time(DateUtil.format(payInfo.getCreateTime(), "yyyy-MM-dd'T'HH:mm:ssXXX"));
            //过期时间是配置的,要取nacos上的值,跟RocketMQ延迟消息等级的时间要一致
            sqbBodyByPurchase.setExpire_time(sqbParams.getExpire_time());
            sqbBodyByPurchase.setAmount(String.valueOf(payInfo.getPayAmount()));
            sqbBodyByPurchase.setCurrency("156");
            //sqbBodyByPurchase.setSubject(payInfo.getBody());
            // 在支付宝账单回显小程序订单编号
            sqbBodyByPurchase.setSubject(payInfo.getOrderNumber());
            sqbBodyByPurchase.setOperator(payInfo.getBody());
            sqbBodyByPurchase.setCustomer(payInfo.getBizUserId());
            sqbBodyByPurchase.setIndustry_code("0");
            sqbBodyByPurchase.setPos_info(payInfo.getBody());
            sqbBodyByPurchase.setNotify_url(payInfo.getApiNoticeUrl());
            sqbBodyByPurchase.setReturn_url(payInfo.getReturnUrl());
    
            ServerResponseEntity<SQBPurchaseResp> responseResult = litePosApiFeignClient.purchase(sqbBodyByPurchase);
            if (!responseResult.isSuccess()) {
                logger.error("订单号："+payInfo.getOrderNumber()+","+responseResult.getMsg());
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
            
            SQBPurchaseResp sqbPurchaseResp = responseResult.getData();
            if(Objects.isNull(sqbPurchaseResp)){
                throw new LuckException(ResponseEnum.EXCEPTION);
            }
            // 更新orderToken
            PayInfo updatePayInfo = new PayInfo();
            updatePayInfo.setPayId(payInfo.getPayId());
            updatePayInfo.setOrderToken(sqbPurchaseResp.getOrderToken());
            payInfoMapper.update(updatePayInfo);
    
            //组装参数给前端唤起收钱吧收银台
            sqbPayInfoVO.setAppid(wxMiniApp.getAppId());
            sqbPayInfoVO.setOpenid(payInfo.getBizUserId());
            sqbPayInfoVO.setToken(sqbPurchaseResp.getOrderToken());
            
            return ServerResponseEntity.success(sqbPayInfoVO);
        }
        return null;
    }


    private ServerResponseEntity<?> doWxPay(PayInfoBO payInfo, String notifyUrl, PayType payType) throws WxPayException {
        WxPayService wxPayService = wxConfig.getWxPayService(payType);
        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody(payInfo.getBody());
        orderRequest.setOutTradeNo(String.valueOf(payInfo.getOrderNumber()));
//        orderRequest.setOutTradeNo(String.valueOf(payInfo.getPayId()));
        //新增attach字段 传 OrderNumber
        orderRequest.setAttach(payInfo.getOrderNumber());
        orderRequest.setTotalFee((int)(long)payInfo.getPayAmount());
        orderRequest.setSpbillCreateIp(IpHelper.getIpAddr());
        orderRequest.setOpenid(payInfo.getBizUserId());

        orderRequest.setNotifyUrl(notifyUrl);

        // 微信小程序支付 || 微信公众号支付
        if (Objects.equals(payType, PayType.WECHATPAY) || Objects.equals(payType, PayType.WECHATPAY_MP)) {
            orderRequest.setTradeType(WxPayConstants.TradeType.JSAPI);
            WxPayMpOrderResult wxPayMpOrderResult = wxPayService.createOrder(orderRequest);
            return ServerResponseEntity.success(wxPayMpOrderResult);
        }
        // 微信网页支付
        else if (Objects.equals(payType, PayType.WECHATPAY_SWEEP_CODE)) {
            orderRequest.setProductId(String.valueOf(payInfo.getPayId()));
            // 生成微信二维码
            orderRequest.setTradeType(WxPayConstants.TradeType.NATIVE);
            WxPayNativeOrderResult wxPayNativeOrderResult = wxPayService.createOrder(orderRequest);

            return ServerResponseEntity.success(wxPayNativeOrderResult.getCodeUrl());
        }
        // 微信H5支付
        else if (Objects.equals(payType, PayType.WECHATPAY_H5)) {
            // 生成微信二维码
            orderRequest.setTradeType(WxPayConstants.TradeType.MWEB);
            String s = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"\",\"wap_name\": \"\"}}";
            orderRequest.setSceneInfo(s);
            WxPayMwebOrderResult wxPayMwebOrderResult = wxPayService.createOrder(orderRequest);
            return ServerResponseEntity.success(wxPayMwebOrderResult.getMwebUrl());
        }
        // 微信app支付
        else if (Objects.equals(payType, PayType.WECHATPAY_APP)) {
            orderRequest.setTradeType(WxPayConstants.TradeType.APP);
            WxPayAppOrderResult wxPayAppOrderResult = wxPayService.createOrder(orderRequest);
            return ServerResponseEntity.success(wxPayAppOrderResult);
        }
        return null;
    }
    
    public PayInfoResultBO validateAndGetPayInfo(HttpServletRequest request, PayType payType, String xmlData) throws UnsupportedEncodingException, WxPayException, AlipayApiException {

        PayInfoResultBO payInfoBo = new PayInfoResultBO();
        
        if (Objects.equals(payType, PayType.SQB_LITE_POS)) {
            payInfoBo.setPaySuccess(false);
    
            boolean flag = SQBSignUtils.callbackSign(xmlData,sqbParams.getPublicKey());
            logger.info("收钱吧支付成功回调，请求参数验签结果：{}，请检查数据，返回报文：{}", flag ,xmlData);
            if ( flag ){
                JSONObject jsonObject =  JSONObject.parseObject(xmlData);
                ResultNoticeRequest resultNoticeRequest = jsonObject.getObject("request", ResultNoticeRequest.class);
                SQBBodyByResultNotice sqbBodyByResultNotice = resultNoticeRequest.getBody();
                
                //根据orderNumber 查询订单支付信息
                PayInfo dbPayInfo = payInfoMapper.getPayInfoByOrderNumber(sqbBodyByResultNotice.getCheck_sn());
    
                if(dbPayInfo==null){
                    logger.error("支付成功回调，支付订单号已支付，或者订单对应支付单不存在。返回报文：{}",xmlData);
                    return payInfoBo;
                }
                
                //订单取消和订单操作失败,不用调用取消接口，让mq取消订单就可以了
                if (Objects.equals(SQBResultNoticeOrderStatus.CANCELED.value(),Integer.valueOf(sqbBodyByResultNotice.getOrder_status()))
                    || Objects.equals(SQBResultNoticeOrderStatus.FAIL.value(),Integer.valueOf(sqbBodyByResultNotice.getOrder_status()))){
                    
                    //通知收钱吧回调结束,更新支付记录表信息
                    PayInfo updatePayInfo = new PayInfo();
                    updatePayInfo.setPayId(dbPayInfo.getPayId());
                    updatePayInfo.setCallbackContent(xmlData);
                    updatePayInfo.setCallbackTime(new Date());
                    updatePayInfo.setPayStatus(PayStatus.CANCELED_OR_CANCELED.value());
                    payInfoMapper.update(updatePayInfo);
                    
                    payInfoBo.setSuccessString(SQBSignUtils.callbackContent(sqbParams.getAppid(),sqbParams.getPrivateKey()));
                }
    
                //支付成功
                if (Objects.equals(SQBResultNoticeOrderStatus.SUCCESS.value(),Integer.valueOf(sqbBodyByResultNotice.getOrder_status()))){
                    //订单的流水信息
                    List<ResultNoticeTender> tenders = sqbBodyByResultNotice.getTenders();
                    if(CollectionUtil.isNotEmpty(tenders)){
                        
                        ResultNoticeTender resultNoticeTender = tenders.get(0);
                        if( Objects.nonNull(resultNoticeTender) && "3".equals(resultNoticeTender.getPay_status()) ){
                            payInfoBo.setOrderNumber(sqbBodyByResultNotice.getCheck_sn());
                            payInfoBo.setOrderIds(dbPayInfo.getOrderIds());
                            payInfoBo.setPayId(dbPayInfo.getPayId());
                            payInfoBo.setBizPayNo(resultNoticeTender.getTender_sn());
                            payInfoBo.setPaySuccess(true);
                            payInfoBo.setSuccessString(SQBSignUtils.callbackContent(sqbParams.getAppid(),sqbParams.getPrivateKey()));
                            payInfoBo.setPayAmount(Long.valueOf(resultNoticeTender.getAmount()));
                            payInfoBo.setCallbackContent(xmlData);
                        }
                    }
                }
            }
            return payInfoBo;
        }
        
        // 微信支付
        if (isWxPay(payType)) {
            WxPayService wxPayService = wxConfig.getWxPayService(payType);
            WxPayOrderNotifyResult parseOrderNotifyResult = wxPayService.parseOrderNotifyResult(xmlData);
//            payInfoBo.setPayId(Long.valueOf(parseOrderNotifyResult.getOutTradeNo()));
            //修改outtradeno为订单编号 。这里也要根据订单编号修改支付成功回调。
//            payInfoBo.setOrderIds(parseOrderNotifyResult.getOutTradeNo());
            payInfoBo.setOrderNumber(parseOrderNotifyResult.getOutTradeNo());
            /**
             * 根据orderNumber 查询订单id
             */
            ServerResponseEntity<EsOrderBO> orderResponse = orderFeignClient.getEsOrderByOrderNumber(payInfoBo.getOrderNumber());
            if(orderResponse.isFail() || orderResponse.getData()==null){
                logger.error("支付成功回调，返回的订单编号查询失败，请检查数据，返回报文：{}",xmlData);
                payInfoBo.setPaySuccess(false);
                return payInfoBo;
            }


            EsOrderBO esOrderBO = orderResponse.getData();
            payInfoBo.setOrderIds(StrUtil.toString(esOrderBO.getOrderId()));

            PayInfo dbPayInfo = payInfoMapper.getPayInfoByOrderIds(StrUtil.toString(esOrderBO.getOrderId()));
            if(dbPayInfo==null){
                logger.error("支付成功回调，支付订单号已支付，或者订单对应支付单不存在。返回报文：{}",xmlData);
                payInfoBo.setPaySuccess(false);
                return payInfoBo;
            }

            payInfoBo.setPayId(dbPayInfo.getPayId());
            payInfoBo.setBizPayNo(parseOrderNotifyResult.getTransactionId());
            payInfoBo.setPaySuccess(SUCCESS.equals(parseOrderNotifyResult.getResultCode()));
            payInfoBo.setSuccessString(WX_SUCCESS_XML);
            payInfoBo.setPayAmount(Long.valueOf(parseOrderNotifyResult.getTotalFee()));
            payInfoBo.setCallbackContent(xmlData);
        } else if (isAliPay(payType)) {
            //获取支付宝POST过来反馈信息
            Map<String, String> params = parseAliNotifyAndGetResult(request);
            //商户订单号,之前生成的带用户ID的订单号
            payInfoBo.setPayId(Long.valueOf(params.get("out_trade_no")));
            //支付宝交易号
            payInfoBo.setBizPayNo(params.get("trade_no"));
            payInfoBo.setSuccessString(ALI_SUCCESS);
            payInfoBo.setPayAmount(new BigDecimal(params.get("total_amount")).multiply(new BigDecimal(100)).longValue());
            // 这个不是支付宝退款的通知
            if (StrUtil.isBlank(params.get("refund_fee"))) {
                payInfoBo.setPaySuccess(Objects.equals(params.get(TRADE_STATUS_KEY), TRADE_SUCCESS));
            } else {
                payInfoBo.setPaySuccess(false);
            }

            payInfoBo.setCallbackContent(Json.toJsonString(params));
        }

        if (StrUtil.isNotBlank(payInfoBo.getCallbackContent())) {
            //如果这里要兼容支付宝支付，上面支付宝支付传参的订单id也要修改成为订单编号。
            PayInfo payInfo = new PayInfo();
            payInfo.setPayId(payInfoBo.getPayId());
//            payInfo.setOrderIds(payInfoBo.getOrderIds());
            payInfo.setBizPayNo(payInfoBo.getBizPayNo());
            payInfo.setCallbackContent(payInfoBo.getCallbackContent());
            payInfo.setCallbackTime(new Date());
            payInfoService.update(payInfo);
        }
        return payInfoBo;
    }


    public boolean doRefund(RefundInfoBO refundInfo) {

        PayType payType = refundInfo.getPayType();
        if(Objects.equals(refundInfo.getRefundAmount(),0L)){
            return true;
        }
        // 提交提款操作
        try {
            if (isWxPay(payType)) {
                // 退款操作
                // 生成退款请求对象
                WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
                // 商户订单号  修改为传订单编码
                wxPayRefundRequest.setOutTradeNo(refundInfo.getOrderNumber());
                //
//                wxPayRefundRequest.setTransactionId(refundInfo.getBizPayNo());
                // 订单金额
                wxPayRefundRequest.setTotalFee(refundInfo.getPayAmount().intValue());
                // 退款金额
                wxPayRefundRequest.setRefundFee(refundInfo.getRefundAmount().intValue());
                // 退款编码  修改传退款单号进行退款
//                wxPayRefundRequest.setOutRefundNo(refundInfo.getRefundId().toString());
                wxPayRefundRequest.setOutRefundNo(refundInfo.getRefundNumber());

                // 只进行退款，不需要任何回调
                if (!Objects.equals(refundInfo.getOnlyRefund(), 1)) {
                    // notifyUrl（通知结果）
                    wxPayRefundRequest.setNotifyUrl(refundInfo.getNotifyUrl());
                }
                WxPayService wxPayService = wxConfig.getWxPayService(payType);
                wxPayService.refund(wxPayRefundRequest);

            } else if (isAliPay(payType)) {
                AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
                request.setNotifyUrl(refundInfo.getNotifyUrl());
                AlipayTradeRefundModel alipayTradeRefundModel = new AlipayTradeRefundModel();
                alipayTradeRefundModel.setOutTradeNo(refundInfo.getPayId().toString());
                alipayTradeRefundModel.setRefundAmount(PriceUtil.toDecimalPrice(refundInfo.getRefundAmount()).toString());
                alipayTradeRefundModel.setOutRequestNo(refundInfo.getRefundId().toString());
                request.setBizModel(alipayTradeRefundModel);
                AlipayTradeRefundResponse response = aliPayConfig.getAlipayClient().certificateExecute(request);
                // 对fund_change必须了解为本次退款操作，若相同退款参数调用，第一次返回fund_change=Y，第二次返回fund_change=N，但是第二次仍返回退款信息。
                if (response.isSuccess() && StrUtil.isNotBlank(response.getFundChange())) {
                    return true;
                } else {
                    logger.error("退款失败，Mq会多次回调：" + Json.toJsonString(response));
                    // 因为这个退款是mq调用的，所以如果一次退款失败，就调用多次，做好幂等的操作
                    throw new LuckException(ResponseEnum.EXCEPTION);
                }
            }else if (Objects.equals(payType, PayType.BALANCE)){
                // 注意 只进行退款是不需要回调的 这个参数要传到余额支付那，不然会有问题
                // 退款金额
                BalanceRefundBO balanceRefundBO = new BalanceRefundBO();
                balanceRefundBO.setRefundId(refundInfo.getRefundId());
                balanceRefundBO.setPayId(refundInfo.getPayId());
                balanceRefundBO.setChangeBalance(refundInfo.getRefundAmount());
                balanceRefundBO.setUserId(refundInfo.getUserId());
                ServerResponseEntity<Void> responseEntity = userBalanceLogClient.doRefund(balanceRefundBO);
                if (responseEntity.isSuccess()) {
                    return true;
                } else {
                    logger.error("退款失败，Mq会多次回调：" + Json.toJsonString(responseEntity));
                    // 因为这个退款是mq调用的，所以如果一次退款失败，就调用多次，做好幂等的操作
                    throw new LuckException(ResponseEnum.EXCEPTION);
                }
            }else if (Objects.equals(payType, PayType.SQB_LITE_POS)){
                // 对接收钱吧退款接口
                String body = "商城订单";
                SQBBodyByRefund sqbBodyByRefund = new SQBBodyByRefund();
                List<RefundTender> tenders = new ArrayList<>();

                /**
                 * 组装订单退款主体信息
                 */
                // 请求编号，每次请求必须唯一
                sqbBodyByRefund.setRequest_id(RandomUtil.getUniqueKey(true));
                // 品牌编号，系统对接前由"收钱吧"分配并提供
                sqbBodyByRefund.setBrand_code(sqbParams.getBrand_code());
                // 商户内部使用的门店编号
                sqbBodyByRefund.setStore_sn(sqbParams.getStore_sn());
                // 商户门店名称
                sqbBodyByRefund.setStore_name(body);
                // 门店收银机编号，如果没有请传入"0"
                sqbBodyByRefund.setWorkstation_sn("0");
                // 商户订单号，在商户系统中唯一
                sqbBodyByRefund.setCheck_sn(refundInfo.getRefundNumber());
                // POS 或 电商等业务系统内的实际销售订单号，不同于check_sn
                //sqbBodyByPurchase.setSales_sn(refundInfo.getOrderNumber());
                // 商户订单创建时间
                sqbBodyByRefund.setSales_time(DateUtil.format(refundInfo.getSalesTime(), "yyyy-MM-dd'T'HH:mm:ssXXX"));
                // 订单总金额，精确到分，金额应为负数
                sqbBodyByRefund.setAmount("-".concat(refundInfo.getRefundAmount().toString()));
                // 币种，ISO numeric currency code 如："156"for CNY
                sqbBodyByRefund.setCurrency("156");
                // 订单简短描述，建议传8个字内
                //sqbBodyByRefund.setSubject(body);
                sqbBodyByRefund.setSubject(refundInfo.getRefundNumber());
                // 订单描述
                //sqbBodyByRefund.setDescription(refundInfo.getBody());
                // 操作员，可以传入收款的收银员或导购员
                sqbBodyByRefund.setOperator(body);
                // 可以传入需要备注顾客的信息
                sqbBodyByRefund.setCustomer(body);
                // 行业代码, 0=零售1:酒店; 2:餐饮; 3:文娱; 4:教育;
                sqbBodyByRefund.setIndustry_code("0");
                // 传入商户系统的产品名称、系统编号等信息，便于帮助商户调查问题
                sqbBodyByRefund.setPos_info(body);
                // 通知接收地址。总共回调7次，回调时间间隔：4m,10m,10m,1h,2h,6h,15h。
                sqbBodyByRefund.setNotify_url(refundInfo.getNotifyUrl());
                //sqbBodyByPurchase.setNotify_url("http://83ryzc.natappfree.cc/ua/notice/pay");

                /**
                 * 组装订单流水信息
                 */
                RefundTender refundTender = new RefundTender();
                refundTender.setTransaction_sn(refundInfo.getRefundNumber());
                refundTender.setOriginal_tender_sn(refundInfo.getBizPayNo());
                //refundTender.setAmount(PriceUtil.toDecimalPrice(refundInfo.getRefundAmount()).toString());
                refundTender.setAmount("-".concat(refundInfo.getRefundAmount().toString()));
                refundTender.setPay_status("0");
                refundTender.setCreate_time(DateUtil.format(new Date(), "yyyy-MM-dd'T'HH:mm:ssXXX"));
                tenders.add(refundTender);
                sqbBodyByRefund.setTenders(tenders);

                ServerResponseEntity<SQBRefundResp> refund = litePosApiFeignClient.refund(sqbBodyByRefund);
                if(refund.isFail()){
                    logger.error("退款单号："+refundInfo.getRefundNumber()+","+refund.getMsg());
                    throw new LuckException(ResponseEnum.EXCEPTION);
                }
            }
        } catch (WxPayException e) {
            e.printStackTrace();
            throw new LuckException(e.getCustomErrorMsg(), e);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            throw new LuckException(e.getErrMsg(), e);
        }
        return false;
    }

    public RefundInfoResultBO validateAndGetRefundInfo(PayType payType, String xmlData) throws  WxPayException {

        RefundInfoResultBO refundInfoResultBo = new RefundInfoResultBO();
        refundInfoResultBo.setRefundSuccess(false);

        if (isWxPay(payType)) {
            WxPayRefundNotifyResult wxPayRefundNotifyResult = wxConfig.getWxPayService(payType).parseRefundNotifyResult(xmlData);
            WxPayRefundNotifyResult.ReqInfo reqInfo = wxPayRefundNotifyResult.getReqInfo();
            refundInfoResultBo.setSuccessString(WX_SUCCESS_XML);
            refundInfoResultBo.setCallbackContent(xmlData);
//            refundInfoResultBo.setRefundId(Long.valueOf(reqInfo.getOutRefundNo()));
            refundInfoResultBo.setRefundNumber(reqInfo.getOutRefundNo());
            refundInfoResultBo.setBizRefundNo(reqInfo.getRefundId());
            refundInfoResultBo.setRefundSuccess(SUCCESS.equals(reqInfo.getRefundStatus()));
        }
        if (Objects.equals(payType, PayType.SQB_LITE_POS)) {
            boolean flag = SQBSignUtils.callbackSign(xmlData,sqbParams.getPublicKey());
            logger.info("收钱吧退款成功回调，请求参数验签结果：{}，请检查数据，返回报文：{}", flag ,xmlData);
            if ( flag ){
                JSONObject jsonObject =  JSONObject.parseObject(xmlData);
                ResultNoticeRequest resultNoticeRequest = jsonObject.getObject("request", ResultNoticeRequest.class);
                SQBBodyByResultNotice sqbBodyByResultNotice = resultNoticeRequest.getBody();

                //退款成功
                if (Objects.equals(SQBResultNoticeOrderStatus.SUCCESS.value(),Integer.valueOf(sqbBodyByResultNotice.getOrder_status()))){
                    refundInfoResultBo.setSuccessString(SQBSignUtils.callbackContent(sqbParams.getAppid(),sqbParams.getPrivateKey()));
                    refundInfoResultBo.setCallbackContent(xmlData);
                    // refundInfoResultBo.setRefundId(Long.valueOf(reqInfo.getOutRefundNo()));
                    refundInfoResultBo.setRefundNumber(sqbBodyByResultNotice.getCheck_sn());
                    // refundInfoResultBo.setBizRefundNo(dbPayInfo.getRefundId());
                    refundInfoResultBo.setRefundSuccess(Objects.equals(SQBResultNoticeOrderStatus.SUCCESS.value(),Integer.valueOf(sqbBodyByResultNotice.getOrder_status())));
                }
            }
        }
        return refundInfoResultBo;
    }

    /**
     * 获取支付宝POST过来反馈信息
     */
    private Map<String, String> parseAliNotifyAndGetResult(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {
        //乱码解决，这段代码在出现乱码时使用
        request.setCharacterEncoding("utf-8");
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>(16);
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCertCheckV1(params, feignShopConfig.getAlipay().getAlipayCertPath(), cn.hutool.core.util.CharsetUtil.UTF_8, AlipayConstants.SIGN_TYPE_RSA2);

        if (!signVerified) {
            // 验签失败
            throw new LuckException("验签失败");
        }
        return params;
    }


    public boolean isWxPay(PayType payType) {
        return (Objects.equals(payType, PayType.WECHATPAY)
                || Objects.equals(payType, PayType.WECHATPAY_SWEEP_CODE)
                || Objects.equals(payType, PayType.WECHATPAY_H5)
                || Objects.equals(payType, PayType.WECHATPAY_MP)
                || Objects.equals(payType, PayType.WECHATPAY_APP));
    }

    public boolean isAliPay(PayType payType) {
        return (Objects.equals(payType, PayType.ALIPAY_H5)
                || Objects.equals(payType, PayType.ALIPAY)
                || Objects.equals(payType, PayType.ALIPAY_APP));
    }
}
