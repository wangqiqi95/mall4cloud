package com.mall4j.cloud.payment.controller;

import com.alipay.api.AlipayApiException;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.mall4j.cloud.common.constant.PayType;
import com.mall4j.cloud.common.response.ResponseEnum;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.payment.bo.PayInfoResultBO;
import com.mall4j.cloud.payment.constant.PayEntry;
import com.mall4j.cloud.payment.constant.PayStatus;
import com.mall4j.cloud.payment.manager.PayManager;
import com.mall4j.cloud.payment.manager.PayNoticeManager;
import com.mall4j.cloud.payment.model.PayInfo;
import com.mall4j.cloud.payment.service.PayInfoService;
import lombok.extern.slf4j.Slf4j;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

/**
 * @author FrozenWatermelon
 */
@Slf4j
@ApiIgnore
@Controller
@RequestMapping("/ua/notice/pay")
public class PayNoticeController {

    @Autowired
    private PayInfoService payInfoService;

    @Autowired
    private PayManager payManager;


    @Autowired
    private PayNoticeManager payNoticeManager;
    /**
     * 支付异步回调
     */
    @RequestMapping("/{payEntry}/{payType}")
    public ResponseEntity<String> notice(HttpServletRequest request,
                                         @PathVariable("payType") Integer payType,
                                         @PathVariable("payEntry") Integer payEntry,
                                         @RequestBody(required = false) String xmlData) throws WxPayException, UnsupportedEncodingException, AlipayApiException {
        log.info("支付成功回调，参数信息：payType:{},payEntry:{},xmlData:{}",payType,payEntry,xmlData);
        PayInfoResultBO payInfoResultBO = payManager.validateAndGetPayInfo(request, PayType.instance(payType), xmlData);

        // 校验订单参数异常，返回未授权
        if (!payInfoResultBO.getPaySuccess()) {
            return ResponseEntity.ok(payInfoResultBO.getSuccessString());
        }

        PayInfo payInfo = payInfoService.getByPayId(payInfoResultBO.getPayId());
        // 已经支付
        if (Objects.equals(payInfo.getPayStatus(), PayStatus.PAYED.value()) || Objects.equals(payInfo.getPayStatus(), PayStatus.REFUND.value())) {
            return ResponseEntity.ok(payInfoResultBO.getSuccessString());
        }
        // 支付金额不对
        if (!Objects.equals(payInfo.getPayAmount(), payInfoResultBO.getPayAmount())) {
            return ResponseEntity.ok(ResponseEnum.UNAUTHORIZED.value());
        }
        if (Objects.equals(payEntry, PayEntry.ORDER.value())) {
            // 订单支付回调
            return payNoticeManager.noticeOrder(payInfoResultBO, payInfo);
        } else if (Objects.equals(payEntry, PayEntry.RECHARGE.value())) {
            // 余额充值回调
            return payNoticeManager.noticeRecharge(payInfoResultBO, payInfo);
        } else if (Objects.equals(payEntry, PayEntry.VIP.value())) {
            // 购买会员回调
            return payNoticeManager.noticeBuyVip(payInfoResultBO, payInfo);
        } else if (Objects.equals(payEntry, PayEntry.COUPON_PACK.value())) {
            // 购买会员回调
            return payNoticeManager.noticeCouponPack(payInfoResultBO, payInfo);
        }
        return ResponseEntity.ok(ResponseEnum.UNAUTHORIZED.value());
    }

//    /**
//     * 支付异步回调
//     * 原来的方式返回微信成功后，微信仍然不断调用这个接口。更换方式不通过springmvc 直接response.write返回支付成功。
//     */
//    @RequestMapping("/{payEntry}/{payType}")
//    public void notice(HttpServletRequest request,HttpServletResponse response,
//                                      @PathVariable("payType") Integer payType,
//                                      @PathVariable("payEntry") Integer payEntry,
//                                      @RequestBody(required = false) String xmlData) throws WxPayException, IOException, AlipayApiException {
//        log.info("支付成功回调，参数信息：payType:{},payEntry:{},xmlData:{}",payType,payEntry,xmlData);
////        HttpServletResponse response;
//        String successData = "<xml><return_code><![CDATA[SUCCESS]]</return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
//        String errorData = "<xml><return_code><![CDATA[ERROR]]</return_code><return_msg><![CDATA[ERROR]]></return_msg></xml>";
//
//        PayInfoResultBO payInfoResultBO = payManager.validateAndGetPayInfo(request, PayType.instance(payType), xmlData);
//
//
//        // 校验订单参数异常，返回未授权
//        if (!payInfoResultBO.getPaySuccess()) {
////            return ResponseEntity.ok(payInfoResultBO.getSuccessString());
//            log.error("支付成功回调，校验不通过,xmlData:{}",xmlData);
//            response.getWriter().write(errorData);
//            return;
//        }
//
//        PayInfo payInfo = payInfoService.getByPayId(payInfoResultBO.getPayId());
//        // 已经支付
//        if (Objects.equals(payInfo.getPayStatus(), PayStatus.PAYED.value()) || Objects.equals(payInfo.getPayStatus(), PayStatus.REFUND.value())) {
//            response.getWriter().write(successData);
//            return;
//        }
//        // 支付金额不对
//        if (!Objects.equals(payInfo.getPayAmount(), payInfoResultBO.getPayAmount())) {
//            log.error("支付成功回调，金额验证不通过,xmlData:{}",xmlData);
//            response.getWriter().write(errorData);
//            return;
//        }
//        if (Objects.equals(payEntry, PayEntry.ORDER.value())) {
//            // 订单支付回调
//            payNoticeManager.noticeOrder(payInfoResultBO, payInfo);
//            response.getWriter().write(successData);
//            return;
//        }
//    }


//    @RequestMapping("/test")
//    @ResponseBody
//    public ResponseEntity<String> test(HttpServletRequest request,HttpServletResponse response) throws WxPayException, IOException, AlipayApiException {
//        System.out.println("123123123");
//        System.out.println("123123123");
//        System.out.println("123123123");
//        System.out.println("123123123");
//        System.out.println("123123123");
//        System.out.println("123123123");
//        return ResponseEntity.ok("<xml><return_code><![CDATA[SUCCESS]]</return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
//    }

//    @RequestMapping("/test1")
//    public ResponseEntity<String> test1(HttpServletRequest request,HttpServletResponse response) throws WxPayException, IOException, AlipayApiException {
//        System.out.println("123123123");
//        System.out.println("123123123");
//        System.out.println("123123123");
//        System.out.println("123123123");
//        System.out.println("123123123");
//        System.out.println("123123123");
//        return ResponseEntity.ok("1");
//    }

//    @RequestMapping("/test2")
//    public ResponseEntity<String> test2(HttpServletRequest request,HttpServletResponse response) throws WxPayException, IOException, AlipayApiException {
//        System.out.println("123123123");
//        System.out.println("123123123");
//        System.out.println("123123123");
//        System.out.println("123123123");
//        System.out.println("123123123");
//        System.out.println("123123123");
//        return ResponseEntity.ok("");
//    }

//    @RequestMapping("/test3")
//    @ResponseBody
//    public void test3(HttpServletRequest request,HttpServletResponse response) throws WxPayException, IOException, AlipayApiException {
//        response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]</return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
//    }

}
