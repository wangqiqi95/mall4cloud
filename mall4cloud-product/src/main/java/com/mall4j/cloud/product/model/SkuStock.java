package com.mall4j.cloud.product.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 库存信息
 *
 * @author FrozenWatermelon
 * @date 2020-11-11 13:49:06
 */
@Data
public class SkuStock extends BaseModel implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 库存id
	 */
	@TableId(type = IdType.AUTO)
	private Long stockId;

	/**
	 * 商品id
	 */
	private Long spuId;

	/**
	 * SKU ID
	 */
	private Long skuId;

	/**
	 * 实际库存
	 */
	private Integer actualStock;

	/**
	 * 锁定库存
	 */
	private Integer lockStock;

	/**
	 * 可售卖库存
	 */
	private Integer stock;

	/**
	 * 视频号库存
	 */
	private Integer channelsStock;

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Integer getActualStock() {
		return actualStock;
	}

	public void setActualStock(Integer actualStock) {
		this.actualStock = actualStock;
	}

	public Integer getLockStock() {
		return lockStock;
	}

	public void setLockStock(Integer lockStock) {
		this.lockStock = lockStock;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	@Override
	public String toString() {
		return "SkuStock{" +
				"stockId=" + stockId +
				", spuId=" + spuId +
				", skuId=" + skuId +
				", actualStock=" + actualStock +
				", lockStock=" + lockStock +
				", stock=" + stock +
				'}';
	}
}
