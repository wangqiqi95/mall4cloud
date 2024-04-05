package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销封禁记录VO
 *
 * @author cl
 * @date 2021-08-09 14:14:08
 */
public class DistributionUserBanVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long banId;

    @ApiModelProperty("分销员id")
    private Long distributionUserId;

    @ApiModelProperty("原因(0 失去联系 1恶意刷单 2其他)")
    private Integer banReason;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("状态(1 正常 2暂时封禁 -1永久封禁)")
    private Integer state;

    @ApiModelProperty("修改人")
    private Long modifier;

    @ApiModelProperty("分销员昵称")
    private String nickName;

    @ApiModelProperty("分销员手机号")
    private String userMobile;

    @ApiModelProperty("操作人名称")
    private String modifierName;

	public Long getBanId() {
		return banId;
	}

	public void setBanId(Long banId) {
		this.banId = banId;
	}

	public Long getDistributionUserId() {
		return distributionUserId;
	}

	public void setDistributionUserId(Long distributionUserId) {
		this.distributionUserId = distributionUserId;
	}

	public Integer getBanReason() {
		return banReason;
	}

	public void setBanReason(Integer banReason) {
		this.banReason = banReason;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

	@Override
	public String toString() {
		return "DistributionUserBanVO{" +
				"createTime=" + createTime +
				", updateTime=" + updateTime +
				", banId=" + banId +
				", distributionUserId=" + distributionUserId +
				", banReason=" + banReason +
				", remarks='" + remarks + '\'' +
				", state=" + state +
				", modifier=" + modifier +
				", nickName='" + nickName + '\'' +
				", userMobile='" + userMobile + '\'' +
				", modifierName='" + modifierName + '\'' +
				'}';
	}
}
