package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 拼团活动商品表DTO
 *
 * @author YXF
 * @date 2021-03-20 10:39:32
 */
public class GroupSpuDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("活动商品id")
    private Long groupSpuId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("活动id")
    private Long groupActivityId;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("已成团订单数（统计）")
    private Long groupOrderCount;

    @ApiModelProperty("已成团人数（统计）")
    private Long groupNumberCount;

    @ApiModelProperty("状态（1:正常 0:失效 -1:已删除）")
    private Integer status;

	public Long getGroupSpuId() {
		return groupSpuId;
	}

	public void setGroupSpuId(Long groupSpuId) {
		this.groupSpuId = groupSpuId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getGroupActivityId() {
		return groupActivityId;
	}

	public void setGroupActivityId(Long groupActivityId) {
		this.groupActivityId = groupActivityId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getGroupOrderCount() {
		return groupOrderCount;
	}

	public void setGroupOrderCount(Long groupOrderCount) {
		this.groupOrderCount = groupOrderCount;
	}

	public Long getGroupNumberCount() {
		return groupNumberCount;
	}

	public void setGroupNumberCount(Long groupNumberCount) {
		this.groupNumberCount = groupNumberCount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "GroupSpuDTO{" +
				"groupSpuId=" + groupSpuId +
				",shopId=" + shopId +
				",groupActivityId=" + groupActivityId +
				",spuId=" + spuId +
				",groupOrderCount=" + groupOrderCount +
				",groupNumberCount=" + groupNumberCount +
				",status=" + status +
				'}';
	}
}
