package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 员工企微工具信息DTO
 *
 * @author ZengFanChang
 * @date 2022-02-12 00:13:55
 */
public class StaffToolDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("导购ID")
    private Long staffId;

    @ApiModelProperty("工具数据(json格式)")
    private String toolData;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public String getToolData() {
		return toolData;
	}

	public void setToolData(String toolData) {
		this.toolData = toolData;
	}

	@Override
	public String toString() {
		return "StaffToolDTO{" +
				"id=" + id +
				",staffId=" + staffId +
				",toolData=" + toolData +
				'}';
	}
}
