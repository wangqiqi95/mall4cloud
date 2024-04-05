package com.mall4j.cloud.api.docking.jos.enums;

/**
 * @description: 证件类型
 * @date 2021/12/23 22:44
 */
public enum CertType {
	 RESIDENT_IDENTITY_CARD("201","居民身份证"),
	  MILITARY_OFFICER("202", "军官证"),
	  ARMED_POLICE_OFFICER("203", "武警警官证"),
	  SOLDIERS("204", "士兵证"),
	  FOREIGN_PASSPORT("208", "外国护照"),
	  HK_AND_MACAO_HOMECOMING_PERMIT("209", "港澳同胞回乡证"),
	  TAIWANESE_SYNDROME("211","台胞证"),
	  MAINLAND_TRAVEL_PERMIT_FOR_TAIWAN_RESIDENTS("213","台湾居民来往大陆通行证"),
	  PASS_FOR_MAINLAND_RESIDENTS_TO_AND_FROM_TAIWAN("214","大陆居民往来台湾通行证"),
	  MAINLAND_TRAVEL_PERMIT_FOR_HONG_KONG_AND_MACAO_RESIDENTS("210","港澳居民来往内地通行证"),
	  RPF_HK_AND_MACAO_R_CHINA("237","中华人民共和国港澳居民居住证")
		;

	/**
	 * 证件类型编码
	 */
	private String certType;

	/**
	 * 描述
	 */
	private String desc;

	CertType(String certType, String desc) {
		this.certType = certType;
		this.desc = desc;
	}

	public String certType() {
		return certType;
	}

	public String desc() {
		return desc;
	}


}
