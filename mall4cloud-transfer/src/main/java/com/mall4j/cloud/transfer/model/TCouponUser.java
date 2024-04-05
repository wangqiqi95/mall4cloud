package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 优惠券用户关联信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-08 23:40:39
 */
public class TCouponUser extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 优惠券ID
     */
    private Long couponId;

    /**
     * 券码
     */
    private String couponCode;

    /**
     * 优惠券来源信息（1：小程序添加/2：CRM同步优惠券）
     */
    private Integer couponSourceType;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动来源（1：推券/2：领券/3：买券/4：积分活动）
     */
    private Integer activitySource;

    /**
     * 某次领取批次号
     */
    private Long batchId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String userPhone;

    /**
     * 关联crm会员id
     */
    private String vipCode;

    /**
     * 领券时间
     */
    private Date receiveTime;

    /**
     * 开始时间
     */
    private Date userStartTime;

    /**
     * 结束时间
     */
    private Date userEndTime;

    /**
     * 优惠券状态 0:冻结 1:有效 2:核销
     */
    private Integer status;

    /**
     * 导购id
     */
    private Long staffId;

    /**
     * 订单号
     */
    private Long orderNo;

    /**
     * 订单金额
     */
    private Long orderAmount;

    /**
     * 优惠金额
     */
    private Long couponAmount;

    /**
     * 核销人id
     */
    private Long writeOffUserId;

    /**
     * 核销人名称
     */
    private String writeOffUserName;

    /**
     * 核销人工号
     */
    private String writeOffUserCode;

    /**
     * 核销人手机号
     */
    private String writeOffUserPhone;

    /**
     * 核销门店id
     */
    private Long writeOffShopId;

    /**
     * 核销门店名称
     */
    private String writeOffShopName;

    /**
     * 核销时间
     */
    private Date writeOffTime;

    /**
     * 微信支付编号
     */
    private String wechatPayNo;

    /**
     * 领取门店id
     */
    private Long shopId;

    /**
     * 领取门店名称
     */
    private String shopName;

    /**
     * 发放人（存在系统送券情况）
     */
    private Long createId;

    /**
     * 发放人名称
     */
    private String createName;

    /**
     * 发放人手机号
     */
    private String createPhone;

    /**
     * 
     */
    private String token;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public Integer getCouponSourceType() {
		return couponSourceType;
	}

	public void setCouponSourceType(Integer couponSourceType) {
		this.couponSourceType = couponSourceType;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Integer getActivitySource() {
		return activitySource;
	}

	public void setActivitySource(Integer activitySource) {
		this.activitySource = activitySource;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getVipCode() {
		return vipCode;
	}

	public void setVipCode(String vipCode) {
		this.vipCode = vipCode;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Date getUserStartTime() {
		return userStartTime;
	}

	public void setUserStartTime(Date userStartTime) {
		this.userStartTime = userStartTime;
	}

	public Date getUserEndTime() {
		return userEndTime;
	}

	public void setUserEndTime(Date userEndTime) {
		this.userEndTime = userEndTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public Long getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Long orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Long getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(Long couponAmount) {
		this.couponAmount = couponAmount;
	}

	public Long getWriteOffUserId() {
		return writeOffUserId;
	}

	public void setWriteOffUserId(Long writeOffUserId) {
		this.writeOffUserId = writeOffUserId;
	}

	public String getWriteOffUserName() {
		return writeOffUserName;
	}

	public void setWriteOffUserName(String writeOffUserName) {
		this.writeOffUserName = writeOffUserName;
	}

	public String getWriteOffUserCode() {
		return writeOffUserCode;
	}

	public void setWriteOffUserCode(String writeOffUserCode) {
		this.writeOffUserCode = writeOffUserCode;
	}

	public String getWriteOffUserPhone() {
		return writeOffUserPhone;
	}

	public void setWriteOffUserPhone(String writeOffUserPhone) {
		this.writeOffUserPhone = writeOffUserPhone;
	}

	public Long getWriteOffShopId() {
		return writeOffShopId;
	}

	public void setWriteOffShopId(Long writeOffShopId) {
		this.writeOffShopId = writeOffShopId;
	}

	public String getWriteOffShopName() {
		return writeOffShopName;
	}

	public void setWriteOffShopName(String writeOffShopName) {
		this.writeOffShopName = writeOffShopName;
	}

	public Date getWriteOffTime() {
		return writeOffTime;
	}

	public void setWriteOffTime(Date writeOffTime) {
		this.writeOffTime = writeOffTime;
	}

	public String getWechatPayNo() {
		return wechatPayNo;
	}

	public void setWechatPayNo(String wechatPayNo) {
		this.wechatPayNo = wechatPayNo;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getCreatePhone() {
		return createPhone;
	}

	public void setCreatePhone(String createPhone) {
		this.createPhone = createPhone;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "TCouponUser{" +
				"id=" + id +
				",couponId=" + couponId +
				",couponCode=" + couponCode +
				",couponSourceType=" + couponSourceType +
				",activityId=" + activityId +
				",activitySource=" + activitySource +
				",batchId=" + batchId +
				",userId=" + userId +
				",userName=" + userName +
				",userPhone=" + userPhone +
				",vipCode=" + vipCode +
				",receiveTime=" + receiveTime +
				",userStartTime=" + userStartTime +
				",userEndTime=" + userEndTime +
				",status=" + status +
				",staffId=" + staffId +
				",orderNo=" + orderNo +
				",orderAmount=" + orderAmount +
				",couponAmount=" + couponAmount +
				",writeOffUserId=" + writeOffUserId +
				",writeOffUserName=" + writeOffUserName +
				",writeOffUserCode=" + writeOffUserCode +
				",writeOffUserPhone=" + writeOffUserPhone +
				",writeOffShopId=" + writeOffShopId +
				",writeOffShopName=" + writeOffShopName +
				",writeOffTime=" + writeOffTime +
				",wechatPayNo=" + wechatPayNo +
				",shopId=" + shopId +
				",shopName=" + shopName +
				",createId=" + createId +
				",createName=" + createName +
				",createPhone=" + createPhone +
				",updateTime=" + updateTime +
				",token=" + token +
				'}';
	}
}
