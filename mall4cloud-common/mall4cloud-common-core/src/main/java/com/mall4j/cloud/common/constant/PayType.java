package com.mall4j.cloud.common.constant;

import java.util.Objects;

/**
 * @author FrozenWatermelon
 */
public enum PayType {

	/** 积分支付*/
	SCOREPAY(0,"积分支付"),

	/** 微信小程序支付*/
	WECHATPAY(1,"微信小程序支付"),

	/** 支付宝*/
	ALIPAY(2,"支付宝"),

	/** 微信扫码支付*/
	WECHATPAY_SWEEP_CODE(3,"微信扫码支付"),

	/** 微信H5支付*/
	WECHATPAY_H5(4,"微信H5支付"),

	/** 微信公众号*/
	WECHATPAY_MP(5,"微信公众号支付"),

	/** 支付宝H5支付*/
	ALIPAY_H5(6,"支付宝H5支付"),

	/** 支付宝APP支付*/
	ALIPAY_APP(7,"支付宝APP支付"),

	/** 微信APP支付*/
	WECHATPAY_APP(8,"微信APP支付"),

	/** 余额支付*/
	BALANCE(9,"余额支付"),
	
	/** 收钱吧轻POS支付*/
	SQB_LITE_POS(10,"收钱吧轻POS支付")
	;



	private final Integer num;

	private final String payTypeName;

	public Integer value() {
		return num;
	}

	public String getPayTypeName() {
		return payTypeName;
	}

	public static Integer[] weChatPayArray = new Integer[]{WECHATPAY.value(), WECHATPAY_APP.value(), WECHATPAY_H5.value(), WECHATPAY_MP.value(), WECHATPAY_SWEEP_CODE.value()};
	public static Integer[] aliPayArray = new Integer[]{ALIPAY.value(), ALIPAY_H5.value(), ALIPAY_APP.value()};

	PayType(Integer num, String payTypeName){
		this.num = num;
		this.payTypeName = payTypeName;
	}

	public static PayType instance(Integer value) {
		PayType[] enums = values();
		for (PayType statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}

	public static String getPayTypeName(Integer value) {
		PayType[] enums = values();
		for (PayType statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum.payTypeName;
			}
		}
		return null;
	}

	public static Boolean isWeChatPay(Integer value) {
		return Objects.equals(value, WECHATPAY.value()) || Objects.equals(value, WECHATPAY_APP.value()) ||
				Objects.equals(value, WECHATPAY_H5.value()) || Objects.equals(value, WECHATPAY_MP.value()) ||
				Objects.equals(value, WECHATPAY_SWEEP_CODE.value());
	}

	public static Boolean isAliPay(Integer value) {
		return Objects.equals(value, ALIPAY.value()) || Objects.equals(value, ALIPAY_H5.value()) ||
				Objects.equals(value, ALIPAY_APP.value());
	}
}
