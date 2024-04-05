package com.mall4j.cloud.user.model;

import java.io.Serializable;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 用户和标签关联表
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public class UserTagUser extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    private Long userTagUserId;

    /**
     * 标签id
     */
    private Long userTagId;

    /**
     * 用户id
     */
    private Long userId;

	public Long getUserTagUserId() {
		return userTagUserId;
	}

	public void setUserTagUserId(Long userTagUserId) {
		this.userTagUserId = userTagUserId;
	}

	public Long getUserTagId() {
		return userTagId;
	}

	public void setUserTagId(Long userTagId) {
		this.userTagId = userTagId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "UserTagUser{" +
				"userTagUserId=" + userTagUserId +
				",createTime=" + createTime +
				",userTagId=" + userTagId +
				",userId=" + userId +
				'}';
	}
}
