package com.mall4j.cloud.api.flow.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 流量分析—用户数据VO
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public class UserAnalysisVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long userAnalysisId;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("用户Id（用户未登陆时，为uuid）")
    private String userId;

    @ApiModelProperty("省Id")
    private Long provinceId;

    @ApiModelProperty("省名称")
    private String provinceName;

    @ApiModelProperty("加购数量")
    private Integer plusShopCart;

    @ApiModelProperty("下单金额")
    private Long placeOrderAmount;

    @ApiModelProperty("支付金额")
    private Long payAmount;

    @ApiModelProperty("浏览量")
    private Integer visitNums;

	@ApiModelProperty("点击量")
	private Integer clickNums;

    @ApiModelProperty("0:旧用户, 1:新用户, 2.未登陆用户")
    private Integer userType;

    @ApiModelProperty("会话数")
    private Integer sessionNums;

    @ApiModelProperty("系统类型 (1:pc  2:h5  3:小程序 4:安卓  5:ios)")
    private Integer systemType;

    @ApiModelProperty("商品访客数")
	private Integer prodVisitUser;

    @ApiModelProperty("商品浏览量")
	private Integer prodVisitNums;

    @ApiModelProperty("商品id列表")
	private List<Long> spuIdList;

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

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
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

	public Integer getClickNums() {
		return clickNums;
	}

	public void setClickNums(Integer clickNums) {
		this.clickNums = clickNums;
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

	public Integer getProdVisitUser() {
		return prodVisitUser;
	}

	public void setProdVisitUser(Integer prodVisitUser) {
		this.prodVisitUser = prodVisitUser;
	}

	public Integer getProdVisitNums() {
		return prodVisitNums;
	}

	public void setProdVisitNums(Integer prodVisitNums) {
		this.prodVisitNums = prodVisitNums;
	}

	public List<Long> getSpuIdList() {
		return spuIdList;
	}

	public void setSpuIdList(List<Long> spuIdList) {
		this.spuIdList = spuIdList;
	}

	@Override
	public String toString() {
		return "UserAnalysisVO{" +
				"userAnalysisId=" + userAnalysisId +
				", createDate=" + createDate +
				", userId='" + userId + '\'' +
				", provinceId=" + provinceId +
				", provinceName='" + provinceName + '\'' +
				", plusShopCart=" + plusShopCart +
				", placeOrderAmount=" + placeOrderAmount +
				", payAmount=" + payAmount +
				", visitNums=" + visitNums +
				", clickNums=" + clickNums +
				", userType=" + userType +
				", sessionNums=" + sessionNums +
				", systemType=" + systemType +
				", prodVisitUser=" + prodVisitUser +
				", prodVisitNums=" + prodVisitNums +
				", spuIdList=" + spuIdList +
				'}';
	}
}
