package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品收藏信息DTO
 *
 * @author FrozenWatermelon
 * @date 2020-11-21 14:43:16
 */
public class SpuCollectionDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("收藏表")
    private Long id;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("用户id")
    private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "SpuCollectionDTO{" +
				"id=" + id +
				",spuId=" + spuId +
				",userId=" + userId +
				'}';
	}
}
