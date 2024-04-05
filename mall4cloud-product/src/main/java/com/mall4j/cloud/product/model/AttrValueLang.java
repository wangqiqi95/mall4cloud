package com.mall4j.cloud.product.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 属性值-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public class AttrValueLang extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 属性值id
     */
    private Long attrValueId;

    /**
     * 语言 0.通用 1.中文 2.英文
     */
    private Integer lang;

    /**
     * 属性值
     */
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
		return "AttrValueLang{" +
				"attrValueId=" + attrValueId +
				",lang=" + lang +
				",value=" + value +
				'}';
	}
}
