package com.mall4j.cloud.common.product.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.product.vo.app.CategoryAppVO;
import com.mall4j.cloud.common.product.vo.search.SpuSearchVO;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 品牌信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public class BrandVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("brand_id")
    private Long brandId;

	@ApiModelProperty("品牌多语言列表")
	private List<BrandLangVO> brandLangList;

    @ApiModelProperty("品牌名称")
    private String name;

    @ApiModelProperty("品牌描述")
    private String desc;

    @ApiModelProperty("品牌logo图片")
	@JsonSerialize(using = ImgJsonSerializer.class)
    private String imgUrl;

    @ApiModelProperty("检索首字母")
    private String firstLetter;

    @ApiModelProperty("排序")
    private Integer seq;

    @ApiModelProperty("状态 1:enable, 0:disable, -1:deleted")
    private Integer status;

	@ApiModelProperty("分类")
	private List<CategoryVO> categories;

	@ApiModelProperty("是否置顶 0：不置顶  1：置顶")
	private Integer isTop;

	@ApiModelProperty("品牌下的商品数量")
	private Integer spuCount;

	@ApiModelProperty("商品列表")
	private List<SpuSearchVO> spuList;

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public List<BrandLangVO> getBrandLangList() {
		return brandLangList;
	}

	public void setBrandLangList(List<BrandLangVO> brandLangList) {
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

	public List<CategoryVO> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryVO> categories) {
		this.categories = categories;
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

	public List<SpuSearchVO> getSpuList() {
		return spuList;
	}

	public void setSpuList(List<SpuSearchVO> spuList) {
		this.spuList = spuList;
	}

	@Override
	public String toString() {
		return "BrandVO{" +
				"brandId=" + brandId +
				", brandLangList=" + brandLangList +
				", name='" + name + '\'' +
				", desc='" + desc + '\'' +
				", imgUrl='" + imgUrl + '\'' +
				", firstLetter='" + firstLetter + '\'' +
				", seq=" + seq +
				", status=" + status +
				", categories=" + categories +
				", isTop=" + isTop +
				", spuCount=" + spuCount +
				", spuList=" + spuList +
				'}';
	}
}
