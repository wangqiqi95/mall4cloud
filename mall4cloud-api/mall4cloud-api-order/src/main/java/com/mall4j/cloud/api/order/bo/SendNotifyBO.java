package com.mall4j.cloud.api.order.bo;

/**
 * @author lhd
 * @date 2021/05/08
 */
public class SendNotifyBO {

    /**
     * 订单id
     */
    private Long bizId;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 店铺id
     */
    private Long shopId;
    /**
     * 商品名称
     */
    private String spuName;

    /**
     * 手机号
     */
    private String mobile;
    /**
     * 发送类型
     */
    private Integer sendType;

    /**
     * 实付金额
     */
    private Long actualTotal;

    /**
     * 实付金额转换成string并除以100
     */
    private String actualTotalStr;

    /**
     * 物流公司名称
     */
    private String dvyName;
    /**
     * 退款金额、拼团金额、实付金额
     */
    private String price;
    /**
     * 物流单号
     */
    private String dvyFlowId;
    /**
     * 退款备注
     */
    private String remark;
    /**
     * 等级名称
     */
    private String levelName;
    /**
     * 退款原因
     */
    private String rejectMessage;
    /**
     * 退款原因
     */
    private Integer hour;

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRejectMessage() {
        return rejectMessage;
    }

    public void setRejectMessage(String rejectMessage) {
        this.rejectMessage = rejectMessage;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDvyName() {
        return dvyName;
    }

    public void setDvyName(String dvyName) {
        this.dvyName = dvyName;
    }

    public String getDvyFlowId() {
        return dvyFlowId;
    }

    public void setDvyFlowId(String dvyFlowId) {
        this.dvyFlowId = dvyFlowId;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public Long getBizId() {
        return bizId;
    }

    public void setBizId(Long bizId) {
        this.bizId = bizId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Long getActualTotal() {
        return actualTotal;
    }

    public void setActualTotal(Long actualTotal) {
        this.actualTotal = actualTotal;
    }

    public String getActualTotalStr() {
        return actualTotalStr;
    }

    public void setActualTotalStr(String actualTotalStr) {
        this.actualTotalStr = actualTotalStr;
    }

    @Override
    public String toString() {
        return "SendNotifyBO{" +
                "bizId=" + bizId +
                ", userId=" + userId +
                ", shopId=" + shopId +
                ", spuName='" + spuName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", sendType=" + sendType +
                ", actualTotal=" + actualTotal +
                ", actualTotalStr='" + actualTotalStr + '\'' +
                ", dvyName='" + dvyName + '\'' +
                ", price='" + price + '\'' +
                ", dvyFlowId='" + dvyFlowId + '\'' +
                ", remark='" + remark + '\'' +
                ", levelName='" + levelName + '\'' +
                ", rejectMessage='" + rejectMessage + '\'' +
                ", hour=" + hour +
                '}';
    }
}
