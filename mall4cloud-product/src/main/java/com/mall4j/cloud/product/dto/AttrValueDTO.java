package com.mall4j.cloud.product.dto;

import com.mall4j.cloud.common.product.dto.AttrLangDTO;
import com.mall4j.cloud.common.product.dto.AttrValueLangDTO;
import com.mall4j.cloud.product.model.AttrLang;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 属性值信息DTO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
public class AttrValueDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("属性id")
    private Long attrValueId;

    @ApiModelProperty("属性ID")
    private Long attrId;

    @ApiModelProperty("属性值国际化信息")
    private List<AttrValueLangDTO> values;

    private String name;

	@ApiModelProperty("属性值图片")
	private String imgUrl;

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

	public List<AttrValueLangDTO> getValues() {
		return values;
	}

	public void setValues(List<AttrValueLangDTO> values) {
		this.values = values;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	@Override
	public String toString() {
		return "AttrValueDTO{" +
				"attrValueId=" + attrValueId +
				",attrId=" + attrId +
				",values=" + values +
				'}';
	}
}
