package com.mall4j.cloud.product.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分类-国际化表DTO
 *
 * @author YXF
 * @date 2021-04-22 17:48:16
 */
public class CategoryLangDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("语言 0.通用 1.中文 2.英文")
    private Integer lang;

    @ApiModelProperty("分类名称")
    private String name;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
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
		return "CategoryLangDTO{" +
				"categoryId=" + categoryId +
				",lang=" + lang +
				",name=" + name +
				'}';
	}
}
