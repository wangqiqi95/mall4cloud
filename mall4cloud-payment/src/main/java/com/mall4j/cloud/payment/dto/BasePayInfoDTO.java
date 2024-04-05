package com.mall4j.cloud.payment.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 基本的支付信息
 * @author FrozenWatermelon
 * @date 2021/5/13
 */
public class BasePayInfoDTO {

    @NotNull(message = "支付方式不能为空")
    @ApiModelProperty(value = "支付方式 (0积分支付 1:微信小程序支付 2:支付宝 3微信扫码支付 4 微信h5支付 5微信公众号支付 6支付宝H5支付 7支付宝APP支付 8微信APP支付 9余额支付 10收钱吧轻POS支付)", required = true)
    private Integer payType;

    @ApiModelProperty(value = "支付完成回跳地址", required = true)
    private String returnUrl;

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    @Override
    public String toString() {
        return "BasePayInfoDTO{" +
                "payType=" + payType +
                ", returnUrl='" + returnUrl + '\'' +
                '}';
    }
}
