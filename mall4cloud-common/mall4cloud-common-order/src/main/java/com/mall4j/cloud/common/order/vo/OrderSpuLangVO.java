package com.mall4j.cloud.common.order.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 订单sku-国际化表VO
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public class OrderSpuLangVO {

    @ApiModelProperty("语言 1.中文 2.英文")
    private Integer lang;

	@ApiModelProperty("sku名称")
	private String spuName;

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

	@Override
	public String toString() {
		return "OrderSpuLangVO{" +
				"lang=" + lang +
				", spuName='" + spuName + '\'' +
				'}';
	}
}
