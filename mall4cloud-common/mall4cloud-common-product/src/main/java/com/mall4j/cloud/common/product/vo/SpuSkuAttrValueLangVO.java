package com.mall4j.cloud.common.product.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品sku属性值-国际化表VO
 *
 * @author YXF
 * @date 2021-04-09 17:30:44
 */
public class SpuSkuAttrValueLangVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("语言 0.通用 1.中文 2.英文")
    private Integer lang;

	@ApiModelProperty("销售属性名称")
	private String attrName;

	@ApiModelProperty("销售属性值ID")
	private Integer attrValueId;

	@ApiModelProperty("销售属性值")
	private String attrValueName;

	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public Integer getAttrValueId() {
		return attrValueId;
	}

	public void setAttrValueId(Integer attrValueId) {
		this.attrValueId = attrValueId;
	}

	public String getAttrValueName() {
		return attrValueName;
	}

	public void setAttrValueName(String attrValueName) {
		this.attrValueName = attrValueName;
	}

	@Override
	public String toString() {
		return "SpuSkuAttrValueLangVO{" +
				"lang=" + lang +
				", attrName='" + attrName + '\'' +
				", attrValueId=" + attrValueId +
				", attrValueName='" + attrValueName + '\'' +
				'}';
	}
}
