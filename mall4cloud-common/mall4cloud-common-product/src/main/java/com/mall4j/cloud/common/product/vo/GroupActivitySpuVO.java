package com.mall4j.cloud.common.product.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 拼团活动表VO
 *
 * @author YXF
 * @date 2021-03-20 10:39:31
 */
public class GroupActivitySpuVO{

    @ApiModelProperty("拼团活动id")
    private Long groupActivityId;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("成团人数")
    private Integer groupNumber;

	@ApiModelProperty("已成团人数（统计）")
	private Long groupOrderCount;

	@ApiModelProperty("商品价格（sku最低价）")
	private Long price;

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

	public Integer getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNumber(Integer groupNumber) {
		this.groupNumber = groupNumber;
	}

	public Long getGroupOrderCount() {
		return groupOrderCount;
	}

	public void setGroupOrderCount(Long groupOrderCount) {
		this.groupOrderCount = groupOrderCount;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "GroupActivitySpuVO{" +
				"groupActivityId=" + groupActivityId +
				"spuId=" + spuId +
				", groupNumber=" + groupNumber +
				", groupOrderCount=" + groupOrderCount +
				", price=" + price +
				'}';
	}
}
