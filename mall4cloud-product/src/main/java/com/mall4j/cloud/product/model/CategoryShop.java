package com.mall4j.cloud.product.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 品牌分类关联信息
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 16:40:44
 */
public class CategoryShop extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long categoryShopId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 扣率: 为空代表采用平台扣率
     */
    private Double rate;

    /**
     * 经营资质图片，以,分割
     */
    private String qualifications;

	public Long getCategoryShopId() {
		return categoryShopId;
	}

	public void setCategoryShopId(Long categoryShopId) {
		this.categoryShopId = categoryShopId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getQualifications() {
		return qualifications;
	}

	public void setQualifications(String qualifications) {
		this.qualifications = qualifications;
	}

	@Override
	public String toString() {
		return "CategoryShop{" +
				"categoryShopId=" + categoryShopId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",shopId=" + shopId +
				",categoryId=" + categoryId +
				",rate=" + rate +
				",qualifications=" + qualifications +
				'}';
	}
}
