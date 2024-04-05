package com.mall4j.cloud.common.product.vo.app;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品规格属性关联信息VO
 *
 * @author FrozenWatermelon
 * @date 2021-03-03 09:00:00
 */
public class SpuAttrValueAppVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("规格属性名称")
    private String attrName;

	@ApiModelProperty("规格属性值名称")
	private String attrValueName;

	@ApiModelProperty("规格属性描述")
	private String attrDesc;

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

	public String getAttrDesc() {
		return attrDesc;
	}

	public void setAttrDesc(String attrDesc) {
		this.attrDesc = attrDesc;
	}

	@Override
	public String toString() {
		return "SpuAttrValueAppVO{" +
				"attrName='" + attrName + '\'' +
				", attrValueName='" + attrValueName + '\'' +
				", attrDesc='" + attrDesc + '\'' +
				'}';
	}
}
