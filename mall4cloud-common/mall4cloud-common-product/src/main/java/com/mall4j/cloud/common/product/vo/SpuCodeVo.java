package com.mall4j.cloud.common.product.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class SpuCodeVo extends BaseVO implements Serializable {

	private static final long serialVersionUID = -1461519729674410384L;
	@ApiModelProperty("spu id")
	private Long spuId;

	@ApiModelProperty("spu code")
	private String spuCode;

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public String getSpuCode() {
		return spuCode;
	}

	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}
}
