package com.mall4j.cloud.api.docking.skq_erp.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 类描述：员工信息
 *
 * @date 2022/1/9 11:24：15
 */
public class EmployeeInfoVo {


	@ApiModelProperty(value = "员工编码")
	private String eCode;

	@ApiModelProperty(value = "员工姓名")
	private String eName;

	@ApiModelProperty(value = "营业员工号")
	private String eNumNo;

	@ApiModelProperty(value = "性别")
	private String sex;

	@ApiModelProperty(value = "入职日期")
	private String hireDate;

	@ApiModelProperty(value = "是否离职")
	private String isLeave;

	@ApiModelProperty(value = "离职日期")
	private String leaveDate;

	@ApiModelProperty(value = "手机号码")
	private String mobile;

	@ApiModelProperty(value = "店铺编码")
	private String shopCode;

	@ApiModelProperty(value = "店铺类型")
	private String shopType;

	@ApiModelProperty(value = "openId")
	private String openId;

	@ApiModelProperty(value = "职位")
	private String appPosition;

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

	public String geteNumNo() {
		return eNumNo;
	}

	public void seteNumNo(String eNumNo) {
		this.eNumNo = eNumNo;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getHireDate() {
		return hireDate;
	}

	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}

	public String getIsLeave() {
		return isLeave;
	}

	public void setIsLeave(String isLeave) {
		this.isLeave = isLeave;
	}

	public String getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getShopType() {
		return shopType;
	}

	public void setShopType(String shopType) {
		this.shopType = shopType;
	}

	public String getAppPosition() {
		return appPosition;
	}

	public void setAppPosition(String appPosition) {
		this.appPosition = appPosition;
	}

	@Override
	public String toString() {
		return "EmployeeInfoVo{" + "eCode='" + eCode + '\'' + ", eName='" + eName + '\'' + ", eNumNo='" + eNumNo + '\'' + ", sex='" + sex + '\''
				+ ", hireDate='" + hireDate + '\'' + ", isLeave='" + isLeave + '\'' + ", leaveDate='" + leaveDate + '\'' + ", mobile='" + mobile + '\''
				+ ", shopCode='" + shopCode + '\'' + ", shopType='" + shopType + '\'' + ", openId='" + openId + '\'' + ", appPosition='" + appPosition + '\''
				+ '}';
	}
}
