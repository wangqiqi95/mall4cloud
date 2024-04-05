package com.mall4j.cloud.common.product.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品-国际化表VO
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public class SpuLangVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("语言 1.中文 2.英文")
    private Integer lang;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("简要描述,卖点等")
    private String sellingPoint;

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	public String getSellingPoint() {
		return sellingPoint;
	}

	public void setSellingPoint(String sellingPoint) {
		this.sellingPoint = sellingPoint;
	}

	@Override
	public String toString() {
		return "SpuLangVO{" +
				"spuId=" + spuId +
				",lang=" + lang +
				",spuName=" + spuName +
				",sellingPoint=" + sellingPoint +
				'}';
	}
}
