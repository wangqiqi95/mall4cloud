package com.mall4j.cloud.api.delivery.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 订单快递信息项VO
 *
 * @author lhd
 * @date 2020-05-18 15:10:00
 */
public class DeliveryItemInfoVO {

	@ApiModelProperty(value = "接受站点", required = true)
	private String AcceptStation;
	@ApiModelProperty(value = "接受时间", required = true)
	private String AcceptTime;

	public String getAcceptStation() {
		return AcceptStation;
	}

	public void setAcceptStation(String acceptStation) {
		AcceptStation = acceptStation;
	}

	public String getAcceptTime() {
		return AcceptTime;
	}

	public void setAcceptTime(String acceptTime) {
		AcceptTime = acceptTime;
	}

	@Override
	public String toString() {
		return "DeliveryInfoVO{" +
				"AcceptStation='" + AcceptStation + '\'' +
				", AcceptTime='" + AcceptTime + '\'' +
				'}';
	}
}
