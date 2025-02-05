package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 属性与属性分组关联信息DTO
 *
 * @author YXF
 * @date 2020-11-23 16:20:01
 */
public class AttrCategoryDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("属性与分类关联id")
    private Long attrCategoryId;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("属性id")
    private Long attrId;

	public Long getAttrCategoryId() {
		return attrCategoryId;
	}

	public void setAttrCategoryId(Long attrCategoryId) {
		this.attrCategoryId = attrCategoryId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getAttrId() {
		return attrId;
	}

	public void setAttrId(Long attrId) {
		this.attrId = attrId;
	}

	@Override
	public String toString() {
		return "AttrCategoryDTO{" +
				"attrCategoryId=" + attrCategoryId +
				",categoryId=" + categoryId +
				",attrId=" + attrId +
				'}';
	}
}
