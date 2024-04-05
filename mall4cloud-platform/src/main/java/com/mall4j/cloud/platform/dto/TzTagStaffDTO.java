package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 标签关联员工DTO
 *
 * @author gmq
 * @date 2022-09-13 12:01:45
 */
public class TzTagStaffDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long id;

    @ApiModelProperty("标签id")
    private Long tagId;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("0正常 1删除")
    private Integer delFlag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		return "TzTagStaffDTO{" +
				"id=" + id +
				",tagId=" + tagId +
				",staffId=" + staffId +
				",delFlag=" + delFlag +
				'}';
	}
}
