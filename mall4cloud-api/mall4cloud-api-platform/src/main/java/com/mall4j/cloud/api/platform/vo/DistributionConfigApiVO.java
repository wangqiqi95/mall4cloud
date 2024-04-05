package com.mall4j.cloud.api.platform.vo;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author cl
 * @date 2021-08-12 09:46:26
 */
public class DistributionConfigApiVO {

    @ApiModelProperty("分销开关： 0:关闭 1:开启")
    private Integer distributionSwitch;

    @ApiModelProperty("客户绑定设置-业绩归属 :  0:允许绑定,关系优先 1:不绑定 分享人优先")
    private Integer attribution;

// 申请条件配置-------------------------------------------------------------

    @ApiModelProperty("申请条件-条件审核判定： 0:人工判定 1:系统判定")
    private Integer autoCheck;

    @ApiModelProperty("申请条件-条件审核判定：购买指定商品，确认收货任意一件商品后计入")
    private List<Long> spuIdList;

    @ApiModelProperty("申请条件-条件审核判定：消费金额大于等于expenseAmount元，实付金额+积分抵扣+余额抵扣总金额，收货后计入(单位: 元)")
    private BigDecimal expenseAmount;

    @ApiModelProperty("申请条件-条件审核判定：消费笔数大于等于expenseNumber次,下单次数，收货后计入")
    private Integer expenseNumber;

    @ApiModelProperty("申请条件-申请所需信息：是否需要真实姓名 true 需要 false不需要")
    private Boolean realName;

    @ApiModelProperty("申请条件-申请所需信息：是否需要身份证号码 true 需要 false不需要")
    private Boolean identityCardPic;

    @ApiModelProperty("申请条件-申请所需信息：是否需要身份证照片 true 需要 false不需要")
    private Boolean identityCardNumber;

// 提现申请配置-------------------------------------------------------------

    @ApiModelProperty("提现申请配置-提现频率(天)")
    private Integer frequency;

    @ApiModelProperty("提现申请配置-单笔提现最高（元）")
    private BigDecimal amountMax;

    @ApiModelProperty("提现申请配置-单笔提现最低 （元）")
    private BigDecimal amountMin;

    @ApiModelProperty("提现申请配置-打款说明")
    private String paymentExplain;

    @ApiModelProperty("提现申请配置-提现次数")
    private Integer number;

    public Integer getDistributionSwitch() {
        return distributionSwitch;
    }

    public void setDistributionSwitch(Integer distributionSwitch) {
        this.distributionSwitch = distributionSwitch;
    }

    public Integer getAttribution() {
        return attribution;
    }

    public void setAttribution(Integer attribution) {
        this.attribution = attribution;
    }

    public Integer getAutoCheck() {
        return autoCheck;
    }

    public void setAutoCheck(Integer autoCheck) {
        this.autoCheck = autoCheck;
    }

    public List<Long> getSpuIdList() {
        return spuIdList;
    }

    public void setSpuIdList(List<Long> spuIdList) {
        this.spuIdList = spuIdList;
    }

    public BigDecimal getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public Integer getExpenseNumber() {
        return expenseNumber;
    }

    public void setExpenseNumber(Integer expenseNumber) {
        this.expenseNumber = expenseNumber;
    }

    public Boolean getRealName() {
        return realName;
    }

    public void setRealName(Boolean realName) {
        this.realName = realName;
    }

    public Boolean getIdentityCardPic() {
        return identityCardPic;
    }

    public void setIdentityCardPic(Boolean identityCardPic) {
        this.identityCardPic = identityCardPic;
    }

    public Boolean getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(Boolean identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public BigDecimal getAmountMax() {
        return amountMax;
    }

    public void setAmountMax(BigDecimal amountMax) {
        this.amountMax = amountMax;
    }

    public BigDecimal getAmountMin() {
        return amountMin;
    }

    public void setAmountMin(BigDecimal amountMin) {
        this.amountMin = amountMin;
    }

    public String getPaymentExplain() {
        return paymentExplain;
    }

    public void setPaymentExplain(String paymentExplain) {
        this.paymentExplain = paymentExplain;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "DistributionConfigApiVO{" +
                "distributionSwitch=" + distributionSwitch +
                ", attribution=" + attribution +
                ", autoCheck=" + autoCheck +
                ", spuIdList=" + spuIdList +
                ", expenseAmount=" + expenseAmount +
                ", expenseNumber=" + expenseNumber +
                ", realName=" + realName +
                ", identityCardPic=" + identityCardPic +
                ", identityCardNumber=" + identityCardNumber +
                ", frequency=" + frequency +
                ", amountMax=" + amountMax +
                ", amountMin=" + amountMin +
                ", paymentExplain='" + paymentExplain + '\'' +
                ", number=" + number +
                '}';
    }
}
