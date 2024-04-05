package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 商品分组标签关联信息DTO
 *
 * @author lhd
 * @date 2021-02-20 14:28:10
 */
public class SpuTagReferenceDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分组引用id")
    private Long referenceId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("标签id")
    private Long tagId;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("状态(1:正常,0:删除)")
    private Integer status;

    @ApiModelProperty("商品名称")
    private String spuName;

	/**
	 * 排序
	 */
	private Integer seq;

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

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

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	@Override
	public String toString() {
		return "SpuTagReferenceVO{" +
				"referenceId=" + referenceId +
				",shopId=" + shopId +
				",tagId=" + tagId +
				",spuId=" + spuId +
				",status=" + status +
				",spuName=" + spuName +
				'}';
	}
}
