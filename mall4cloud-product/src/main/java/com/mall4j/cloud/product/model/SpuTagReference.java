package com.mall4j.cloud.product.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 商品分组标签关联信息
 *
 * @author lhd
 * @date 2021-02-20 14:28:10
 */
public class SpuTagReference extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 分组引用id
     */
    private Long referenceId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 标签id
     */
    private Long tagId;

    /**
     * 商品id
     */
    private Long spuId;

    /**
     * 状态(1:正常,0:删除)
     */
    private Integer status;

	/**
	 * 排序
	 */
	private Integer seq;

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "SpuTagReferenceVO{" +
				"referenceId=" + referenceId +
				",shopId=" + shopId +
				",tagId=" + tagId +
				",spuId=" + spuId +
				",status=" + status +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getSeq() {
		return seq;
	}
}
