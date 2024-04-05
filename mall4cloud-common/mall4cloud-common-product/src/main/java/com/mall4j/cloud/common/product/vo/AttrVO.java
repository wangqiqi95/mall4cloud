package com.mall4j.cloud.common.product.vo;

import com.mall4j.cloud.common.product.dto.SpuFilterPropertiesDTO;
import com.mall4j.cloud.common.vo.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * 属性信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:23
 */
public class AttrVO extends BaseVO{
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("attr id")
	private Long attrId;

	@ApiModelProperty("店铺id")
	private Long shopId;

	@ApiModelProperty("属性多语言信息")
	private List<AttrLangVO> attrLangList;

	@ApiModelProperty("属性名称")
	private String name;

	@ApiModelProperty("属性描述")
	private String desc;

	@ApiModelProperty("作为搜索参数 0:不需要，1:需要")
	private Integer searchType;

	@ApiModelProperty("属性类型 0:销售属性，1:基本属性")
	private Integer attrType;

	@ApiModelProperty("属性值列表")
	private List<AttrValueVO> attrValues;

	@ApiModelProperty("分类列表")
	private List<CategoryVO> categories;

	@ApiModelProperty("权重(数值越大排序靠前)")
	private Integer weight;

	@ApiModelProperty("人群性别关联信息")
	private SpuFilterPropertiesDTO spuFilterProperties;

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

	public List<AttrValueVO> getAttrValues() {
		return attrValues;
	}

	public void setAttrValues(List<AttrValueVO> attrValues) {
		this.attrValues = attrValues;
	}

	public List<CategoryVO> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryVO> categories) {
		this.categories = categories;
	}

	public List<AttrLangVO> getAttrLangList() {
		return attrLangList;
	}

	public void setAttrLangList(List<AttrLangVO> attrLangList) {
		this.attrLangList = attrLangList;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public SpuFilterPropertiesDTO getSpuFilterProperties() {
		return spuFilterProperties;
	}

	public void setSpuFilterProperties(SpuFilterPropertiesDTO spuFilterProperties) {
		this.spuFilterProperties = spuFilterProperties;
	}

	@Override
	public String toString() {
		return "AttrVO{" +
				"attrId=" + attrId +
				", shopId=" + shopId +
				", name='" + name + '\'' +
				", attrLangList=" + attrLangList +
				", desc='" + desc + '\'' +
				", attrType=" + attrType +
				", searchType=" + searchType +
				", attrValues=" + attrValues +
				", categorys=" + categories +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
