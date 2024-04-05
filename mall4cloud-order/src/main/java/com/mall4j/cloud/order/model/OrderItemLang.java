package com.mall4j.cloud.order.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 订单项-国际化
 *
 * @author YXF
 * @date 2021-05-17 10:26:54
 */
public class OrderItemLang extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 订单项ID
     */
    private Long orderItemId;

    /**
     * 语言 1.中文 2.英文
     */
    private Integer lang;

    /**
     * 商品名称
     */
    private String spuName;

    /**
     * sku名称
     */
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
		return "OrderItemLang{" +
				"orderItemId=" + orderItemId +
				",lang=" + lang +
				",spuName=" + spuName +
				",skuName=" + skuName +
				'}';
	}
}
