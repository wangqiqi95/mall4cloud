package com.mall4j.cloud.api.docking.skq_erp.dto;

import com.mall4j.cloud.api.docking.skq_erp.config.IStdDataCheck;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 类描述：推退单到中台
 *
 * @date 2022/1/7 13:59：43
 */
public class PushStoreDto implements Serializable, IStdDataCheck {

	private static final long serialVersionUID = 6573988171542758902L;

	@ApiModelProperty(value = "店铺/逻辑仓 编码", required = true)
	private String code;

	@ApiModelProperty(value = "类型 1门店 2逻辑仓", required = true)
	private String filterType;

	@ApiModelProperty(value = "对接平台方 XXY", required = true)
	private String platform;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFilterType() {
		return filterType;
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@Override
	public String toString() {
		return "PushStoreDto{" +
				"code='" + code + '\'' +
				", filterType='" + filterType + '\'' +
				", platform='" + platform + '\'' +
				'}';
	}

	@Override
	public ServerResponseEntity check() {
		return ServerResponseEntity.success();
	}
}
