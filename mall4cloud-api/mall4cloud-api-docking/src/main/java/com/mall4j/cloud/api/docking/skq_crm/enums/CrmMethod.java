package com.mall4j.cloud.api.docking.skq_crm.enums;


public enum CrmMethod {
	/**
	 * 会员基本信息查询
	 */
	CUSTOMER_GET("/applets/customer/get", "会员基本信息查询"),

	/**
	 * 会员基本信息查询
	 */
	SCORE_EXPIRED_GET("/applets/customer/annualOverduePoint", "会员年度过期积分查询接口"),

	/**
	 * 会员注册验证接口
	 */
	CUSTOMER_CHECK("/applets/customer/check", "会员注册验证接口"),

	/**
	 * 会员新增接口
	 */
	CUSTOMER_ADD("/applets/customer/add", "会员新增接口"),

	/**
	 * 会员修改接口
	 */
	CUSTOMER_UPDATE("/applets/customer/update", "会员修改接口"),

	/**
	 * 会员积分明细查询接口
	 */
	CUSTOMER_POINT_DETAIL_GET("/applets/customer/pointDetailGet", "会员积分明细查询接口"),

	/**
	 * 会员积分增减接口
	 */
	CUSTOMER_POINT_CHANGE("/applets/customer/pointChange", "会员积分增减接口"),

	/**
	 * 积分兑换数据同步接口
	 */
	SYNC_CONVERT_DATA("/applets/pointRewords/log","积分兑换数据同步接口"),

	/**
	 * 优惠券查询接口
	 */
	COUPON_GET("/applets/coupon/get", "优惠券查询接口"),

	/**
	 * 微信优惠券项目Id同步至CRM接口
	 */
	COUPON_PROJECTSYSN("/applets/coupon/projectSysn", "微信优惠券项目Id同步至CRM接口"),

	/**
	 * 优惠券发放（小程序—>CRM）
	 */
	COUPON_RELEASE("/applets/coupon/release", "优惠券发放（小程序—>CRM）"),

	/**
	 * 优惠券验证接口
	 */
	COUPON_CHECK("/applets/coupon/check", "优惠券验证接口"),

	/**
	 * 优惠券核销接口
	 */
	COUPON_WRITE_OFF("/applets/coupon/writeOff", "优惠券核销接口"),

	/**
	 * 会员关注、取关接口
	 */
	CUSTOMER_ATTENTIONCANCEL("/applets/customer/attentionCancel", "会员关注、取关接口"),

	/**
	 * 获取淘宝外域入会验证码
	 */
	CUSTOMER_TMALL_SMS_CODE("/tmall/member/sms/code/send", "获取淘宝外域入会验证码"),

	/**
	 * 验证淘宝外域入会验证码
	 */
	CUSTOMER_TMALL_SMS_CHECK("/tmall/member/sms/code/check", "验证淘宝外域入会验证码"),

	/**
	 * 订单查询
	 */
	ORDER_SELECT("/applets/order/member", "订单查询"),

	/**
	 * 优惠券冻结解冻
	 */
	COUPON_FREEZETHAW("/applets/coupon/freezeThaw", "优惠券冻结解冻"),

	/**
	 * 小程序优惠券信息创建（小程序券）
	 */
	COUPON_PUSH("/applets/coupon/freezeThaw", "小程序优惠券信息创建（小程序券）"),

	/**
	 * 小程序优惠券信息修改（小程序券）
	 */
	COUPON_UPDATE_PUSH("/applets/coupon/project", "小程序优惠券信息修改（小程序券）"),

	/**
	 * 优惠券状态变更  发放/过期/核销
	 */
	COUPON_STATE_UPDATE("/applets/coupon/instance", "优惠券状态变更（发放/过期/核销）"),
	
	/**
	 * 单个优惠券码查询
	 */
	COUPON_SINGLE_GET("/applets/coupon/singleGet","单个优惠券码查询");

	private String uri;

	private String methodDesc;

	CrmMethod(String uri, String methodDesc) {
		this.uri = uri;
		this.methodDesc = methodDesc;
	}

	public String uri() {
		return uri;
	}

	public String methodDesc() {
		return methodDesc;
	}
}
