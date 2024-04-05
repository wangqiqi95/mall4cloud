package com.mall4j.cloud.payment.controller.staff;


import com.alipay.api.AlipayApiException;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.common.util.BooleanUtil;
import com.mall4j.cloud.payment.bo.PayInfoBO;
import com.mall4j.cloud.payment.constant.PayEntry;
import com.mall4j.cloud.payment.dto.PayInfoDTO;
import com.mall4j.cloud.payment.dto.StaffPayInfoDTO;
import com.mall4j.cloud.payment.manager.PayManager;
import com.mall4j.cloud.payment.service.PayInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/s/pay")
@Api(tags = "代客下单支付接口")
@RefreshScope
public class StaffPayController {

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
    public ServerResponseEntity<?> pay(@Valid @RequestBody StaffPayInfoDTO payParam) throws AlipayApiException, WxPayException {
        PayInfoDTO payInfoDTO = new PayInfoDTO();
        BeanUtils.copyProperties(payParam, payInfoDTO);
        PayInfoBO payInfo = payInfoService.pay(payParam.getUserId(), payInfoDTO);
//        payInfo.setBizUserId(AuthUserContext.get().getBizUserId());
        payInfo.setPayType(payParam.getPayType());
        // 回调地址
        payInfo.setApiNoticeUrl(domainUrl + "/ua/notice/pay/" + PayEntry.ORDER.value() + "/" + payParam.getPayType());
        payInfo.setReturnUrl(payParam.getReturnUrl());

        return payManager.doPay(payInfo);
    }

    @GetMapping("/is_pay/{payEntry}/{orderIds}/{userId}")
    @ApiOperation(value = "根据订单号查询该订单是否已经支付", notes = "根据订单号查询该订单是否已经支付")
    public ResponseEntity<Boolean> isPay(@PathVariable Integer payEntry, @PathVariable String orderIds, @PathVariable Long userId) {
        Integer isPay = payInfoService.isPay(orderIds, userId, payEntry);
        return ResponseEntity.ok(BooleanUtil.isTrue(isPay));
    }


}
