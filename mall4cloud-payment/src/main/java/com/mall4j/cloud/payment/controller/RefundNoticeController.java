package com.mall4j.cloud.payment.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.mall4j.cloud.common.constant.PayType;
import com.mall4j.cloud.payment.bo.RefundInfoResultBO;
import com.mall4j.cloud.payment.manager.PayManager;
import com.mall4j.cloud.payment.model.RefundInfo;
import com.mall4j.cloud.payment.service.RefundInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;

/**
 * 退款成功通知，目前只有微信会有退款的回调
 * @author FrozenWatermelon
 */
@Slf4j
@ApiIgnore
@RestController
@RequestMapping("/ua/notice/refund")
public class RefundNoticeController {

    @Autowired
    private PayManager payManager;

    @Autowired
    private RefundInfoService refundInfoService;

    /**
     * 退款结果通知
     */
    @RequestMapping("/order/{payType}")
    public ResponseEntity<String> result(@RequestBody(required = false) String xmlData, @PathVariable Integer payType) throws WxPayException {
//        log.info("退款回调：payType：{}，xmlData:{}。",payType,xmlData);
        RefundInfoResultBO refundInfoResultBO = payManager.validateAndGetRefundInfo(PayType.instance(payType), xmlData);
        log.info("退款成功回调，解析结果:{}：payType：{}，xmlData:{}。", JSONObject.toJSONString(refundInfoResultBO),payType,xmlData);
        RefundInfo refundInfo = refundInfoService.getByRefundNumber(refundInfoResultBO.getRefundNumber());
//        RefundInfo refundInfo = refundInfoService.getByRefundId(refundInfoResultBO.getRefundId());
        if (!refundInfoResultBO.getRefundSuccess()) {
            if (StrUtil.isNotBlank(refundInfo.getCallbackContent())) {
                refundInfo.setCallbackContent(refundInfoResultBO.getCallbackContent());
                refundInfo.setCallbackTime(new Date());
                refundInfoService.update(refundInfo);
            }
            return ResponseEntity.ok(refundInfoResultBO.getSuccessString());
        }
        refundInfo.setCallbackContent(refundInfoResultBO.getCallbackContent());
        
        refundInfoService.refundSuccess(refundInfo);
        return ResponseEntity.ok(refundInfoResultBO.getSuccessString());
    }

}
