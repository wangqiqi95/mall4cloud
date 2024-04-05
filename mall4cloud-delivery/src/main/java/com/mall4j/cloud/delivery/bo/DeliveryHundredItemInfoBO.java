package com.mall4j.cloud.delivery.bo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 订单快递信息项VO
 *
 * @author lhd
 * @date 2020-05-18 15:10:00
 */
public class DeliveryHundredItemInfoBO {

	@ApiModelProperty(value = "接受站点", required = true)
	private String context;
	@ApiModelProperty(value = "接受时间", required = true)
	private String ftime;

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getFtime() {
		return ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}

	@Override
	public String toString() {
		return "DeliveryHundredItemInfoBO{" +
				"context='" + context + '\'' +
				", ftime='" + ftime + '\'' +
				'}';
	}
}
