package com.mall4j.cloud.product.dto;

import com.mall4j.cloud.common.product.dto.AttrLangDTO;
import com.mall4j.cloud.common.product.dto.SpuFilterPropertiesDTO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 属性信息DTO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:23
 */
public class AttrDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("attr id")
    private Long attrId;

	@ApiModelProperty("店铺id")
	private Long shopId;

    @ApiModelProperty("多语言列表")
    private List<AttrLangDTO> attrLangList;

	@ApiModelProperty("属性描述")
    private String desc;

	@ApiModelProperty("作为搜索参数 0:不需要，1:需要")
	private Integer searchType;

	@ApiModelProperty("属性类型 0:销售属性，1:基本属性")
	private Integer attrType;

	@ApiModelProperty("权重(数值越大排序靠前)")
	private Integer weight;

	@ApiModelProperty("分类id列表")
	private List<Long> categoryIds;

    @ApiModelProperty("属性值列表")
	private List<AttrValueDTO> attrValues;

	@ApiModelProperty("属性名称")
	private String name;
	private String nameZh;

	@ApiModelProperty("中台分类及性别信息")
	private SpuFilterPropertiesDTO SpuFilterProperties;

	public Long getAttrId() {
		return attrId;
	}

	public void setAttrId(Long attrId) {
		this.attrId = attrId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public List<AttrLangDTO> getAttrLangList() {
		return attrLangList;
	}

	public void setAttrLangList(List<AttrLangDTO> attrLangList) {
		this.attrLangList = attrLangList;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getAttrType() {
		return attrType;
	}

	public void setAttrType(Integer attrType) {
		this.attrType = attrType;
	}

	public Integer getSearchType() {
		return searchType;
	}

	public void setSearchType(Integer searchType) {
		this.searchType = searchType;
	}

	public List<AttrValueDTO> getAttrValues() {
		return attrValues;
	}

	public void setAttrValues(List<AttrValueDTO> attrValues) {
		this.attrValues = attrValues;
	}

	public List<Long> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameZh() {
		return nameZh;
	}

	public void setNameZh(String nameZh) {
		this.nameZh = nameZh;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public SpuFilterPropertiesDTO getSpuFilterProperties() {
		return SpuFilterProperties;
	}

	public void setSpuFilterProperties(SpuFilterPropertiesDTO spuFilterProperties) {
		SpuFilterProperties = spuFilterProperties;
	}

	@Override
	public String toString() {
		return "AttrDTO{" +
				"attrId=" + attrId +
				", shopId='" + shopId + '\'' +
				", attrLangList='" + attrLangList + '\'' +
				", desc='" + desc + '\'' +
				", attrType=" + attrType +
				", searchType=" + searchType +
				", categoryIds=" + categoryIds +
				", attrValues=" + attrValues +
				", name=" + name +
				'}';
	}
}
