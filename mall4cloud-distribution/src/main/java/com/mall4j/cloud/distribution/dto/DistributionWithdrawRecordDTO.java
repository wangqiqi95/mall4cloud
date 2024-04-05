package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 佣金管理-佣金提现记录DTO
 *
 * @author ZengFanChang
 * @date 2021-12-05 20:15:06
 */
public class DistributionWithdrawRecordDTO{

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("身份类型 1导购 2微客")
    private Integer identityType;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("姓名")
    private String username;

    @ApiModelProperty("工号/卡号")
    private String userNumber;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("提现单号")
    private String withdrawOrderNo;

	@ApiModelProperty("是否历史订单 0否 1是")
	private Integer historyOrder;

    @ApiModelProperty("提现金额")
    private Long withdrawAmount;

    @ApiModelProperty("转账方式 1默认三方转账")
    private Integer transferType;

    @ApiModelProperty("申请时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;

	@ApiModelProperty("申请开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyStartTime;

	@ApiModelProperty("申请结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyEndTime;

    @ApiModelProperty("处理时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date processTime;

	@ApiModelProperty("处理开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date processStartTime;

	@ApiModelProperty("处理结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date processEndTime;

	@ApiModelProperty("提现状态 0待处理 1提现中 2提现成功 3提现失败 4拒绝提现")
    private Integer withdrawStatus;

    @ApiModelProperty("处理人ID")
    private Long reviewerId;

    @ApiModelProperty("处理人姓名")
    private String reviewerName;

    @ApiModelProperty("申请提现订单号集合")
    private List<Long> orderIdList;

    @ApiModelProperty("处理类型 1通过 2拒绝")
    private Integer processType;

    @ApiModelProperty("拒绝原因")
    private String rejectReason;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserNumber() {
		return userNumber;
	}

	public void setUserNumber(String userNumber) {
		this.userNumber = userNumber;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getWithdrawOrderNo() {
		return withdrawOrderNo;
	}

	public void setWithdrawOrderNo(String withdrawOrderNo) {
		this.withdrawOrderNo = withdrawOrderNo;
	}

	public Integer getHistoryOrder() {
		return historyOrder;
	}

	public void setHistoryOrder(Integer historyOrder) {
		this.historyOrder = historyOrder;
	}

	public Long getWithdrawAmount() {
		return withdrawAmount;
	}

	public void setWithdrawAmount(Long withdrawAmount) {
		this.withdrawAmount = withdrawAmount;
	}

	public Integer getTransferType() {
		return transferType;
	}

	public void setTransferType(Integer transferType) {
		this.transferType = transferType;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Date getApplyStartTime() {
		return applyStartTime;
	}

	public void setApplyStartTime(Date applyStartTime) {
		this.applyStartTime = applyStartTime;
	}

	public Date getApplyEndTime() {
		return applyEndTime;
	}

	public void setApplyEndTime(Date applyEndTime) {
		this.applyEndTime = applyEndTime;
	}

	public Date getProcessTime() {
		return processTime;
	}

	public void setProcessTime(Date processTime) {
		this.processTime = processTime;
	}

	public Date getProcessStartTime() {
		return processStartTime;
	}

	public void setProcessStartTime(Date processStartTime) {
		this.processStartTime = processStartTime;
	}

	public Date getProcessEndTime() {
		return processEndTime;
	}

	public void setProcessEndTime(Date processEndTime) {
		this.processEndTime = processEndTime;
	}

	public Integer getWithdrawStatus() {
		return withdrawStatus;
	}

	public void setWithdrawStatus(Integer withdrawStatus) {
		this.withdrawStatus = withdrawStatus;
	}

	public Long getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(Long reviewerId) {
		this.reviewerId = reviewerId;
	}

	public String getReviewerName() {
		return reviewerName;
	}

	public void setReviewerName(String reviewerName) {
		this.reviewerName = reviewerName;
	}

	public List<Long> getOrderIdList() {
		return orderIdList;
	}

	public void setOrderIdList(List<Long> orderIdList) {
		this.orderIdList = orderIdList;
	}

	public Integer getProcessType() {
		return processType;
	}

	public void setProcessType(Integer processType) {
		this.processType = processType;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	@Override
	public String toString() {
		return "DistributionWithdrawRecordDTO{" +
				"id=" + id +
				", identityType=" + identityType +
				", userId=" + userId +
				", username='" + username + '\'' +
				", userNumber='" + userNumber + '\'' +
				", mobile='" + mobile + '\'' +
				", storeId=" + storeId +
				", withdrawOrderNo='" + withdrawOrderNo + '\'' +
				", historyOrder=" + historyOrder +
				", withdrawAmount=" + withdrawAmount +
				", transferType=" + transferType +
				", applyTime=" + applyTime +
				", applyStartTime=" + applyStartTime +
				", applyEndTime=" + applyEndTime +
				", processTime=" + processTime +
				", processStartTime=" + processStartTime +
				", processEndTime=" + processEndTime +
				", withdrawStatus=" + withdrawStatus +
				", reviewerId=" + reviewerId +
				", reviewerName='" + reviewerName + '\'' +
				", orderIdList=" + orderIdList +
				", processType=" + processType +
				", rejectReason='" + rejectReason + '\'' +
				'}';
	}
}
