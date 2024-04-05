package com.mall4j.cloud.common.product.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * sku-国际化表DTO
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public class SkuLangDTO{
    private static final long serialVersionUID = 1L;

	public SkuLangDTO() {
	}

	public SkuLangDTO(Integer lang, String skuName, String attrs) {
		this.lang = lang;
		this.skuName = skuName;
		this.attrs = attrs;
	}

	@ApiModelProperty("sku id")
    private Long skuId;

    @ApiModelProperty("语言 1.中文 2.英文")
    private Integer lang;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("多个销售属性值id逗号分隔")
    private String attrs;

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	@Override
	public String toString() {
		return "SkuLang{" +
				"skuId=" + skuId +
				", lang=" + lang +
				", skuName='" + skuName + '\'' +
				", attrs='" + attrs + '\'' +
				'}';
	}
}
