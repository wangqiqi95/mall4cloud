package com.mall4j.cloud.distribution.model;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 佣金管理-佣金提现记录
 *
 * @author ZengFanChang
 * @date 2021-12-05 20:15:06
 */
public class DistributionWithdrawRecord extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 身份类型 1导购 2微客
     */
    @ApiModelProperty("身份类型 1导购 2微客")
    private Integer identityType;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String username;

    /**
     * 工号/卡号
     */
    @ApiModelProperty("工号/卡号")
    private String userNumber;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 门店ID
     */
    @ApiModelProperty("门店ID")
    private Long storeId;

    /**
     * 提现单号
     */
    @ApiModelProperty("提现单号")
    private String withdrawOrderNo;

    /**
     * 是否历史订单 0否 1是
     */
    @ApiModelProperty("是否历史订单 0否 1是")
    private Integer historyOrder;

    /**
     * 提现金额
     */
    @ApiModelProperty("提现金额")
    private Long withdrawAmount;

    /**
     * 提现需退还金额
     */
    @ApiModelProperty("提现需退还金额")
    private Long withdrawNeedRefundAmount;

    /**
     * 转账方式 1默认三方转账
     */
    @ApiModelProperty("转账方式 1默认三方转账")
    private Integer transferType;

    /**
     * 申请提现ID
     */
    @ApiModelProperty("申请提现ID(第三方)")
    private String applyId;

    /**
     * 申请时间
     */
    @ApiModelProperty("申请时间")
    private Date applyTime;

    /**
     * 处理时间
     */
    @ApiModelProperty("处理时间")
    private Date processTime;

    /**
     * 提现状态 0待处理 1提现中 2提现成功 3提现失败 4拒绝提现
     */
    @ApiModelProperty("提现状态 0待处理 1提现中 2提现成功 3提现失败 4拒绝提现 5京东作废")
    private Integer withdrawStatus;

    /**
     * 拒绝原因
     */
    @ApiModelProperty("拒绝原因")
    private String rejectReason;

    /**
     * 转账失败原因
     */
    @ApiModelProperty("转账失败原因")
    private String transferFailReason;

    /**
     * 处理人ID
     */
    @ApiModelProperty("处理人ID")
    private Long reviewerId;

    /**
     * 处理人姓名
     */
    @ApiModelProperty("处理人姓名")
    private String reviewerName;


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

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public Integer getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(Integer withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getTransferFailReason() {
        return transferFailReason;
    }

    public void setTransferFailReason(String transferFailReason) {
        this.transferFailReason = transferFailReason;
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

    public Long getWithdrawNeedRefundAmount() {
        return withdrawNeedRefundAmount;
    }

    public void setWithdrawNeedRefundAmount(Long withdrawNeedRefundAmount) {
        this.withdrawNeedRefundAmount = withdrawNeedRefundAmount;
    }

    @Override
    public String toString() {
        return "DistributionWithdrawRecord{" +
                "createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", id=" + id +
                ", identityType=" + identityType +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", userNumber='" + userNumber + '\'' +
                ", mobile='" + mobile + '\'' +
                ", storeId=" + storeId +
                ", withdrawOrderNo='" + withdrawOrderNo + '\'' +
                ", historyOrder=" + historyOrder +
                ", withdrawAmount=" + withdrawAmount +
                ", withdrawNeedRefundAmount=" + withdrawNeedRefundAmount +
                ", transferType=" + transferType +
                ", applyId='" + applyId + '\'' +
                ", applyTime=" + applyTime +
                ", processTime=" + processTime +
                ", withdrawStatus=" + withdrawStatus +
                ", rejectReason='" + rejectReason + '\'' +
                ", transferFailReason='" + transferFailReason + '\'' +
                ", reviewerId=" + reviewerId +
                ", reviewerName='" + reviewerName + '\'' +
                '}';
    }
}
