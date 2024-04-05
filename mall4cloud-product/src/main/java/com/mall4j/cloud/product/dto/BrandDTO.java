package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 品牌信息DTO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public class BrandDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("brand_id")
    private Long brandId;

    @ApiModelProperty("品牌多语言列表")
    private List<BrandLangDTO> brandLangList;

    @ApiModelProperty("品牌名称")
    private String name;

    @ApiModelProperty("品牌描述")
    private String desc;

	@NotNull(message = "logo图片不能为空")
    @ApiModelProperty("品牌logo图片")
    private String imgUrl;

	@NotNull(message = "首字母不能为空")
    @ApiModelProperty("检索首字母")
    private String firstLetter;

	@NotNull(message = "序号不能为空")
    @ApiModelProperty("排序")
    private Integer seq;

    @ApiModelProperty("状态 1:enable, 0:disable, -1:deleted")
    private Integer status;

	@ApiModelProperty("分类")
	private List<Long> categoryIds;

	@ApiModelProperty("是否置顶 0：不置顶  1：置顶")
	private Integer isTop;

	@ApiModelProperty("品牌下的商品数量")
	private Integer spuCount;

	@ApiModelProperty("店铺id")
	private Long shopId;

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

	public List<BrandLangDTO> getBrandLangList() {
		return brandLangList;
	}

	public void setBrandLangList(List<BrandLangDTO> brandLangList) {
		this.brandLangList = brandLangList;
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

	public List<Long> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Long> categoryIds) {
		this.categoryIds = categoryIds;
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

	@Override
	public String toString() {
		return "BrandDTO{" +
				"brandId=" + brandId +
				", brandLangList=" + brandLangList +
				", name='" + name + '\'' +
				", desc='" + desc + '\'' +
				", imgUrl='" + imgUrl + '\'' +
				", firstLetter='" + firstLetter + '\'' +
				", seq=" + seq +
				", status=" + status +
				", categoryIds=" + categoryIds +
				", isTop=" + isTop +
				", spuCount=" + spuCount +
				", shopId=" + shopId +
				'}';
	}
}
