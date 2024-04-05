package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author cl
 * @date 2021-08-10 14:16:25
 */
public class DistributionUserAchievementVO {

    @ApiModelProperty("分销员id")
    private Long distributionUserId;

    @ApiModelProperty("分销员分销钱包id")
    private Long walletId;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("手机号")
    private String userMobile;

    @ApiModelProperty("直推奖励")
    private Double aGenerationCommission;

    @ApiModelProperty("间推奖励")
    private Double  secondGenerationCommission;

    @ApiModelProperty("邀请奖励")
    private Double invitationRewards;

    @ApiModelProperty("待结算金额")
    private Long unsettledAmount;

    @ApiModelProperty("可提现金额")
    private Long settledAmount;

    @ApiModelProperty("已失效金额")
    private Long invalidAmount;

    @ApiModelProperty("积累收益")
    private Long accumulateAmount;

    public Long getDistributionUserId() {
        return distributionUserId;
    }

    public void setDistributionUserId(Long distributionUserId) {
        this.distributionUserId = distributionUserId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public Double getaGenerationCommission() {
        return aGenerationCommission;
    }

    public void setaGenerationCommission(Double aGenerationCommission) {
        this.aGenerationCommission = aGenerationCommission;
    }

    public Double getSecondGenerationCommission() {
        return secondGenerationCommission;
    }

    public void setSecondGenerationCommission(Double secondGenerationCommission) {
        this.secondGenerationCommission = secondGenerationCommission;
    }

    public Double getInvitationRewards() {
        return invitationRewards;
    }

    public void setInvitationRewards(Double invitationRewards) {
        this.invitationRewards = invitationRewards;
    }

    public Long getUnsettledAmount() {
        return unsettledAmount;
    }

    public void setUnsettledAmount(Long unsettledAmount) {
        this.unsettledAmount = unsettledAmount;
    }

    public Long getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(Long settledAmount) {
        this.settledAmount = settledAmount;
    }

    public Long getInvalidAmount() {
        return invalidAmount;
    }

    public void setInvalidAmount(Long invalidAmount) {
        this.invalidAmount = invalidAmount;
    }

    public Long getAccumulateAmount() {
        return accumulateAmount;
    }

    public void setAccumulateAmount(Long accumulateAmount) {
        this.accumulateAmount = accumulateAmount;
    }

    @Override
    public String toString() {
        return "DistributionUserAchievementVO{" +
                "distributionUserId=" + distributionUserId +
                ", walletId=" + walletId +
                ", nickName='" + nickName + '\'' +
                ", userMobile='" + userMobile + '\'' +
                ", aGenerationCommission=" + aGenerationCommission +
                ", secondGenerationCommission=" + secondGenerationCommission +
                ", invitationRewards=" + invitationRewards +
                ", unsettledAmount=" + unsettledAmount +
                ", settledAmount=" + settledAmount +
                ", invalidAmount=" + invalidAmount +
                ", accumulateAmount=" + accumulateAmount +
                '}';
    }
}
