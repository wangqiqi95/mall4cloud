package com.mall4j.cloud.delivery.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 物流订单项信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
public class DeliveryOrderItem extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 订单物流包裹id
     */
    private Long deliveryOrderId;

    /**
     * 商品图片
     */
    private String imgUrl;

    /**
     * 商品名称
     */
    private String spuName;

    /**
     * 商品数量
     */
    private Integer count;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDeliveryOrderId() {
		return deliveryOrderId;
	}

	public void setDeliveryOrderId(Long deliveryOrderId) {
		this.deliveryOrderId = deliveryOrderId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "DeliveryOrderItem{" +
				"id=" + id +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",deliveryOrderId=" + deliveryOrderId +
				",imgUrl=" + imgUrl +
				",spuName=" + spuName +
				",count=" + count +
				'}';
	}
}
