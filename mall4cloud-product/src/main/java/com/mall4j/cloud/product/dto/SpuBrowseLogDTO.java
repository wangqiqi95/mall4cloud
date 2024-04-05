package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 商品浏览记录表DTO
 *
 * @author YXF
 * @date 2021-03-19 14:28:14
 */
public class SpuBrowseLogDTO{

	@ApiModelProperty("商品浏览记录id")
	private Long spuBrowseLogId;

	@ApiModelProperty("用户id")
	private Long userId;

	@ApiModelProperty("商品id")
	private Long spuId;

	@ApiModelProperty("分类id")
	private Long categoryId;

	@ApiModelProperty("商品类型")
	private Integer spuType;

	@ApiModelProperty("商品浏览记录id列表(用于批量删除)")
	private List<Long> spuBrowseLogIds;


	public Long getSpuBrowseLogId() {
		return spuBrowseLogId;
	}

	public void setSpuBrowseLogId(Long spuBrowseLogId) {
		this.spuBrowseLogId = spuBrowseLogId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public List<Long> getSpuBrowseLogIds() {
		return spuBrowseLogIds;
	}

	public void setSpuBrowseLogIds(List<Long> spuBrowseLogIds) {
		this.spuBrowseLogIds = spuBrowseLogIds;
	}

	public Integer getSpuType() {
		return spuType;
	}

	public void setSpuType(Integer spuType) {
		this.spuType = spuType;
	}

	@Override
	public String toString() {
		return "SpuBrowseLogDTO{" +
				"spuBrowseLogId=" + spuBrowseLogId +
				", userId=" + userId +
				", spuId=" + spuId +
				", categoryId=" + categoryId +
				", spuBrowseLogIds=" + spuBrowseLogIds +
				", spuType=" + spuType +
				'}';
	}
}
