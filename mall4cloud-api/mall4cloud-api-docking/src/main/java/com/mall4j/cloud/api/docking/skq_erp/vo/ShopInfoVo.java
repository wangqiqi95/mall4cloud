package com.mall4j.cloud.api.docking.skq_erp.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 类描述：门店信息
 *
 * @date 2022/1/9 10:54：58
 */
@Data
public class ShopInfoVo implements Serializable {

	@ApiModelProperty(value = "经销商名称")
	private String customer;

	@ApiModelProperty(value = "店铺编码")
	private String eCode;

	@ApiModelProperty(value = "店铺名称")
	private String eName;

	@ApiModelProperty(value = "ERP系统代码")
	private String erpCode;

	@ApiModelProperty(value = "上级经销商")
	private String customerUp;

	@ApiModelProperty(value = "省份")
	private String cpProvince;

	@ApiModelProperty(value = "城市")
	private String cpCity;

	@ApiModelProperty(value = "区")
	private String cpDistrict;

	@ApiModelProperty(value = "店铺类型")
	private String shopType;

	@ApiModelProperty(value = "门店类型：自营/经销/代销/电商")
	private String nature;

	@ApiModelProperty(value = "创建时间")
	private String creationDate;

	@ApiModelProperty(value = "修改时间")
	private String modifiedDate;

	@ApiModelProperty(value = "启用")
	private String isActive;
	@ApiModelProperty(value = "管理大区")
	private String	departName;
	@ApiModelProperty(value = "管理小区")
	private String	blockName;

	private String STATE_NAME;
	private String address;

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	public String geteName() {
		return eName;
	}

	public void seteName(String eName) {
		this.eName = eName;
	}

	public String getErpCode() {
		return erpCode;
	}

	public void setErpCode(String erpCode) {
		this.erpCode = erpCode;
	}

	public String getCustomerUp() {
		return customerUp;
	}

	public void setCustomerUp(String customerUp) {
		this.customerUp = customerUp;
	}

	public String getCpProvince() {
		return cpProvince;
	}

	public void setCpProvince(String cpProvince) {
		this.cpProvince = cpProvince;
	}

	public String getCpCity() {
		return cpCity;
	}

	public void setCpCity(String cpCity) {
		this.cpCity = cpCity;
	}

	public String getCpDistrict() {
		return cpDistrict;
	}

	public void setCpDistrict(String cpDistrict) {
		this.cpDistrict = cpDistrict;
	}

	public String getShopType() {
		return shopType;
	}

	public void setShopType(String shopType) {
		this.shopType = shopType;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getSTATE_NAME() {
		return STATE_NAME;
	}

	public void setSTATE_NAME(String STATE_NAME) {
		this.STATE_NAME = STATE_NAME;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "ShopInfoVo{" + "customer='" + customer + '\'' + ", eCode='" + eCode + '\'' + ", eName='" + eName + '\'' + ", erpCode='" + erpCode + '\''
				+ ", customerUp='" + customerUp + '\'' + ", cpProvince='" + cpProvince + '\'' + ", cpCity='" + cpCity + '\'' + ", cpDistrict='" + cpDistrict
				+ '\'' + ", shopType='" + shopType + '\'' + ", creationDate='" + creationDate + '\'' + ", modifiedDate='" + modifiedDate + '\'' + ", isActive='"
				+ isActive + '\'' + '}';
	}
}
