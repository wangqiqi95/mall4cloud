package com.mall4j.cloud.product.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * sku-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:08:38
 */
public class SkuLang extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * sku id
     */
    private Long skuId;

    /**
     * 语言 0.通用 1.中文 2.英文
     */
    private Integer lang;


    /**
     * sku名称
     */
    private String skuName;

    /**
     * 多个销售属性值id逗号分隔
     */
    private String attrs;

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getAttrs() {
		return attrs;
	}

	public void setAttrs(String attrs) {
		this.attrs = attrs;
	}

	@Override
	public String toString() {
		return "SkuLang{" +
				"skuId=" + skuId +
				", lang=" + lang +
				", skuName='" + skuName + '\'' +
				", attrs='" + attrs + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
