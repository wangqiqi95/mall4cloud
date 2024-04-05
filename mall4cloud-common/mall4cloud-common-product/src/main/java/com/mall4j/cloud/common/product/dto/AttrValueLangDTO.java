package com.mall4j.cloud.common.product.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 属性值-国际化表DTO
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public class AttrValueLangDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("属性值id")
    private Long attrValueId;

    @ApiModelProperty("语言 0.通用 1.中文 2.英文")
    private Integer lang;

    @ApiModelProperty("属性值")
    private String value;

	public Long getAttrValueId() {
		return attrValueId;
	}

	public void setAttrValueId(Long attrValueId) {
		this.attrValueId = attrValueId;
	}

	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "AttrValueLangDTO{" +
				"attrValueId=" + attrValueId +
				",lang=" + lang +
				",value=" + value +
				'}';
	}
}
