package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户和标签关联表DTO
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public class UserTagUserDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增id")
    private Long userTagUserId;

    @ApiModelProperty("标签id")
    private Long userTagId;

    @ApiModelProperty("用户id")
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
		return "UserTagUserDTO{" +
				"userTagUserId=" + userTagUserId +
				",userTagId=" + userTagId +
				",userId=" + userId +
				'}';
	}
}
