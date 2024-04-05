package com.mall4j.cloud.group.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 拼团活动商品规格
 *
 * @author YXF
 * @date 2021-03-20 10:39:32
 */
public class GroupSku extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 拼团活动商品规格id
     */
    private Long groupSkuId;

    /**
     * 拼团活动id
     */
    private Long groupActivityId;

    /**
     * 商品规格id
     */
    private Long skuId;

    /**
     * 活动价格
     */
    private Long actPrice;

    /**
     * 已售数量
     */
    private Long sellNum;

	public Long getGroupSkuId() {
		return groupSkuId;
	}

	public void setGroupSkuId(Long groupSkuId) {
		this.groupSkuId = groupSkuId;
	}

	public Long getGroupActivityId() {
		return groupActivityId;
	}

	public void setGroupActivityId(Long groupActivityId) {
		this.groupActivityId = groupActivityId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Long getActPrice() {
		return actPrice;
	}

	public void setActPrice(Long actPrice) {
		this.actPrice = actPrice;
	}

	public Long getSellNum() {
		return sellNum;
	}

	public void setSellNum(Long sellNum) {
		this.sellNum = sellNum;
	}

	@Override
	public String toString() {
		return "GroupSku{" +
				"groupSkuId=" + groupSkuId +
				",groupActivityId=" + groupActivityId +
				",skuId=" + skuId +
				",actPrice=" + actPrice +
				",sellNum=" + sellNum +
				'}';
	}
}
