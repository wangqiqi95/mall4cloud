package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销员信息
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
public class DistributionUser extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 促销员表
     */
    private Long distributionUserId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 上级id
     */
    private Long parentId;

    /**
     * 上级促销员ids （如：1,2,3）
     */
    private String parentIds;

    /**
     * 目前促销员所处层级（0顶级）
     */
    private Integer grade;

    /**
     * 绑定时间
     */
    private Date bindTime;

    /**
	 * DistributionUserStateEnum
     * 状态(-1永久封禁 0待审核状态 1正常 2暂时封禁 3 审核未通过)
     */
    private Integer state;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像路径
     */
    private String pic;

    /**
     * 手机号
     */
    private String userMobile;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号码
     */
    private String identityCardNumber;

    /**
     * 身份证正面
     */
    private String identityCardPicFront;

    /**
     * 身份证背面
     */
    private String identityCardPicBack;

    /**
     * 手持身份证
     */
    private String identityCardPicHold;

    /**
     * 改变成封禁时的状态记录
     */
    private Integer stateRecord;

	public Long getDistributionUserId() {
		return distributionUserId;
	}

	public void setDistributionUserId(Long distributionUserId) {
		this.distributionUserId = distributionUserId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public Date getBindTime() {
		return bindTime;
	}

	public void setBindTime(Date bindTime) {
		this.bindTime = bindTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdentityCardNumber() {
		return identityCardNumber;
	}

	public void setIdentityCardNumber(String identityCardNumber) {
		this.identityCardNumber = identityCardNumber;
	}

	public String getIdentityCardPicFront() {
		return identityCardPicFront;
	}

	public void setIdentityCardPicFront(String identityCardPicFront) {
		this.identityCardPicFront = identityCardPicFront;
	}

	public String getIdentityCardPicBack() {
		return identityCardPicBack;
	}

	public void setIdentityCardPicBack(String identityCardPicBack) {
		this.identityCardPicBack = identityCardPicBack;
	}

	public String getIdentityCardPicHold() {
		return identityCardPicHold;
	}

	public void setIdentityCardPicHold(String identityCardPicHold) {
		this.identityCardPicHold = identityCardPicHold;
	}

	public Integer getStateRecord() {
		return stateRecord;
	}

	public void setStateRecord(Integer stateRecord) {
		this.stateRecord = stateRecord;
	}

	@Override
	public String toString() {
		return "DistributionUser{" +
				"distributionUserId=" + distributionUserId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",userId=" + userId +
				",parentId=" + parentId +
				",parentIds=" + parentIds +
				",grade=" + grade +
				",bindTime=" + bindTime +
				",state=" + state +
				",nickName=" + nickName +
				",pic=" + pic +
				",userMobile=" + userMobile +
				",realName=" + realName +
				",identityCardNumber=" + identityCardNumber +
				",identityCardPicFront=" + identityCardPicFront +
				",identityCardPicBack=" + identityCardPicBack +
				",identityCardPicHold=" + identityCardPicHold +
				",stateRecord=" + stateRecord +
				'}';
	}
}
