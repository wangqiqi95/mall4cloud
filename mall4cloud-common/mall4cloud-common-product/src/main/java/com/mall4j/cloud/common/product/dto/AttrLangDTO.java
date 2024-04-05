package com.mall4j.cloud.common.product.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 属性-国际化表DTO
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public class AttrLangDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("属性id")
    private Long attrId;

    @ApiModelProperty("语言 0.通用 1.中文 2.英文")
    private Integer lang;

    @ApiModelProperty("属性名称")
    private String name;

    @ApiModelProperty("属性描述")
    private String desc;

	public Long getAttrId() {
		return attrId;
	}

	public void setAttrId(Long attrId) {
		this.attrId = attrId;
	}

	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
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

	@Override
	public String toString() {
		return "AttrLangDTO{" +
				"attrId=" + attrId +
				",lang=" + lang +
				",name=" + name +
				",desc=" + desc +
				'}';
	}
}
