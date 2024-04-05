package com.mall4j.cloud.common.product.vo;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * 商品规格属性关联信息VO
 *
 * @author FrozenWatermelon
 * @date 2021-03-03 09:00:00
 */
public class SpuAttrValueVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品属性值关联信息id")
    private Long spuAttrValueId;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("规格属性id")
    private Long attrId;

    @ApiModelProperty("规格属性名称")
    private String attrName;

    @ApiModelProperty("规格属性值id")
    private Long attrValueId;

	@ApiModelProperty("规格属性值名称")
	private String attrValueName;

	@ApiModelProperty("搜索类型 0:不需要，1:需要")
	private Integer searchType;

	@ApiModelProperty("规格属性描述")
	private String attrDesc;

	@ApiModelProperty("商品属性多语言列表")
	private List<SpuAttrValueLangVO> spuAttrValueLangList;

	public Long getSpuAttrValueId() {
		return spuAttrValueId;
	}

	public void setSpuAttrValueId(Long spuAttrValueId) {
		this.spuAttrValueId = spuAttrValueId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Long getAttrId() {
		return attrId;
	}

	public void setAttrId(Long attrId) {
		this.attrId = attrId;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public Long getAttrValueId() {
		return attrValueId;
	}

	public void setAttrValueId(Long attrValueId) {
		this.attrValueId = attrValueId;
	}

	public String getAttrValueName() {
		return attrValueName;
	}

	public void setAttrValueName(String attrValueName) {
		this.attrValueName = attrValueName;
	}

	public Integer getSearchType() {
		return searchType;
	}

	public void setSearchType(Integer searchType) {
		this.searchType = searchType;
	}

	public String getAttrDesc() {
		return attrDesc;
	}

	public void setAttrDesc(String attrDesc) {
		this.attrDesc = attrDesc;
	}

	public List<SpuAttrValueLangVO> getSpuAttrValueLangList() {
		return spuAttrValueLangList;
	}

	public void setSpuAttrValueLangList(List<SpuAttrValueLangVO> spuAttrValueLangList) {
		this.spuAttrValueLangList = spuAttrValueLangList;
	}

	@Override
	public String toString() {
		return "SpuAttrValueVO{" +
				"spuAttrValueId=" + spuAttrValueId +
				", spuId=" + spuId +
				", attrId=" + attrId +
				", attrName='" + attrName + '\'' +
				", attrValueId=" + attrValueId +
				", attrValueName='" + attrValueName + '\'' +
				", searchType=" + searchType +
				", attrDesc='" + attrDesc + '\'' +
				", spuAttrValueLangList=" + spuAttrValueLangList +
				'}';
	}
}
