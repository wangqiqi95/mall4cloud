package com.mall4j.cloud.api.product.dto;

import lombok.Builder;

public class IphSyncCategoryDto {

	/**
	 * 父ID
	 */
	private Long parentId;

	/**
	 * 分类名称
	 */
	private String name;

	public IphSyncCategoryDto() {
	}

	public IphSyncCategoryDto(Long parentId, String name) {
		this.parentId = parentId;
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "IphSyncCategoryDto{" + "parentId=" + parentId + ", name='" + name + '\'' + '}';
	}
}
