package com.mall4j.cloud.seckill.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 秒杀分类信息DTO
 *
 * @author lhd
 * @date 2021-04-19 11:26:35
 */
public class SeckillCategoryDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("秒杀分类id")
    private Long categoryId;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("排序")
    private Integer seq;

    @ApiModelProperty("分类ids")
    private List<Long> categoryIds;

	public List<Long> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	@Override
	public String toString() {
		return "SeckillCategoryDTO{" +
				"categoryId=" + categoryId +
				", name='" + name + '\'' +
				", seq=" + seq +
				", categoryIds=" + categoryIds +
				'}';
	}
}
