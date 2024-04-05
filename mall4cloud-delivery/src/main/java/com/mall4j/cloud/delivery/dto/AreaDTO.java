package com.mall4j.cloud.delivery.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 省市区地区信息DTO
 *
 * @author YXF
 * @date 2020-11-25 15:16:14
 */
public class AreaDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty()
    private Long areaId;

    @NotNull(message = "地址不能为空")
    @ApiModelProperty("地址")
    private String areaName;

	@NotNull(message = "上级地址不能为空")
    @ApiModelProperty("上级地址")
    private Long parentId;

	@NotNull(message = "等级不能为空")
    @ApiModelProperty("等级（从1开始）")
    private Integer level;

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "AreaDTO{" +
				"areaId=" + areaId +
				",areaName=" + areaName +
				",parentId=" + parentId +
				",level=" + level +
				'}';
	}
}
