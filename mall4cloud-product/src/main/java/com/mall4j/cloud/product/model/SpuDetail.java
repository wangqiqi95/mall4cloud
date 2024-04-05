package com.mall4j.cloud.product.model;

import com.mall4j.cloud.common.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品详情信息
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Data
public class SpuDetail extends BaseModel implements Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * 商品id
	 */
	private Long spuId;

	/**
	 * 语言 0.通用 1.中文 2.英文
	 */
	private Integer lang;

	/**
	 * 商品详情
	 */
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

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		return "SpuDetail{" +
				"spuId=" + spuId +
				"lang=" + lang +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",detail=" + detail +
				'}';
	}
}
