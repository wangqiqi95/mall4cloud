package com.mall4j.cloud.flow.constant;


/**
 * 页面编号（编号的顺序对数据没有影响）
 *
 * @author YXF
 * @date 2021-05-22
 */
public enum SystemTypeEnum {
	/** PC*/
	PC(1),
	/** H4 */
	H5(2),
	/** 小程序 */
	APPLETS(3),
	/** 安卓 */
	ANDROID(4),
	/** IOS */
	IOS(5);

	private Integer id;

	public Integer value() {
		return id;
	}

	SystemTypeEnum(Integer id){
		this.id = id;
	}

	public static SystemTypeEnum instance(Integer value) {
		SystemTypeEnum[] enums = values();
		for (SystemTypeEnum statusEnum : enums) {
			if (statusEnum.value().equals(value)) {
				return statusEnum;
			}
		}
		return H5;
	}

}
