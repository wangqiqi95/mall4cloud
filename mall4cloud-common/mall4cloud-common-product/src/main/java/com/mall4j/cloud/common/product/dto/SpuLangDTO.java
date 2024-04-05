package com.mall4j.cloud.common.product.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 商品-国际化表DTO
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public class SpuLangDTO{
    private static final long serialVersionUID = 1L;

	public SpuLangDTO() {
	}

	public SpuLangDTO(Integer lang, String spuName, String sellingPoint) {
		this.lang = lang;
		this.spuName = spuName;
		this.sellingPoint = sellingPoint;
	}

	@ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("语言 1.中文 2.英文")
    private Integer lang;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("简要描述,卖点等")
    private String sellingPoint;

    @ApiModelProperty("详细描述")
    private String detail;

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	public String getSellingPoint() {
		return sellingPoint;
	}

	public void setSellingPoint(String sellingPoint) {
		this.sellingPoint = sellingPoint;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		return "SpuLangDTO{" +
				"spuId=" + spuId +
				",lang=" + lang +
				",spuName=" + spuName +
				",sellingPoint=" + sellingPoint +
				",detail=" + detail +
				'}';
	}
}
