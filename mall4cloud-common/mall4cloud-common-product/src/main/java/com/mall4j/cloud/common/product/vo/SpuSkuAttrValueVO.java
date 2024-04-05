package com.mall4j.cloud.common.product.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 商品sku销售属性关联信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public class SpuSkuAttrValueVO extends BaseVO {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("商品sku销售属性关联信息id")
	private Long spuSkuAttrId;

	@ApiModelProperty("SPU ID")
	private Long spuId;

	@ApiModelProperty("语言 0.通用 1.中文 2.英文")
	private Integer lang;

	@ApiModelProperty("SKU ID")
	private Long skuId;

	@ApiModelProperty("销售属性ID")
	private Long attrId;

	@ApiModelProperty("销售属性国际化信息")
	private List<SpuSkuAttrValueLangVO> spuSkuAttrValueLangList;

	@ApiModelProperty("销售属性名称")
	private String attrName;

	@ApiModelProperty("销售属性值ID")
	private Integer attrValueId;

	@ApiModelProperty("销售属性值")
	private String attrValueName;

	@ApiModelProperty("状态 1:enable, 0:disable, -1:deleted")
	private Integer status;

	public Long getSpuSkuAttrId() {
		return spuSkuAttrId;
	}

	public void setSpuSkuAttrId(Long spuSkuAttrId) {
		this.spuSkuAttrId = spuSkuAttrId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

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

	public Long getAttrId() {
		return attrId;
	}

	public void setAttrId(Long attrId) {
		this.attrId = attrId;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public Integer getAttrValueId() {
		return attrValueId;
	}

	public void setAttrValueId(Integer attrValueId) {
		this.attrValueId = attrValueId;
	}

	public String getAttrValueName() {
		return attrValueName;
	}

	public void setAttrValueName(String attrValueName) {
		this.attrValueName = attrValueName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<SpuSkuAttrValueLangVO> getSpuSkuAttrValueLangList() {
		return spuSkuAttrValueLangList;
	}

	public void setSpuSkuAttrValueLangList(List<SpuSkuAttrValueLangVO> spuSkuAttrValueLangList) {
		this.spuSkuAttrValueLangList = spuSkuAttrValueLangList;
	}

	@Override
	public String toString() {
		return "SpuSkuAttrValueVO{" +
				"spuSkuAttrId=" + spuSkuAttrId +
				", spuId=" + spuId +
				", lang=" + lang +
				", skuId=" + skuId +
				", attrId=" + attrId +
				", attrName='" + attrName + '\'' +
				", attrValueId=" + attrValueId +
				", attrValueName='" + attrValueName + '\'' +
				", status=" + status +
				", spuSkuAttrValueLangList=" + spuSkuAttrValueLangList +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
