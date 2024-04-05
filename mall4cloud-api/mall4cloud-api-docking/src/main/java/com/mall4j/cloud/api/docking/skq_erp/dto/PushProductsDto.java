package com.mall4j.cloud.api.docking.skq_erp.dto;

import com.mall4j.cloud.api.docking.skq_erp.config.IStdDataCheck;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：推退单到中台
 *
 * @date 2022/1/7 13:59：43
 */
public class PushProductsDto implements Serializable, IStdDataCheck {

	private static final long serialVersionUID = 6573988171542758902L;

	@ApiModelProperty(value = "商品编码", required = true)
	private String psCProEcode;

	@ApiModelProperty(value = "上架状态，0：未上架，1：已上架 传1", required = true)
	private Integer approveStatus;

	@ApiModelProperty(value = "对接平台方 XXY", required = true)
	private String platform;

	public String getPsCProEcode() {
		return psCProEcode;
	}

	public void setPsCProEcode(String psCProEcode) {
		this.psCProEcode = psCProEcode;
	}

	public Integer getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Integer approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@Override
	public String toString() {
		return "PushProductDto{" +
				"psCProEcode='" + psCProEcode + '\'' +
				", approveStatus='" + approveStatus + '\'' +
				", platform='" + platform + '\'' +
				'}';
	}

	@Override
	public ServerResponseEntity check() {
		return ServerResponseEntity.success();
	}
}
