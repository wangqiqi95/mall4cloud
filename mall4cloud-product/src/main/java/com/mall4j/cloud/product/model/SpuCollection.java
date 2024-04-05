package com.mall4j.cloud.product.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 商品收藏信息
 *
 * @author FrozenWatermelon
 * @date 2020-11-21 14:43:16
 */
public class SpuCollection extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 收藏表
     */
    private Long id;

    /**
     * 商品id
     */
    private Long spuId;

    /**
     * 用户id
     */
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
		return "SpuCollection{" +
				"id=" + id +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",spuId=" + spuId +
				",userId=" + userId +
				'}';
	}
}
