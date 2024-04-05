package com.mall4j.cloud.common.product.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分类信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Data
public class CategoryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("父ID")
    private Long parentId;

    @ApiModelProperty("父分类名称")
	private String parentName;

    @ApiModelProperty("分类语言")
    private List<CategoryLangVO> categoryLangList;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("分类描述")
    private String desc;

    @ApiModelProperty("分类地址{parent_id}-{child_id},...")
    private String path;

    @ApiModelProperty("状态 1:enable, 0:disable, -1:deleted")
    private Integer status;

    @ApiModelProperty("分类图标")
    private String icon;

    @ApiModelProperty("分类的显示图片")
    private String imgUrl;

    @ApiModelProperty("分类层级 从0开始")
    private Integer level;

	@ApiModelProperty("分类扣率")
	private Double deductionRate;

	@ApiModelProperty("排序")
	private Integer seq;

	@ApiModelProperty("上级/子分类列表")
	private List<CategoryVO> categories;

	@ApiModelProperty("平台一级分类id")
	private Long primaryCategoryId;

	@ApiModelProperty("平台二级分类id")
	private Long secondaryCategoryId;

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public List<CategoryLangVO> getCategoryLangList() {
		return categoryLangList;
	}

	public void setCategoryLangList(List<CategoryLangVO> categoryLangList) {
		this.categoryLangList = categoryLangList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public List<CategoryVO> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryVO> categories) {
		this.categories = categories;
	}

	public Double getDeductionRate() {
		return deductionRate;
	}

	public void setDeductionRate(Double deductionRate) {
		this.deductionRate = deductionRate;
	}

	public Long getPrimaryCategoryId() {
		return primaryCategoryId;
	}

	public void setPrimaryCategoryId(Long primaryCategoryId) {
		this.primaryCategoryId = primaryCategoryId;
	}

	@Override
	public String toString() {
		return "CategoryVO{" +
				"categoryId=" + categoryId +
				", shopId=" + shopId +
				", parentId=" + parentId +
				", parentName='" + parentName + '\'' +
				", categoryLangList=" + categoryLangList +
				", name='" + name + '\'' +
				", desc='" + desc + '\'' +
				", path='" + path + '\'' +
				", status=" + status +
				", icon='" + icon + '\'' +
				", imgUrl='" + imgUrl + '\'' +
				", level=" + level +
				", deductionRate=" + deductionRate +
				", seq=" + seq +
				", categories=" + categories +
				", primaryCategoryId=" + primaryCategoryId +
				'}';
	}
}
