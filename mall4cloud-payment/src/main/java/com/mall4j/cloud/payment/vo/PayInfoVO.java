package com.mall4j.cloud.payment.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author Pineapple
 * @date 2021/6/10 9:13
 */
public class PayInfoVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    private Long index;

    @ApiModelProperty("外部订单流水号")
    private String bizPayNo;

    @ApiModelProperty("关联订单号")
    private String orderIds;

    @ApiModelProperty("支付入口[0订单 1充值 2开通会员]")
    private Integer payEntry;

    /**
     * 支付方式 [0积分支付	1微信小程序支付	2支付宝支付	3微信扫码支付
     * 4微信H5支付	5微信公众号支付	6支付宝H5支付	7支付宝APP支付	8微信APP支付    9余额支付  10轻POS收钱吧支付]
     */
    @ApiModelProperty("支付方式")
    private Integer payType;

    @ApiModelProperty("支付状态[-2 收钱吧订单已取消 -1退款 0未支付 1已支付]")
    private Integer payStatus;

    @ApiModelProperty("支付积分")
    private Long payScore;

    @ApiModelProperty("支付金额")
    private Long payAmount;

    @ApiModelProperty("回调时间")
    private Date callbackTime;

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getBizPayNo() {
        return bizPayNo;
    }

    public void setBizPayNo(String bizPayNo) {
        this.bizPayNo = bizPayNo;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public Integer getPayEntry() {
        return payEntry;
    }

    public void setPayEntry(Integer payEntry) {
        this.payEntry = payEntry;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Long getPayScore() {
        return payScore;
    }

    public void setPayScore(Long payScore) {
        this.payScore = payScore;
    }

    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }

    public Date getCallbackTime() {
        return callbackTime;
    }

    public void setCallbackTime(Date callbackTime) {
        this.callbackTime = callbackTime;
    }

    @Override
    public String toString() {
        return "PayInfoVO{" +
                "index=" + index +
                ", bizPayNo='" + bizPayNo + '\'' +
                ", orderIds='" + orderIds + '\'' +
                ", payEntry=" + payEntry +
                ", payType=" + payType +
                ", payStatus=" + payStatus +
                ", payScore=" + payScore +
                ", payAmount=" + payAmount +
                ", callbackTime=" + callbackTime +
                '}';
    }
}
