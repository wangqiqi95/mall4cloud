package com.mall4j.cloud.api.openapi.ipuhuo.dto;

import java.io.Serializable;

public class AddGoodsTagRespDto implements Serializable, BaseResultDto {

	private static final long serialVersionUID = 1631548819864524465L;
	private Long tagid;

	public AddGoodsTagRespDto() {
	}

	public AddGoodsTagRespDto(Long tagid) {
		this.tagid = tagid;
	}

	public Long getTagid() {
		return tagid;
	}

	public void setTagid(Long tagid) {
		this.tagid = tagid;
	}

	@Override
	public String toString() {
		return "AddGoodsTagRespDto{" + "tagid=" + tagid + '}';
	}
}
