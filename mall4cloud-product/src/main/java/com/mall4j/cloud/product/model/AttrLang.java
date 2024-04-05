package com.mall4j.cloud.product.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 属性-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public class AttrLang extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 属性id
     */
    private Long attrId;

    /**
     * 语言 0.通用 1.中文 2.英文
     */
    private Integer lang;

    /**
     * 属性名称
     */
    private String name;

	/**
	 * 属性名称
	 */
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
		return "AttrLang{" +
				"attrId=" + attrId +
				",lang=" + lang +
				",name=" + name +
				",desc=" + desc +
				'}';
	}
}
