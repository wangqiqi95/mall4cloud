package com.mall4j.cloud.common.product.vo.app;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 分类-搜索头部信息信息VO
 *
 * @author YXF
 * @date 2021-03-04
 */
public class CategorySearchAppVO {

	@ApiModelProperty("一级分类信息")
	private CategoryAppVO primaryCategory;

	@ApiModelProperty("二级分类信息")
	private List<CategoryAppVO> secondaryCategory;

	@ApiModelProperty("三级分类信息")
	private List<CategoryAppVO> categoryVO;

	public CategoryAppVO getPrimaryCategory() {
		return primaryCategory;
	}

	public void setPrimaryCategory(CategoryAppVO primaryCategory) {
		this.primaryCategory = primaryCategory;
	}

	public List<CategoryAppVO> getSecondaryCategory() {
		return secondaryCategory;
	}

	public void setSecondaryCategory(List<CategoryAppVO> secondaryCategory) {
		this.secondaryCategory = secondaryCategory;
	}

	public List<CategoryAppVO> getCategoryVO() {
		return categoryVO;
	}

	public void setCategoryVO(List<CategoryAppVO> categoryVO) {
		this.categoryVO = categoryVO;
	}

	@Override
	public String toString() {
		return "CategorySearchAppVO{" +
				"primaryCategory=" + primaryCategory +
				", secondaryCategory=" + secondaryCategory +
				", categoryVO=" + categoryVO +
				'}';
	}
}
