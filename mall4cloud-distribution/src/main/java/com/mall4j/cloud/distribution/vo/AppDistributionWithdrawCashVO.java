package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 提现数据
 * @author cl
 * @date 2021-08-17 09:01:23
 */
public class AppDistributionWithdrawCashVO {

    @ApiModelProperty("提现记录id")
    private Long withdrawCashId;

    @ApiModelProperty("提现金额")
    private BigDecimal amount;

    @ApiModelProperty("手续费")
    private BigDecimal fee;

    @ApiModelProperty("类型(0 手动提现 1自动提现)")
    private Integer type;

    @ApiModelProperty("0 微信红包 1企业付款到微信零钱")
    private Integer moneyFlow;

    @ApiModelProperty("流水号")
    private String merchantOrderId;

    @ApiModelProperty("提现状态(0:申请中 1:提现成功 2:拒绝提现 -1:提现失败)")
    private Integer state;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("更新时间-字符串")
    private String monthTimeStr;

    public Long getWithdrawCashId() {
        return withdrawCashId;
    }

    public void setWithdrawCashId(Long withdrawCashId) {
        this.withdrawCashId = withdrawCashId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getMoneyFlow() {
        return moneyFlow;
    }

    public void setMoneyFlow(Integer moneyFlow) {
        this.moneyFlow = moneyFlow;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMonthTimeStr() {
        return monthTimeStr;
    }

    public void setMonthTimeStr(String monthTimeStr) {
        this.monthTimeStr = monthTimeStr;
    }

    @Override
    public String toString() {
        return "AppDistributionWithdrawCashVO{" +
                "withdrawCashId=" + withdrawCashId +
                ", amount=" + amount +
                ", fee=" + fee +
                ", type=" + type +
                ", moneyFlow=" + moneyFlow +
                ", merchantOrderId='" + merchantOrderId + '\'' +
                ", state=" + state +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", monthTimeStr='" + monthTimeStr + '\'' +
                '}';
    }
}
