package com.mall4j.cloud.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 组织结构表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 13:44:09
 */
public class OrganizationDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("组织节点id")
    private Long orgId;

    @ApiModelProperty("上级节点id")
    private Long parentId;

    @ApiModelProperty("组织节点名称")
    private String orgName;

    @ApiModelProperty("节点类型 1-品牌 ，2-片区，3-店群，4-门店")
    private Integer type;

    @ApiModelProperty("组织节点id")
    private String orgCode;

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	@Override
	public String toString() {
		return "OrganizationDTO{" +
				"orgId=" + orgId +
				",parentId=" + parentId +
				",orgName=" + orgName +
				",type=" + type +
				",orgCode=" + orgCode +
				'}';
	}
}
