package com.mall4j.cloud.flow.constant;


/**
 * 页面操作
 * @author yxf
 */
public enum FlowVisitEnum {
	/** 访问页面*/
	VISIT(1),
	/** 分享页面 */
	SHARE(2),
	/** 点击页面 */
	CLICK(3),
	/** 加购 */
	SHOP_CAT(4);
	private Integer id;

	public Integer value() {
		return id;
	}

	FlowVisitEnum(Integer id){
		this.id = id;
	}

	public static FlowVisitEnum instance(String value) {
		FlowVisitEnum[] enums = values();
		for (FlowVisitEnum statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return null;
	}

	public static FlowVisitEnum[] allEnum() {
		FlowVisitEnum[] enums = values();
		return enums;
	}


	public static Boolean isVisitOrShare(Integer id){
		if (id.equals(VISIT.value()) || id.equals(SHARE.value())){
			return true;
		}
		return false;
	}
}
