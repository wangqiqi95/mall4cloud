package com.mall4j.cloud.payment.controller;


import com.alipay.api.AlipayApiException;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.BooleanUtil;
import com.mall4j.cloud.payment.bo.PayInfoBO;
import com.mall4j.cloud.payment.constant.PayEntry;
import com.mall4j.cloud.payment.dto.PayInfoDTO;
import com.mall4j.cloud.payment.manager.PayManager;
import com.mall4j.cloud.payment.service.PayInfoService;
import com.mall4j.cloud.payment.vo.OrderPayInfoVO;
import com.mall4j.cloud.payment.vo.PayInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author FrozenWatermelon
 */
@RestController
@RequestMapping("/pay")
@Api(tags = "订单接口")
@RefreshScope
public class PayController {

    @Autowired
    private PayInfoService payInfoService;

    @Autowired
    private PayManager payManager;

    @Value("${application.domainUrl}")
    private String domainUrl;
    

    /**
     * 支付接口
     */
    @PostMapping("/order")
    @ApiOperation(value = "根据订单号进行支付", notes = "根据订单号进行支付")
    public ServerResponseEntity<?> pay(@Valid @RequestBody PayInfoDTO payParam) throws AlipayApiException, WxPayException {

        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        Long userId = userInfoInTokenBO.getUserId();

        PayInfoBO payInfo = payInfoService.pay(userId, payParam);
        payInfo.setBizUserId(userInfoInTokenBO.getBizUserId());
        payInfo.setPayType(payParam.getPayType());
        // 回调地址
        payInfo.setApiNoticeUrl(domainUrl + "/ua/notice/pay/"+ PayEntry.ORDER.value() +"/" + payParam.getPayType());
        payInfo.setReturnUrl(payParam.getReturnUrl());

        return payManager.doPay(payInfo);
    }
    
    /**
     * 获取支付方式
     */
    @GetMapping("/orderPayType/{orderId}")
    @ApiOperation(value = "根据订单号获取支付方式", notes = "根据订单号获取支付方式")
    public ServerResponseEntity<OrderPayInfoVO> queryOrderPayType(@PathVariable Long orderId) {
        return payInfoService.queryOrderPayType(orderId);
    }
    
    @GetMapping("/queryPayInfo/{orderId}")
    @ApiOperation(value = "根据订单号查询该订单的支付信息", notes = "根据订单号查询该订单的支付信息")
    public ServerResponseEntity<PayInfoVO> queryPayInfo(@PathVariable Long orderId) {
        return ServerResponseEntity.success(payInfoService.queryPayInfo(orderId));
    }

    @GetMapping("/is_pay/{payEntry}/{orderIds}")
    @ApiOperation(value = "根据订单号查询该订单是否已经支付", notes = "根据订单号查询该订单是否已经支付")
    public ResponseEntity<Boolean> isPay(@PathVariable Integer payEntry, @PathVariable String orderIds) {
        Long userId = AuthUserContext.get().getUserId();

        Integer isPay = payInfoService.isPay(orderIds, userId,payEntry);
        return ResponseEntity.ok(BooleanUtil.isTrue(isPay));
    }


}
