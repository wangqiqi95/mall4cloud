package com.mall4j.cloud.api.openapi.ipuhuo.vo;

import com.mall4j.cloud.api.openapi.ipuhuo.dto.BaseResultDto;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class CategoryVO implements BaseResultDto,Serializable {

	private static final long serialVersionUID = -6500909066376626523L;
	@ApiModelProperty(value = "类目id")
	private Long cid;

	@ApiModelProperty(value = "类目层级,可选")
	private Integer level;

	@ApiModelProperty(value = "类目名称")
	private String name;

	@ApiModelProperty(value = "是否叶子类目(1是0否)")
	private Integer isleaf;

	@ApiModelProperty(value = "父类id")
	private Long parentid;

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(Integer isleaf) {
		this.isleaf = isleaf;
	}

	public Long getParentid() {
		return parentid;
	}

	public void setParentid(Long parentid) {
		this.parentid = parentid;
	}

	@Override
	public String toString() {
		return "CategoryVO{" + "cid=" + cid + ", level=" + level + ", name='" + name + '\'' + ", isleaf=" + isleaf + ", parentid=" + parentid + '}';
	}
}
