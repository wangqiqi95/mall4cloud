package com.mall4j.cloud.api.openapi.ipuhuo.vo;

import com.mall4j.cloud.api.openapi.ipuhuo.dto.BaseResultDto;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class ShopInfoVO implements BaseResultDto,Serializable {

	private static final long serialVersionUID = -3328658288836706676L;
	@ApiModelProperty(value = "店铺id")
	private Long shopid;
	@ApiModelProperty(value = "店铺名称")
	private String shopname;
	@ApiModelProperty(value = "店铺logo,可选")
	private String logo;

	public ShopInfoVO() {
	}

	public ShopInfoVO(Long shopid, String shopname, String logo) {
		this.shopid = shopid;
		this.shopname = shopname;
		this.logo = logo;
	}

	public Long getShopid() {
		return shopid;
	}

	public void setShopid(Long shopid) {
		this.shopid = shopid;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	@Override
	public String toString() {
		return "ShopInfoVO{" + "shopid=" + shopid + ", shopname='" + shopname + '\'' + ", logo='" + logo + '\'' + '}';
	}
}
