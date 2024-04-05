package com.mall4j.cloud.group.model;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
/**
 * 限时调价活动
 *
 * @author FrozenWatermelon
 * @date 2022-03-10 01:55:13
 */
public class TimeLimitedDiscountActivity extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Integer id;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动开始时间
     */
    private Date activityBeginTime;

    /**
     * 活动结束时间
     */
    private Date activityEndTime;

    /**
     * 是否全部门店 0 否 1是
     */
    private Integer isAllShop;

    /**
     * 权重
     */
    private Integer weight;

    /**
     * 状态 0 未启用 1已启用
     */
    private Integer status;

    /**
     * 
     */
    private Integer deleted;

    /**
     * 创建人id
     */
    private Long createUserId;

    /**
     * 创建人名称
     */
    private String createUserName;

    /**
     * 更新人id
     */
    private Long updateUserId;

    /**
     * 更新人名称
     */
    private String updateUserName;

	/**
	 * 审核状态：0待审核 1审核通过 2驳回
 	 */
	private Integer checkStatus;

	@ApiModelProperty("类型1，限时调价。2，会员日活动调价")
	private Integer type;

	@ApiModelProperty("是否允许同时使用优惠券0否1是")
	private Integer canUseCoupon;

	@ApiModelProperty("是否同时参加满减则活动0否1是")
	private Integer canUseDiscount;


	public Integer getCanUseCoupon() {
		return canUseCoupon;
	}

	public void setCanUseCoupon(Integer canUseCoupon) {
		this.canUseCoupon = canUseCoupon;
	}

	public Integer getCanUseDiscount() {
		return canUseDiscount;
	}

	public void setCanUseDiscount(Integer canUseDiscount) {
		this.canUseDiscount = canUseDiscount;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Date getActivityBeginTime() {
		return activityBeginTime;
	}

	public void setActivityBeginTime(Date activityBeginTime) {
		this.activityBeginTime = activityBeginTime;
	}

	public Date getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(Date activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	public Integer getIsAllShop() {
		return isAllShop;
	}

	public void setIsAllShop(Integer isAllShop) {
		this.isAllShop = isAllShop;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Long getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Long updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	@Override
	public String toString() {
		return "TimeLimitedDiscountActivity{" +
				"id=" + id +
				",activityName=" + activityName +
				",activityBeginTime=" + activityBeginTime +
				",activityEndTime=" + activityEndTime +
				",isAllShop=" + isAllShop +
				",weight=" + weight +
				",status=" + status +
				",deleted=" + deleted +
				",createTime=" + createTime +
				",createUserId=" + createUserId +
				",createUserName=" + createUserName +
				",updateTime=" + updateTime +
				",updateUserId=" + updateUserId +
				",updateUserName=" + updateUserName +
				'}';
	}
}
