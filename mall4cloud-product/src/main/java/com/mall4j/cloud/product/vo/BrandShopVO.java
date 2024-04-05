package com.mall4j.cloud.product.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 品牌店铺关联信息VO
 *
 * @author FrozenWatermelon
 * @date 2021-05-08 13:31:45
 */
public class BrandShopVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private Long brandShopId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("品牌id")
    private Long brandId;

    @ApiModelProperty("授权资质图片，以,分割")
    private String qualifications;

    @ApiModelProperty("类型 0：平台品牌，1：店铺自定义品牌")
    private Integer type;

    @ApiModelProperty("索引首字母")
    private String firstLetter;

    @ApiModelProperty("logo")
    private String imgUrl;

    @ApiModelProperty("品牌名称")
    private String name;

    @ApiModelProperty("品牌状态")
	private Integer brandStatus;

	public Integer getBrandStatus() {
		return brandStatus;
	}

	public void setBrandStatus(Integer brandStatus) {
		this.brandStatus = brandStatus;
	}

	public String getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}

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

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "BrandShopVO{" +
				"brandShopId=" + brandShopId +
				", shopId=" + shopId +
				", brandId=" + brandId +
				", qualifications='" + qualifications + '\'' +
				", type=" + type +
				", firstLetter='" + firstLetter + '\'' +
				", imgUrl='" + imgUrl + '\'' +
				", name='" + name + '\'' +
				", brandStatus=" + brandStatus +
				'}';
	}
}
