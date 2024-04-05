package com.mall4j.cloud.delivery.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 物流订单项信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
public class DeliveryOrderItemVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("订单物流包裹id")
    private Long deliveryOrderId;

    @ApiModelProperty("商品图片")
    private String imgUrl;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("商品数量")
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
		return "DeliveryOrderItemVO{" +
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
