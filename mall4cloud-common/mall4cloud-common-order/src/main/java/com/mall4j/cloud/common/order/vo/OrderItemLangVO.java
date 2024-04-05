package com.mall4j.cloud.common.order.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 订单项-国际化VO
 *
 * @author YXF
 * @date 2021-05-17 10:26:54
 */
public class OrderItemLangVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单项ID")
    private Long orderItemId;

    @ApiModelProperty("语言 1.中文 2.英文")
    private Integer lang;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("sku名称")
    private String skuName;

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	@Override
	public String toString() {
		return "OrderItemLangVO{" +
				"orderItemId=" + orderItemId +
				", lang=" + lang +
				", spuName='" + spuName + '\'' +
				", skuName='" + skuName + '\'' +
				'}';
	}
}
