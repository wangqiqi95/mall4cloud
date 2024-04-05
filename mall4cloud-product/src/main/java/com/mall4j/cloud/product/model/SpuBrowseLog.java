package com.mall4j.cloud.product.model;

import java.io.Serializable;
import com.mall4j.cloud.common.model.BaseModel;

/**
 * 商品浏览记录表
 *
 * @author YXF
 * @date 2021-03-19 14:28:14
 */
public class SpuBrowseLog extends BaseModel implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 商品浏览记录id
	 */
	private Long spuBrowseLogId;

	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 商品id
	 */
	private Long spuId;

	/**
	 * 分类id
	 */
	private Long categoryId;

	/**
	 * 1:正常 -1:删除
	 */
	private Integer status;

	/**
	 * 商品类型
	 */
	private Integer spuType;

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSpuType() {
		return spuType;
	}

	public void setSpuType(Integer spuType) {
		this.spuType = spuType;
	}

	@Override
	public String toString() {
		return "SpuBrowseLog{" +
				"spuBrowseLogId=" + spuBrowseLogId +
				", userId=" + userId +
				", spuId=" + spuId +
				", categoryId=" + categoryId +
				", status=" + status +
				", spuType=" + spuType +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
