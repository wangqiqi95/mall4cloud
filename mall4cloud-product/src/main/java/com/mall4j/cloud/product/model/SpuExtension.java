package com.mall4j.cloud.product.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author FrozenWatermelon
 * @date 2020-11-11 13:49:06
 */
public class SpuExtension extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 商品扩展信息表id
     */
    private Long spuExtendId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 商品id
     */
    private Long spuId;

    /**
     * 评论数量
     */
    private Integer commentNum;

    /**
     * 销量
     */
    private Integer saleNum;

	/**
	 * 注水销量
	 */
	private Integer waterSoldNum;

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

	public Integer getWaterSoldNum() {
		return waterSoldNum;
	}

	public void setWaterSoldNum(Integer waterSoldNum) {
		this.waterSoldNum = waterSoldNum;
	}

	public Long getSpuExtendId() {
		return spuExtendId;
	}

	public void setSpuExtendId(Long spuExtendId) {
		this.spuExtendId = spuExtendId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public Integer getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(Integer saleNum) {
		this.saleNum = saleNum;
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

	public Integer getChannelsStock() {
		return channelsStock;
	}

	public void setChannelsStock(Integer channelsStock) {
		this.channelsStock = channelsStock;
	}

	@Override
	public String toString() {
		return "SpuExtension{" +
				"spuExtendId=" + spuExtendId +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", spuId=" + spuId +
				", commentNum=" + commentNum +
				", saleNum=" + saleNum +
				", waterSoldNum=" + waterSoldNum +
				", actualStock=" + actualStock +
				", lockStock=" + lockStock +
				", stock=" + stock +
				", channelsStock=" + channelsStock +
				'}';
	}
}
