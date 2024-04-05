package com.mall4j.cloud.payment.bo;

import java.util.Date;

/**
 * @author FrozenWatermelon
 * @date 2020/12/25
 */
public class PayInfoBO {

    /**
     * 支付信息，如商品名称
     */
    private String body;

    /**
     * 支付单号
     */
    private Long payId;

    /**
     * 付款金额
     */
    private Long payAmount;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * api回调域名
     */
    private String apiNoticeUrl;

    /**
     * 支付完成会跳地址
     */
    private String returnUrl;

    /**
     * 第三方用户id
     */
    private String bizUserId;

    /**
     * 支付回调地址类型
     */
    private Integer backType;

    /**
     * 本次支付关联的多个订单号
     */
    private String orderIds;

    /**
     * 是否是购买会员
     */
    private Boolean isVip;

    private String orderNumber;
    
    /**
     * 收钱吧轻POS的订单凭证，H5、微信小程序、微信小程序插件,刷脸终端的场景
     */
    private String orderToken;
    
    /**
     * 订单创建时间
     */
    private Date createTime;
    
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public String getOrderToken() {
        return orderToken;
    }
    
    public void setOrderToken(String orderToken) {
        this.orderToken = orderToken;
    }
    
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }


    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getApiNoticeUrl() {
        return apiNoticeUrl;
    }

    public void setApiNoticeUrl(String apiNoticeUrl) {
        this.apiNoticeUrl = apiNoticeUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getBizUserId() {
        return bizUserId;
    }

    public void setBizUserId(String bizUserId) {
        this.bizUserId = bizUserId;
    }

    public Integer getBackType() {
        return backType;
    }

    public void setBackType(Integer backType) {
        this.backType = backType;
    }

    public Boolean getIsVip() {
        return isVip;
    }

    public void setIsVip(Boolean isVip) {
        this.isVip = isVip;
    }
    
    @Override
    public String toString() {
        return "PayInfoBO{" +
                "body='" + body + '\'' +
                ", payId=" + payId +
                ", payAmount=" + payAmount +
                ", payType=" + payType +
                ", apiNoticeUrl='" + apiNoticeUrl + '\'' +
                ", returnUrl='" + returnUrl + '\'' +
                ", bizUserId='" + bizUserId + '\'' +
                ", backType=" + backType +
                ", orderIds='" + orderIds + '\'' +
                ", isVip=" + isVip +
                ", orderNumber='" + orderNumber + '\'' +
                ", orderToken='" + orderToken + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
