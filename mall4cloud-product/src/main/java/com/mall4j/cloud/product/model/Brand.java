package com.mall4j.cloud.product.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;

/**
 * 品牌信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public class Brand extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * brand_id
     */
    private Long brandId;

    /**
     * 品牌名称
     */
    private String name;

    /**
     * 品牌描述
     */
    private String desc;

    /**
     * 品牌logo图片
     */
    private String imgUrl;

    /**
     * 检索首字母
     */
    private String firstLetter;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 状态 1:enable, 0:disable, -1:deleted
     */
    private Integer status;

	/**
	 * 是否置顶 0：不置顶  1：置顶
	 */
	private Integer isTop;

	/**
	 * 品牌下的商品数量
	 */
	private Integer spuCount;

	/**
	 * 店铺id
	 */
	private Long shopId;

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public Integer getSpuCount() {
		return spuCount;
	}

	public void setSpuCount(Integer spuCount) {
		this.spuCount = spuCount;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Override
	public String toString() {
		return "Brand{" +
				"brandId=" + brandId +
				", name='" + name + '\'' +
				", desc='" + desc + '\'' +
				", imgUrl='" + imgUrl + '\'' +
				", firstLetter='" + firstLetter + '\'' +
				", seq=" + seq +
				", status=" + status +
				", isTop=" + isTop +
				", spuCount=" + spuCount +
				", shopId=" + shopId +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
