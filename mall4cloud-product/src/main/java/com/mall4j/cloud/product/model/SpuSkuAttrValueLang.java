package com.mall4j.cloud.product.model;

import java.io.Serializable;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 商品sku属性值-国际化表
 *
 * @author YXF
 * @date 2021-04-09 17:30:44
 */
public class SpuSkuAttrValueLang extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long spuSkuAttrId;

    /**
     * 语言 0.通用 1.中文 2.英文
     */
    private Integer lang;

    /**
     * 销售属性名称
     */
    private String attrName;

    /**
     * 销售属性值
     */
    private String attrValueName;

	public Long getSpuSkuAttrId() {
		return spuSkuAttrId;
	}

	public void setSpuSkuAttrId(Long spuSkuAttrId) {
		this.spuSkuAttrId = spuSkuAttrId;
	}

	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrValueName() {
		return attrValueName;
	}

	public void setAttrValueName(String attrValueName) {
		this.attrValueName = attrValueName;
	}

	@Override
	public String toString() {
		return "SpuSkuAttrValueLang{" +
				"spuSkuAttrId=" + spuSkuAttrId +
				", lang=" + lang +
				", attrName='" + attrName + '\'' +
				", attrValueName='" + attrValueName + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
