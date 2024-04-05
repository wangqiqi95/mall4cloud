package com.mall4j.cloud.common.product.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 属性值信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public class AttrValueVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("属性id")
    private Long attrValueId;

    @ApiModelProperty("属性ID")
    private Long attrId;

	@ApiModelProperty("属性值")
	private String value;

	@ApiModelProperty("属性值图片")
	private String imgUrl;
    @ApiModelProperty("属性值多语言")
    private List<AttrValueLangVO> values;

	public Long getAttrValueId() {
		return attrValueId;
	}

	public void setAttrValueId(Long attrValueId) {
		this.attrValueId = attrValueId;
	}

	public Long getAttrId() {
		return attrId;
	}

	public void setAttrId(Long attrId) {
		this.attrId = attrId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<AttrValueLangVO> getValues() {
		return values;
	}

	public void setValues(List<AttrValueLangVO> values) {
		this.values = values;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	@Override
	public String toString() {
		return "AttrValueVO{" +
				"attrValueId=" + attrValueId +
				",attrId=" + attrId +
				",value=" + value +
				",values=" + values +
				'}';
	}
}
