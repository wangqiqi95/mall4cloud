package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * DTO
 *
 * @author FrozenWatermelon
 * @date 2022-02-15 19:39:12
 */
public class UserStaffCpRelationDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("会员Id")
    private Long userId;

    @ApiModelProperty("会员企微id")
    private String qiWeiUserId;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("员工企微id")
    private String qiWeiStaffId;

    @ApiModelProperty("绑定关系 1-绑定 2- 解绑")
    private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getQiWeiUserId() {
		return qiWeiUserId;
	}

	public void setQiWeiUserId(String qiWeiUserId) {
		this.qiWeiUserId = qiWeiUserId;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getQiWeiStaffId() {
		return qiWeiStaffId;
	}

	public void setQiWeiStaffId(String qiWeiStaffId) {
		this.qiWeiStaffId = qiWeiStaffId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserStaffCpRelationDTO{" +
				"id=" + id +
				",userId=" + userId +
				",qiWeiUserId=" + qiWeiUserId +
				",staffId=" + staffId +
				",qiWeiStaffId=" + qiWeiStaffId +
				",status=" + status +
				'}';
	}
}
