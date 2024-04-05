package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 导购标签用户信息DTO
 *
 * @author ZengFanChang
 * @date 2022-01-08 23:27:34
 */
public class UserLabelUserDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("导购标签ID")
    private Long labelId;

    @ApiModelProperty("用户ID")
    private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLabelId() {
		return labelId;
	}

	public void setLabelId(Long labelId) {
		this.labelId = labelId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "UserLabelUserDTO{" +
				"id=" + id +
				",labelId=" + labelId +
				",userId=" + userId +
				'}';
	}
}
