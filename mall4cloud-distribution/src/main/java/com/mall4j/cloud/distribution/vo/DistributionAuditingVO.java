package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销员申请信息VO
 *
 * @author cl
 * @date 2021-08-09 14:14:05
 */
public class DistributionAuditingVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分销员申请表")
    private Long auditingId;

    @ApiModelProperty("邀请人id")
    private Long parentDistributionUserId;

    @ApiModelProperty("申请人id")
    private Long distributionUserId;

    @ApiModelProperty("申请时间")
    private Date auditingTime;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("不通过原因(0 资料不足 1条件不足 2不招人 -1其他)")
    private Integer reason;

    @ApiModelProperty("审核状态：0 未审核 1已通过 -1未通过")
    private Integer state;

    @ApiModelProperty("操作人(0代表自动审核)")
    private Long modifier;

	@ApiModelProperty("分销员手机号")
    private String userMobile;

	@ApiModelProperty("分销员昵称")
	private String nickName;

	@ApiModelProperty("邀请人手机号")
    private String inviteeMobile;

	@ApiModelProperty("邀请人")
    private String invitee;

	@ApiModelProperty("积累消费金额")
    private Double sumOfConsumption;

	@ApiModelProperty("积累消费笔数")
	private Double expenseNumber;

	@ApiModelProperty("操作人")
	private String modifierName;

	@ApiModelProperty("身份证号码")
	private String identityCardNumber;

	@ApiModelProperty("身份证正面")
	private String identityCardPicFront;

	@ApiModelProperty("身份证背面")
	private String identityCardPicBack;

	@ApiModelProperty("手持身份证")
	private String identityCardPicHold;

	@ApiModelProperty("用户id")
	private Long userId;

	@ApiModelProperty("真实姓名")
	private String realName;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Long getAuditingId() {
		return auditingId;
	}

	public void setAuditingId(Long auditingId) {
		this.auditingId = auditingId;
	}

	public Long getParentDistributionUserId() {
		return parentDistributionUserId;
	}

	public void setParentDistributionUserId(Long parentDistributionUserId) {
		this.parentDistributionUserId = parentDistributionUserId;
	}

	public Long getDistributionUserId() {
		return distributionUserId;
	}

	public void setDistributionUserId(Long distributionUserId) {
		this.distributionUserId = distributionUserId;
	}

	public Date getAuditingTime() {
		return auditingTime;
	}

	public void setAuditingTime(Date auditingTime) {
		this.auditingTime = auditingTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getReason() {
		return reason;
	}

	public void setReason(Integer reason) {
		this.reason = reason;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getModifier() {
		return modifier;
	}

	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
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

	public Double getSumOfConsumption() {
		return sumOfConsumption;
	}

	public void setSumOfConsumption(Double sumOfConsumption) {
		this.sumOfConsumption = sumOfConsumption;
	}

	public Double getExpenseNumber() {
		return expenseNumber;
	}

	public void setExpenseNumber(Double expenseNumber) {
		this.expenseNumber = expenseNumber;
	}

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	@Override
	public String toString() {
		return "DistributionAuditingVO{" +
				"auditingId=" + auditingId +
				", parentDistributionUserId=" + parentDistributionUserId +
				", distributionUserId=" + distributionUserId +
				", auditingTime=" + auditingTime +
				", remarks='" + remarks + '\'' +
				", reason=" + reason +
				", state=" + state +
				", modifier=" + modifier +
				", userMobile='" + userMobile + '\'' +
				", nickName='" + nickName + '\'' +
				", inviteeMobile='" + inviteeMobile + '\'' +
				", invitee='" + invitee + '\'' +
				", sumOfConsumption=" + sumOfConsumption +
				", expenseNumber=" + expenseNumber +
				", modifierName='" + modifierName + '\'' +
				", identityCardNumber='" + identityCardNumber + '\'' +
				", identityCardPicFront='" + identityCardPicFront + '\'' +
				", identityCardPicBack='" + identityCardPicBack + '\'' +
				", identityCardPicHold='" + identityCardPicHold + '\'' +
				", userId=" + userId +
				", realName='" + realName + '\'' +
				'}';
	}
}
