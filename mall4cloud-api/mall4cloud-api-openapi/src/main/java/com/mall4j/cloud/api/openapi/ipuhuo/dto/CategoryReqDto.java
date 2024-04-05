package com.mall4j.cloud.api.openapi.ipuhuo.dto;

import java.io.Serializable;

public class CategoryReqDto extends CommonReqDto implements Serializable {

	private static final long serialVersionUID = 2791947323972337285L;
	private String parentcid;

	public String getParentcid() {
		return parentcid;
	}

	public void setParentcid(String parentcid) {
		this.parentcid = parentcid;
	}

	@Override
	public String toString() {
		return "CategoryReqDto{" + "parentcid=" + parentcid + '}';
	}
}
