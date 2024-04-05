package com.mall4j.cloud.delivery.bo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 阿里快递信息项VO
 *
 * @author lhd
 * @date 2020-05-18 15:10:00
 */
public class DeliveryAliItemInfoBO {

	@ApiModelProperty(value = "接受站点", required = true)
	private String status;
	@ApiModelProperty(value = "接受时间", required = true)
	private String time;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "DeliveryInfoVO{" +
				"status='" + status + '\'' +
				", time='" + time + '\'' +
				'}';
	}
}
