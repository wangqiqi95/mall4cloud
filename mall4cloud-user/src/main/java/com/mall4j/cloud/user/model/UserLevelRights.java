package com.mall4j.cloud.user.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;

/**
 * 等级-权益关联信息
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
public class UserLevelRights extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 等级-权益关联id
     */
    private Long levelRightsId;

    /**
     * 等级id
     */
    private Long userLevelId;

    /**
     * 权益id
     */
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
		return "UserLevelRights{" +
				"levelRightsId=" + levelRightsId +
				",createTime=" + createTime +
				",userLevelId=" + userLevelId +
				",rightsId=" + rightsId +
				'}';
	}
}
