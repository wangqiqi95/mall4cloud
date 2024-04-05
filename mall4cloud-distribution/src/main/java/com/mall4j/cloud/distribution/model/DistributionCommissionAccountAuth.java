package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 佣金管理-佣金账户-认证
 *
 * @author gww
 * @date 2022-01-31 12:15:41
 */
public class DistributionCommissionAccountAuth extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键ID")
    private Long id;

	@ApiModelProperty("身份类型 1-导购 2-微客")
    private Integer identityType;

	@ApiModelProperty("用户ID")
    private Long userId;

	@ApiModelProperty("认证状态 1-通过 2-驳回 3-待审核 4-会员信息不存在")
    private Integer authStatus;

	@ApiModelProperty("认证失败信息")
    private String authFailMsg;

	@ApiModelProperty("姓名")
    private String name;

	@ApiModelProperty("证件号码")
    private String certNo;

	@ApiModelProperty("电话号码")
    private String telephone;

	@ApiModelProperty("省")
    private String province;

	@ApiModelProperty("市")
    private String city;

	@ApiModelProperty("区")
    private String district;

	@ApiModelProperty("详细地址")
    private String address;

	@ApiModelProperty("身份证正面照")
    private String idPhotoFront;

	@ApiModelProperty("身份证背面照")
    private String idPhotoBack;

	@ApiModelProperty("收款账号")
    private String cardNo;

	@ApiModelProperty("银行卡姓名(此姓名必须和身份证上的姓名一至)")
    private String cardName;

	@ApiModelProperty("标志用户每次请求用于异步通知")
    private String requestId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getIdentityType() {
		return identityType;
	}

	public void setIdentityType(Integer identityType) {
		this.identityType = identityType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(Integer authStatus) {
		this.authStatus = authStatus;
	}

	public String getAuthFailMsg() {
		return authFailMsg;
	}

	public void setAuthFailMsg(String authFailMsg) {
		this.authFailMsg = authFailMsg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIdPhotoFront() {
		return idPhotoFront;
	}

	public void setIdPhotoFront(String idPhotoFront) {
		this.idPhotoFront = idPhotoFront;
	}

	public String getIdPhotoBack() {
		return idPhotoBack;
	}

	public void setIdPhotoBack(String idPhotoBack) {
		this.idPhotoBack = idPhotoBack;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardname) {
		this.cardName = cardname;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Override
	public String toString() {
		return "DistributionCommissionAccountAuth{" +
				"id=" + id +
				",identityType=" + identityType +
				",userId=" + userId +
				",authStatus=" + authStatus +
				",authFailMsg=" + authFailMsg +
				",name=" + name +
				",certNo=" + certNo +
				",telephone=" + telephone +
				",province=" + province +
				",city=" + city +
				",district=" + district +
				",address=" + address +
				",idPhotoFront=" + idPhotoFront +
				",idPhotoBack=" + idPhotoBack +
				",cardNo=" + cardNo +
				",cardName=" + cardName +
				",requestId=" + requestId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
