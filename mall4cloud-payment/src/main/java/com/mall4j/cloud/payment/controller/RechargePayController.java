package com.mall4j.cloud.payment.controller;


import com.alipay.api.AlipayApiException;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.constant.PayType;
import com.mall4j.cloud.common.exception.LuckException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.BooleanUtil;
import com.mall4j.cloud.payment.bo.PayInfoBO;
import com.mall4j.cloud.payment.constant.PayEntry;
import com.mall4j.cloud.payment.dto.BuyVipPayInfoDTO;
import com.mall4j.cloud.payment.dto.PayInfoDTO;
import com.mall4j.cloud.payment.dto.RechargePayInfoDTO;
import com.mall4j.cloud.payment.manager.PayManager;
import com.mall4j.cloud.payment.service.PayInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 */
@RestController
@RequestMapping("/pay")
@Api(tags = "余额充值接口")
@RefreshScope
public class RechargePayController {

    @Autowired
    private PayInfoService payInfoService;

    @Autowired
    private PayManager payManager;

    @Value("${application.domainUrl}")
    private String domainUrl;


    /**
     * 支付接口
     */
    @PostMapping("/recharge")
    @ApiOperation(value = "余额充值接口", notes = "进行余额充值")
    public ServerResponseEntity<?> pay(@Valid @RequestBody RechargePayInfoDTO payParam) throws AlipayApiException, WxPayException {
        if (Objects.equals(payParam.getPayType(), PayType.BALANCE.value()) || Objects.equals(payParam.getPayType(), PayType.SCOREPAY.value())) {
            throw new LuckException("不支持该充值方式");
        }
        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        Long userId = userInfoInTokenBO.getUserId();

        PayInfoBO payInfo = payInfoService.recharge(userId, payParam);

        payInfo.setBizUserId(userInfoInTokenBO.getBizUserId());
        payInfo.setPayType(payParam.getPayType());
        // 回调地址
        payInfo.setApiNoticeUrl(domainUrl + "/ua/notice/pay/"+ PayEntry.RECHARGE.value() +"/" + payParam.getPayType());
        payInfo.setReturnUrl(payParam.getReturnUrl());


        return payManager.doPay(payInfo);
    }

}
