package com.mall4j.cloud.order.bo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author FrozenWatermelon
 * @date 2021/2/4
 */
public class SubmitOrderPayAmountInfoBO {


    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "总共需要支付金额")
    private Long totalFee;

    @ApiModelProperty(value = "总共需要支付积分（积分抵扣金额）")
    private Long totalScore;

    @ApiModelProperty(value = "总共需要支付积分数量")
    private Long orderScore;

    @ApiModelProperty(value = "订单类型")
    private Integer orderType;

    @ApiModelProperty(value = "订单地址id")
    private Long orderAddrId;

    @ApiModelProperty(value = "订单编号")
    private String orderNumber;


    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
    }

    public Long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Long totalScore) {
        this.totalScore = totalScore;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Long getOrderAddrId() {
        return orderAddrId;
    }

    public void setOrderAddrId(Long orderAddrId) {
        this.orderAddrId = orderAddrId;
    }

    public Long getOrderScore() {
        return orderScore;
    }

    public void setOrderScore(Long orderScore) {
        this.orderScore = orderScore;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public String toString() {
        return "SubmitOrderPayAmountInfoBO{" +
                "createTime=" + createTime +
                ", totalFee=" + totalFee +
                ", totalScore=" + totalScore +
                ", orderType=" + orderType +
                ", orderAddrId=" + orderAddrId +
                ", orderScore=" + orderScore +
                '}';
    }
}
