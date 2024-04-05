package com.mall4j.cloud.common.product.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 商品详情信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public class SpuDetailVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("语言 0.通用 1.中文 2.英文")
    private Integer lang;

    @ApiModelProperty("商品详情")
    private String detail;


	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		return "SpuDetailVO{" +
				",lang=" + lang +
				",detail=" + detail +
				'}';
	}
}
