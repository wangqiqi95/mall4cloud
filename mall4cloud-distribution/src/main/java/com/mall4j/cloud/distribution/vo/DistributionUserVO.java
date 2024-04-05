package com.mall4j.cloud.distribution.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销员信息VO
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
public class DistributionUserVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("促销员表")
    private Long distributionUserId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("上级id")
    private Long parentId;

    @ApiModelProperty("上级促销员ids （如：1,2,3）")
    private String parentIds;

    @ApiModelProperty("目前促销员所处层级（0顶级）")
    private Integer grade;

    @ApiModelProperty("绑定时间")
    private Date bindTime;
	/**
	 * DistributionUserStateEnum
	 */
    @ApiModelProperty("状态(-1永久封禁 0待审核状态 1正常 2暂时封禁 3 审核未通过)")
    private Integer state;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像路径")
    private String pic;

    @ApiModelProperty("分销员手机号")
    private String userMobile;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("身份证号码")
    private String identityCardNumber;

    @ApiModelProperty("身份证正面")
	@JsonSerialize(using = ImgJsonSerializer.class)
    private String identityCardPicFront;

    @ApiModelProperty("身份证背面")
	@JsonSerialize(using = ImgJsonSerializer.class)
    private String identityCardPicBack;

    @ApiModelProperty("手持身份证")
	@JsonSerialize(using = ImgJsonSerializer.class)
    private String identityCardPicHold;

    @ApiModelProperty("改变成永久封禁或者暂时封禁时的状态记录")
    private Integer stateRecord;

    @ApiModelProperty("邀请人手机号")
    private String inviteeMobile;

    @ApiModelProperty("邀请人名称")
    private String invitee;

	@ApiModelProperty("累计客户")
	private Integer boundCustomers;

	@ApiModelProperty("累计邀请")
	private Integer invitedVeeker;

	@ApiModelProperty("累计收益")
	private Double accumulateAmount;

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

	public String getInviteeMobile() {
		return inviteeMobile;
	}

	public void setInviteeMobile(String inviteeMobile) {
		this.inviteeMobile = inviteeMobile;
	}

	public String getInvitee() {
		return invitee;
	}

	public void setInvitee(String invitee) {
		this.invitee = invitee;
	}

	public Integer getBoundCustomers() {
		return boundCustomers;
	}

	public void setBoundCustomers(Integer boundCustomers) {
		this.boundCustomers = boundCustomers;
	}

	public Integer getInvitedVeeker() {
		return invitedVeeker;
	}

	public void setInvitedVeeker(Integer invitedVeeker) {
		this.invitedVeeker = invitedVeeker;
	}

	public Double getAccumulateAmount() {
		return accumulateAmount;
	}

	public void setAccumulateAmount(Double accumulateAmount) {
		this.accumulateAmount = accumulateAmount;
	}

	@Override
	public String toString() {
		return "DistributionUserVO{" +
				"distributionUserId=" + distributionUserId +
				", userId=" + userId +
				", parentId=" + parentId +
				", parentIds='" + parentIds + '\'' +
				", grade=" + grade +
				", bindTime=" + bindTime +
				", state=" + state +
				", nickName='" + nickName + '\'' +
				", pic='" + pic + '\'' +
				", userMobile='" + userMobile + '\'' +
				", realName='" + realName + '\'' +
				", identityCardNumber='" + identityCardNumber + '\'' +
				", identityCardPicFront='" + identityCardPicFront + '\'' +
				", identityCardPicBack='" + identityCardPicBack + '\'' +
				", identityCardPicHold='" + identityCardPicHold + '\'' +
				", stateRecord=" + stateRecord +
				", inviteeMobile='" + inviteeMobile + '\'' +
				", invitee='" + invitee + '\'' +
				", boundCustomers=" + boundCustomers +
				", invitedVeeker=" + invitedVeeker +
				", accumulateAmount=" + accumulateAmount +
				'}';
	}
}
