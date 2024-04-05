package com.mall4j.cloud.api.product.dto;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 品牌店铺关联信息DTO
 *
 * @author FrozenWatermelon
 * @date 2021-05-08 13:31:45
 */
public class BrandShopDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Long brandShopId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("品牌id")
    private Long brandId;

    @ApiModelProperty("授权资质图片，以,分割")
	@NotBlank(message = "授权资质图片不能为空")
    private String qualifications;

    @ApiModelProperty("类型 0：平台品牌，1：店铺自定义品牌")
    private Integer type;

	@ApiModelProperty("检索首字母")
	@Length(max = 1, message = "检索首字母长度不能超过1")
	private String firstLetter;

	@ApiModelProperty("排序")
	private Integer seq;

	@ApiModelProperty("是否置顶 0：不置顶  1：置顶")
	private Integer isTop;

    @ApiModelProperty("logo")
    private String imgUrl;

    @ApiModelProperty("品牌名称")
    private String name;

    @ApiModelProperty("品牌描述")
	private String desc;

    @ApiModelProperty("品牌状态")
	private Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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
		return "BrandShopDTO{" +
				"brandShopId=" + brandShopId +
				", shopId=" + shopId +
				", brandId=" + brandId +
				", qualifications='" + qualifications + '\'' +
				", type=" + type +
				", firstLetter='" + firstLetter + '\'' +
				", seq=" + seq +
				", isTop=" + isTop +
				", imgUrl='" + imgUrl + '\'' +
				", name='" + name + '\'' +
				", desc='" + desc + '\'' +
				", status='" + status + '\'' +
				'}';
	}
}
