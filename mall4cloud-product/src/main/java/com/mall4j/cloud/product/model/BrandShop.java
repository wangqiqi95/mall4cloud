package com.mall4j.cloud.product.model;

import java.io.Serializable;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 品牌店铺关联信息
 *
 * @author FrozenWatermelon
 * @date 2021-05-08 13:31:45
 */
public class BrandShop extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long brandShopId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 品牌id
     */
    private Long brandId;

    /**
     * 授权资质图片，以,分割
     */
    private String qualifications;

    /**
     * 类型 0：平台品牌，1：店铺自定义品牌
     */
    private Integer type;

	public Long getBrandShopId() {
		return brandShopId;
	}

	public void setBrandShopId(Long brandShopId) {
		this.brandShopId = brandShopId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public String getQualifications() {
		return qualifications;
	}

	public void setQualifications(String qualifications) {
		this.qualifications = qualifications;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "BrandShop{" +
				"brandShopId=" + brandShopId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",shopId=" + shopId +
				",brandId=" + brandId +
				",qualifications=" + qualifications +
				",type=" + type +
				'}';
	}
}
