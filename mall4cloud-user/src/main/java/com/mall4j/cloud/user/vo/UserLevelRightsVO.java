package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 等级-权益关联信息VO
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
public class UserLevelRightsVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("等级-权益关联id")
    private Long levelRightsId;

    @ApiModelProperty("等级id")
    private Long userLevelId;

    @ApiModelProperty("权益id")
    private Long rightsId;

	public Long getLevelRightsId() {
		return levelRightsId;
	}

	public void setLevelRightsId(Long levelRightsId) {
		this.levelRightsId = levelRightsId;
	}

	public Long getUserLevelId() {
		return userLevelId;
	}

	public void setUserLevelId(Long userLevelId) {
		this.userLevelId = userLevelId;
	}

	public Long getRightsId() {
		return rightsId;
	}

	public void setRightsId(Long rightsId) {
		this.rightsId = rightsId;
	}

	@Override
	public String toString() {
		return "UserLevelRightsVO{" +
				"levelRightsId=" + levelRightsId +
				",createTime=" + createTime +
				",userLevelId=" + userLevelId +
				",rightsId=" + rightsId +
				'}';
	}
}
