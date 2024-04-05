package com.mall4j.cloud.common.product.vo.app;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.product.vo.BrandLangVO;
import com.mall4j.cloud.common.product.vo.CategoryVO;
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
public class BrandAppVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("brand_id")
    private Long brandId;

    @ApiModelProperty("品牌名称")
    private String name;

    @ApiModelProperty("品牌logo图片")
	@JsonSerialize(using = ImgJsonSerializer.class)
    private String imgUrl;

    @ApiModelProperty("检索首字母")
    private String firstLetter;

	@ApiModelProperty("商品列表")
	private List<SpuSearchVO> spuList;

	/**
	 * 品牌多语言列表
	 */
	private List<BrandLangVO> langList ;


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

	public List<SpuSearchVO> getSpuList() {
		return spuList;
	}

	public void setSpuList(List<SpuSearchVO> spuList) {
		this.spuList = spuList;
	}

	public List<BrandLangVO> getLangList() {
		return langList;
	}

	public void setLangList(List<BrandLangVO> langList) {
		this.langList = langList;
	}

	@Override
	public String toString() {
		return "BrandAppVO{" +
				"brandId=" + brandId +
				", name='" + name + '\'' +
				", imgUrl='" + imgUrl + '\'' +
				", firstLetter='" + firstLetter + '\'' +
				", spuList=" + spuList +
				", langList=" + langList +
				'}';
	}
}
