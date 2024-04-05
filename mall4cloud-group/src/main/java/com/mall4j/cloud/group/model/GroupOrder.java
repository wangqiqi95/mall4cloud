package com.mall4j.cloud.group.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 拼团订单表
 *
 * @author YXF
 * @date 2021-03-20 10:39:32
 */
public class GroupOrder extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 拼团订单id
     */
    private Long groupOrderId;

	/**
	 * 拼团活动id
	 */
	private Long groupActivityId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 拼团团队id
     */
    private Long groupTeamId;

    /**
     * user_id(当user_id为0时标识为机器人)
     */
    private Long userId;

    /**
     * 身份标识(0:成员  1:团长)
     */
    private Integer identityType;

    /**
     * 活动商品金额
     */
    private Long activityProdPrice;

    /**
     * 支付金额
     */
    private Long payPrice;

    /**
     * 订单编号
     */
    private Long orderId;

    /**
     * 状态(0:待支付、1:支付成功、-1:失效)
     */
    private Integer status;

	/**
	 * 商品数量
	 */
	private Integer count;

	public Long getGroupOrderId() {
		return groupOrderId;
	}

	public void setGroupOrderId(Long groupOrderId) {
		this.groupOrderId = groupOrderId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getGroupTeamId() {
		return groupTeamId;
	}

	public void setGroupTeamId(Long groupTeamId) {
		this.groupTeamId = groupTeamId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getIdentityType() {
		return identityType;
	}

	public void setIdentityType(Integer identityType) {
		this.identityType = identityType;
	}

	public Long getActivityProdPrice() {
		return activityProdPrice;
	}

	public void setActivityProdPrice(Long activityProdPrice) {
		this.activityProdPrice = activityProdPrice;
	}

	public Long getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(Long payPrice) {
		this.payPrice = payPrice;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getGroupActivityId() {
		return groupActivityId;
	}

	public void setGroupActivityId(Long groupActivityId) {
		this.groupActivityId = groupActivityId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "GroupOrder{" +
				"groupOrderId=" + groupOrderId +
				", groupActivityId=" + groupActivityId +
				", shopId=" + shopId +
				", groupTeamId=" + groupTeamId +
				", userId='" + userId + '\'' +
				", identityType=" + identityType +
				", activityProdPrice=" + activityProdPrice +
				", payPrice=" + payPrice +
				", orderId=" + orderId +
				", status=" + status +
				", count=" + count +
				'}';
	}
}
