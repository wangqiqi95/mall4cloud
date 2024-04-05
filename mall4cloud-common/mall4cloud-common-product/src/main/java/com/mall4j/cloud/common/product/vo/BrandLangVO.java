package com.mall4j.cloud.common.product.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 品牌-国际化表VO
 *
 * @author YXF
 * @date 2021-04-26 15:17:37
 */
public class BrandLangVO{

    @ApiModelProperty("品牌id")
    private Long brandId;

    @ApiModelProperty("语言 0.通用 1.中文 2.英文")
    private Integer lang;

    @ApiModelProperty("品牌名称")
    private String name;

    @ApiModelProperty("品牌描述")
    private String desc;

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
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
		return "BrandLangVO{" +
				"brandId=" + brandId +
				",lang=" + lang +
				",name=" + name +
				",desc=" + desc +
				'}';
	}
}
