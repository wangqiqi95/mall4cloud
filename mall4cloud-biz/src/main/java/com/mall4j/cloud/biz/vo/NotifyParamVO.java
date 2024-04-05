package com.mall4j.cloud.biz.vo;

import java.util.Date;

/**
 * @author lhd
 * @date 2021/05/08
 */
public class NotifyParamVO {

    /**
     * 关联的订单id(等级)
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
     * 1.订单催付 2.付款成功通知 3.商家同意退款 4.商家拒绝退款 5.核销提醒  6.发货提醒  7.拼团失败提醒 8.拼团成功提醒 9.拼团开团提醒 10.开通会员提醒
     * 101.退款临近超时提醒 102.确认收货提醒 103.买家发起退款提醒 104.买家已退货提醒
     */
    private Integer sendType;
    /**
     * 实付金额转换成string并除以100
     */
    private String actualTotalStr;

    /**
     * 支付方式
     */
    private Integer payType;
    /**
     * 等级名称
     */
    private String levelName;

    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 自提点名称
     */
    private String stationName;
    /**
     * 退款备注
     */
    private String remark;

    /**
     * 退款原因
     */
    private String rejectMessage;
    /**
     * 商品数量
     */
    private Integer prodNum;
    /**
     * 金额
     */
    private Double price;

    /**
     * 退款超时时间，小时
     */
    private Long hour;
    /**
     * 开团人数
     */
    private Integer groupCount;

    /**
     * 发货时间
     */
    private Date dvyTime;
    /**
     * 物流编号
     */
    private String dvyFlowId;
    /**
     * 物流公司名称
     */
    private String dvyName;
    /**
     * 取消时间
     */
    private String cancelTime;
    /**
     * 订单创建时间
     */
    private Date createTime;



    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getActualTotalStr() {
        return actualTotalStr;
    }

    public void setActualTotalStr(String actualTotalStr) {
        this.actualTotalStr = actualTotalStr;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRejectMessage() {
        return rejectMessage;
    }

    public void setRejectMessage(String rejectMessage) {
        this.rejectMessage = rejectMessage;
    }

    public Integer getProdNum() {
        return prodNum;
    }

    public void setProdNum(Integer prodNum) {
        this.prodNum = prodNum;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getHour() {
        return hour;
    }

    public void setHour(Long hour) {
        this.hour = hour;
    }

    public Integer getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(Integer groupCount) {
        this.groupCount = groupCount;
    }

    public Date getDvyTime() {
        return dvyTime;
    }

    public void setDvyTime(Date dvyTime) {
        this.dvyTime = dvyTime;
    }

    public String getDvyFlowId() {
        return dvyFlowId;
    }

    public void setDvyFlowId(String dvyFlowId) {
        this.dvyFlowId = dvyFlowId;
    }

    public String getDvyName() {
        return dvyName;
    }

    public void setDvyName(String dvyName) {
        this.dvyName = dvyName;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "NotifyParamVO{" +
                "bizId=" + bizId +
                ", userId=" + userId +
                ", shopId=" + shopId +
                ", spuName='" + spuName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", sendType=" + sendType +
                ", actualTotalStr='" + actualTotalStr + '\'' +
                ", payType=" + payType +
                ", levelName='" + levelName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", shopName='" + shopName + '\'' +
                ", stationName='" + stationName + '\'' +
                ", remark='" + remark + '\'' +
                ", rejectMessage='" + rejectMessage + '\'' +
                ", prodNum=" + prodNum +
                ", price=" + price +
                ", hour=" + hour +
                ", groupCount=" + groupCount +
                ", dvyTime=" + dvyTime +
                ", dvyFlowId='" + dvyFlowId + '\'' +
                ", dvyName='" + dvyName + '\'' +
                ", cancelTime='" + cancelTime + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
