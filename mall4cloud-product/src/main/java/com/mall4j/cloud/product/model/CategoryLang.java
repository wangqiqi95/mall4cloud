package com.mall4j.cloud.product.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分类-国际化表
 *
 * @author YXF
 * @date 2021-04-22 17:48:16
 */
public class CategoryLang extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 语言 0.通用 1.中文 2.英文
     */
    private Integer lang;

    /**
     * 分类名称
     */
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
		return "CategoryLang{" +
				"categoryId=" + categoryId +
				",lang=" + lang +
				",name=" + name +
				'}';
	}
}
