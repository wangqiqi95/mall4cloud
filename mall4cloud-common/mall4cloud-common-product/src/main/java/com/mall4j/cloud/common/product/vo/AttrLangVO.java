package com.mall4j.cloud.common.product.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 属性-国际化表VO
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public class AttrLangVO{
    private static final long serialVersionUID = 1L;

	@ApiModelProperty("语言 0.通用 1.中文 2.英文")
	private Integer lang;

    @ApiModelProperty("属性名称")
    private String name;

	@ApiModelProperty("属性描述")
	private String desc;

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
		return "AttrLangVO{" +
				",lang=" + lang +
				",name=" + name +
				",desc=" + desc +
				'}';
	}
}
