package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销员信息DTO
 *
 * @author cl
 * @date 2021-08-09 14:14:07
 */
public class DistributionUserDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("促销员表")
    private Long distributionUserId;

    @ApiModelProperty("卡号")
    private String cardNo;

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

    @ApiModelProperty("状态(-1永久封禁 0待审核状态 1正常 2暂时封禁 3 审核未通过)")
    private Integer state;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像路径")
    private String pic;

    @ApiModelProperty("手机号")
    private String userMobile;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("身份证号码")
    private String identityCardNumber;

    @ApiModelProperty("身份证正面")
    private String identityCardPicFront;

    @ApiModelProperty("身份证背面")
    private String identityCardPicBack;

    @ApiModelProperty("手持身份证")
    private String identityCardPicHold;

    @ApiModelProperty("改变成封禁时的状态记录")
    private Integer stateRecord;

    @ApiModelProperty("邀请人手机号")
    private String inviteeMobile;

	@ApiModelProperty("排序字段" +
			"分销管理-分销员-分销员管理： 0无 1加入时间 2累计客户 3累计邀请 4累计收益" +
			"分销管理-业绩统计： 0无 1一代佣金 2二代佣金 3邀请奖励 4待结算金额 5可提现金额 6已失效金额")
	private Integer sortParam;

	@ApiModelProperty("排序类型 0无 1 正序 2倒序")
	private Integer sortType;

	public Long getDistributionUserId() {
		return distributionUserId;
	}

	public void setDistributionUserId(Long distributionUserId) {
		this.distributionUserId = distributionUserId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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

	public Integer getSortParam() {
		if (sortParam == null) {
			return 0;
		}
		return sortParam;
	}

	public void setSortParam(Integer sortParam) {
		this.sortParam = sortParam;
	}

	public Integer getSortType() {
		if (sortType == null) {
			return 0;
		}
		return sortType;
	}

	public void setSortType(Integer sortType) {
		this.sortType = sortType;
	}

	@Override
	public String toString() {
		return "DistributionUserDTO{" +
				"distributionUserId=" + distributionUserId +
				", cardNo='" + cardNo + '\'' +
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
				", InviteeMobile='" + inviteeMobile + '\'' +
				", sortParam=" + sortParam +
				", sortType=" + sortType +
				'}';
	}
}
