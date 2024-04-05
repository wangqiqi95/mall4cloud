package com.mall4j.cloud.payment.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Pineapple
 * @date 2021/6/9 16:00
 */
public class AccountDetailVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("微信金额")
    private Long wechatAmount;

    @ApiModelProperty("支付宝金额")
    private Long alipayAmount;

    @ApiModelProperty("余额金额")
    private Long balanceAmount;

    @ApiModelProperty("微信占比")
    private Double wechatPercent;

    @ApiModelProperty("支付宝占比")
    private Double alipayPercent;

    @ApiModelProperty("余额占比")
    private Double balancePercent;

    @ApiModelProperty("合计")
    private Long total;

    public Long getWechatAmount() {
        return wechatAmount;
    }

    public void setWechatAmount(Long wechatAmount) {
        this.wechatAmount = wechatAmount;
    }

    public Long getAlipayAmount() {
        return alipayAmount;
    }

    public void setAlipayAmount(Long alipayAmount) {
        this.alipayAmount = alipayAmount;
    }

    public Long getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Long balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public Double getWechatPercent() {
        return wechatPercent;
    }

    public void setWechatPercent(Double wechatPercent) {
        this.wechatPercent = wechatPercent;
    }

    public Double getAlipayPercent() {
        return alipayPercent;
    }

    public void setAlipayPercent(Double alipayPercent) {
        this.alipayPercent = alipayPercent;
    }

    public Double getBalancePercent() {
        return balancePercent;
    }

    public void setBalancePercent(Double balancePercent) {
        this.balancePercent = balancePercent;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "AccountDetailVO{" +
                "wechatAmount=" + wechatAmount +
                ", alipayAmount=" + alipayAmount +
                ", balanceAmount=" + balanceAmount +
                ", wechatPercent=" + wechatPercent +
                ", alipayPercent=" + alipayPercent +
                ", balancePercent=" + balancePercent +
                ", total=" + total +
                '}';
    }
}
