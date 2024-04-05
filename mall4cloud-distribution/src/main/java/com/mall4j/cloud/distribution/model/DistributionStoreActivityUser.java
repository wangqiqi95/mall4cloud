package com.mall4j.cloud.distribution.model;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 门店活动-报名用户
 *
 * @author gww
 * @date 2021-12-27 13:42:16
 */
public class DistributionStoreActivityUser extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键ID")
    private Long id;

	@ApiModelProperty("组织ID")
    private Long orgId;

	@ApiModelProperty("活动ID")
    private Long activityId;

	@ApiModelProperty("报名用户ID")
    private Long userId;

	@ApiModelProperty("报名用户名称")
    private String userName;

	@ApiModelProperty("报名用户手机号")
    private String userMobile;

	@ApiModelProperty("报名用户性别 1-男 2-女")
	private Integer userGender;

	@ApiModelProperty("报名用户年龄")
    private Integer userAge;

	@ApiModelProperty("报名用户证件号")
    private String userIdCard;

	@ApiModelProperty("报名用户所属导购")
    private Long userStaffId;

	@ApiModelProperty("报名用户所属导购名称")
    private String userStaffName;

	@ApiModelProperty("报名门店ID")
    private Long storeId;

	@ApiModelProperty("报名门店名称")
    private String storeName;

	@ApiModelProperty("报名状态 0-已报名 1-已取消")
	private Integer status;

	@ApiModelProperty("签到状态 0-否 1-是")
	private Integer signStatus;

	@ApiModelProperty("签到时间")
	private Date signTime;

	@ApiModelProperty("衣服尺码")
	private String clothesSize;

	@ApiModelProperty("鞋子尺码")
	private String shoesSize;

	public String getClothesSize() {
		return clothesSize;
	}

	public void setClothesSize(String clothesSize) {
		this.clothesSize = clothesSize;
	}

	public String getShoesSize() {
		return shoesSize;
	}

	public void setShoesSize(String shoesSize) {
		this.shoesSize = shoesSize;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
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

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public Integer getUserGender() {
		return userGender;
	}

	public void setUserGender(Integer userGender) {
		this.userGender = userGender;
	}

	public Integer getUserAge() {
		return userAge;
	}

	public void setUserAge(Integer userAge) {
		this.userAge = userAge;
	}

	public String getUserIdCard() {
		return userIdCard;
	}

	public void setUserIdCard(String userIdCard) {
		this.userIdCard = userIdCard;
	}

	public Long getUserStaffId() {
		return userStaffId;
	}

	public void setUserStaffId(Long userStaffId) {
		this.userStaffId = userStaffId;
	}

	public String getUserStaffName() {
		return userStaffName;
	}

	public void setUserStaffName(String userStaffName) {
		this.userStaffName = userStaffName;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(Integer signStatus) {
		this.signStatus = signStatus;
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}
}
