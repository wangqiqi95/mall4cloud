package com.mall4j.cloud.api.openapi.ipuhuo.dto;

import java.io.Serializable;

public class AddGoodsTagReqDto extends CommonReqDto implements Serializable {

	private static final long serialVersionUID = 7815258421403326824L;
	/**
	 * 商品父级分组id；0代表第一级分组。
	 */
	private Long parentid;

	/**
	 * 商品分组的名称
	 */
	private String name;

	public Long getParentid() {
		return parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "AddGoodsTagReqDto{" + "parentid=" + parentid + ", name='" + name + '\'' + '}';
	}
}
