package com.mall4j.cloud.flow.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 流量分析—用户数据
 *
 * @author YXF
 * @date 2021-05-21 15:25:19
 */
public class UserAnalysis extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long userAnalysisId;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 用户Id（用户未登陆时，为uuid）
     */
    private String userId;

    /**
     * 省Id
     */
    private Long provinceId;

    /**
     * 加购数量
     */
    private Integer plusShopCart;

    /**
     * 下单金额
     */
    private Long placeOrderAmount;

    /**
     * 支付金额
     */
    private Long payAmount;

    /**
     * 浏览量
     */
    private Integer visitNums;

    /**
     * 点击量
     */
    private Integer clickNums;

    /**
     * 0:旧用户, 1:新用户, 2.未登陆用户
     */
    private Integer userType;

    /**
     * 系统类型 (1:pc  2:h5  3:小程序 4:安卓  5:ios)
     */
    private Integer systemType;

    /**
     * 商品id列表
     */
    private List<Long> spuIds;

	/**
	 * 用户浏览的商品信息
	 * @return
	 */
	List<UserVisitProdAnalysis> userVisitProdAnalyses;

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

	public Integer getSystemType() {
		return systemType;
	}

	public void setSystemType(Integer systemType) {
		this.systemType = systemType;
	}

	public List<Long> getSpuIds() {
		return spuIds;
	}

	public void setSpuIds(List<Long> spuIds) {
		this.spuIds = spuIds;
	}

	public List<UserVisitProdAnalysis> getUserVisitProdAnalyses() {
		return userVisitProdAnalyses;
	}

	public void setUserVisitProdAnalyses(List<UserVisitProdAnalysis> userVisitProdAnalyses) {
		this.userVisitProdAnalyses = userVisitProdAnalyses;
	}

	@Override
	public String toString() {
		return "UserAnalysis{" +
				"userAnalysisId=" + userAnalysisId +
				", createDate=" + createDate +
				", userId='" + userId + '\'' +
				", provinceId=" + provinceId +
				", plusShopCart=" + plusShopCart +
				", placeOrderAmount=" + placeOrderAmount +
				", payAmount=" + payAmount +
				", visitNums=" + visitNums +
				", clickNums=" + clickNums +
				", userType=" + userType +
				", systemType=" + systemType +
				", spuIds=" + spuIds +
				", userVisitProdAnalyses=" + userVisitProdAnalyses +
				'}';
	}
}
