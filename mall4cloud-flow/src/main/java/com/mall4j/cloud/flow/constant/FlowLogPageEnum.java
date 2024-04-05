package com.mall4j.cloud.flow.constant;

/**
 * 页面编号（编号的顺序对数据没有影响）
 * @author
 */
public enum FlowLogPageEnum {
	/** 首页*/
	HOME(1, "首页"),
	/** 商品详情（普通） */
	PROD_INFO(3, "商品详情页"),
	/** 分类 */
	CATEGORY(6, "分类页"),
	/** 满减  */
	DISCOUNT(7, "满减页"),
	/** 购物车 */
	SHOP_CAT(8, "购物车页"),
	/** 订单详情 */
	PLACE_ORDER(9, "订单详情页"),
	/** 支付页面 */
	PAY(10, "支付页面页"),
	/** 个人中心 */
	USER_CENTER(11, "个人中心首页"),
	/** 订单列表 */
	ORDER_LIST(12, "订单列表页");

	private Integer id;
	private String name;

	public Integer value() {
		return id;
	}
	public static String name(Integer id) {
		FlowLogPageEnum[] enums = values();
		for (FlowLogPageEnum statusEnum : enums) {
			if (statusEnum.value().equals(id)) {
				return statusEnum.name;
			}
		}
		return null;
	}

	FlowLogPageEnum(Integer id, String name){
		this.id = id;
		this.name = name;
	}

	public static FlowLogPageEnum instance(String value) {
		FlowLogPageEnum[] enums = values();
		for (FlowLogPageEnum statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}

	public static FlowLogPageEnum[] allEnum() {
		FlowLogPageEnum[] enums = values();
		return enums;
	}

//	/**
//	 * 判断是否是商品页面
//	 * @param id
//	 * @return
//	 */
//	public static Boolean isProductPage(Integer id) {
//		if (id.equals(PROD_INFO.value())){
//			return true;
//		}
//		return false;
//	}
//
//	/**
//	 * 判断是否是支付页面
//	 * @param id
//	 * @return
//	 */
//	public static Boolean isPayPage(Integer id) {
//		if (id.equals(PAY.value())){
//			return true;
//		}
//		return false;
//	}
//
//	/**
//	 * 判断是否是商品或支付页面
//	 * @param id
//	 * @return
//	 */
//	public static Boolean isProductOrPayPage(Integer id) {
//		if (id.equals(PROD_INFO.value()) || id.equals(PAY.value())){
//			return true;
//		}
//		return false;
//	}
}
