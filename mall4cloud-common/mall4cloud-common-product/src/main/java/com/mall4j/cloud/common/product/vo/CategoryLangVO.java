package com.mall4j.cloud.common.product.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 分类-国际化表VO
 *
 * @author YXF
 * @date 2021-04-22 17:48:16
 */
public class CategoryLangVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("语言 0.通用 1.中文 2.英文")
    private Integer lang;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("父级分类名称")
	private String parentName;

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
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

	@Override
	public String toString() {
		return "CategoryLangVO{" +
				"lang=" + lang +
				", name='" + name + '\'' +
				", parentName='" + parentName + '\'' +
				'}';
	}
}
