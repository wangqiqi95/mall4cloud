package com.mall4j.cloud.product.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 商品-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public class SpuLang extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

	/**
     * 商品id
     */
    private Long spuId;

    /**
     * 语言 1.中文 2.英文
     */
    private Integer lang;

    /**
     * 商品名称
     */
    private String spuName;

    /**
     * 简要描述,卖点等
     */
    private String sellingPoint;

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

	@Override
	public String toString() {
		return "SpuLang{" +
				"spuId=" + spuId +
				",lang=" + lang +
				",spuName=" + spuName +
				",sellingPoint=" + sellingPoint +
				'}';
	}
}
