package com.mall4j.cloud.flow.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 流量分析—用户数据DTO
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public class UserAnalysisDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long userAnalysisId;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("用户Id（用户未登陆时，为uuid）")
    private String userId;

    @ApiModelProperty("省Id")
    private Long provinceId;

    @ApiModelProperty("加购数量")
    private Integer plusShopCart;

    @ApiModelProperty("下单金额")
    private Long placeOrderAmount;

    @ApiModelProperty("支付金额")
    private Long payAmount;

    @ApiModelProperty("浏览量")
    private Integer visitNums;

    @ApiModelProperty("0:旧用户, 1:新用户, 2.未登陆用户")
    private Integer userType;

    @ApiModelProperty("会话数")
    private Integer sessionNums;

    @ApiModelProperty("系统类型 (1:pc  2:h5  3:小程序 4:安卓  5:ios)")
    private Integer systemType;

	public Long getUserAnalysisId() {
		return userAnalysisId;
	}

	public void setUserAnalysisId(Long userAnalysisId) {
		this.userAnalysisId = userAnalysisId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getPlusShopCart() {
		return plusShopCart;
	}

	public void setPlusShopCart(Integer plusShopCart) {
		this.plusShopCart = plusShopCart;
	}

	public Long getPlaceOrderAmount() {
		return placeOrderAmount;
	}

	public void setPlaceOrderAmount(Long placeOrderAmount) {
		this.placeOrderAmount = placeOrderAmount;
	}

	public Long getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Long payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getVisitNums() {
		return visitNums;
	}

	public void setVisitNums(Integer visitNums) {
		this.visitNums = visitNums;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getSessionNums() {
		return sessionNums;
	}

	public void setSessionNums(Integer sessionNums) {
		this.sessionNums = sessionNums;
	}

	public Integer getSystemType() {
		return systemType;
	}

	public void setSystemType(Integer systemType) {
		this.systemType = systemType;
	}

	@Override
	public String toString() {
		return "UserAnalysisDTO{" +
				"userAnalysisId=" + userAnalysisId +
				",createDate=" + createDate +
				",userId=" + userId +
				",provinceId=" + provinceId +
				",plusShopCart=" + plusShopCart +
				",placeOrderAmount=" + placeOrderAmount +
				",payAmount=" + payAmount +
				",visitNums=" + visitNums +
				",userType=" + userType +
				",sessionNums=" + sessionNums +
				",systemType=" + systemType +
				'}';
	}
}
