package com.mall4j.cloud.product.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 电子价签商品
 *
 * @author FrozenWatermelon
 * @date 2022-03-27 22:24:29
 */
public class ElPriceProd extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 电子价签id
     */
    private Long elId;

    /**
     * 商品id
     */
    private Long spuId;

    /**
     * 商品skuid
     */
    private Long skuId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getElId() {
		return elId;
	}

	public void setElId(Long elId) {
		this.elId = elId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	@Override
	public String toString() {
		return "ElPriceProd{" +
				"id=" + id +
				",elId=" + elId +
				",spuId=" + spuId +
				",skuId=" + skuId +
				'}';
	}
}
