package com.mall4j.cloud.product.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商品收藏信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-11-21 14:43:16
 */
public class SpuCollectionVO extends BaseVO{
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
		return "SpuCollectionVO{" +
				"id=" + id +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",spuId=" + spuId +
				",userId=" + userId +
				'}';
	}
}
