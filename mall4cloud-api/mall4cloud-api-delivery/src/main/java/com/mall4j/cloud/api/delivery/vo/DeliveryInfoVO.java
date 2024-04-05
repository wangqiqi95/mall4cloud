package com.mall4j.cloud.api.delivery.vo;


import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author FrozenWatermelon
 */
public class DeliveryInfoVO {

	@ApiModelProperty(value = "物流公司名称",required=true)
	private String companyName;

	@ApiModelProperty(value = "物流公司官网",required=true)
	private String companyHomeUrl;

	@ApiModelProperty(value = "物流订单号",required=true)
	private String dvyFlowId;

	@ApiModelProperty(value = "物流状态 0:没有记录 1:已揽收 2:运输途中 201:达到目的城市 3:已签收 4:问题件",required=true)
	private Integer state;

	@ApiModelProperty(value = "查询出的物流信息",required=true)
	private List<DeliveryItemInfoVO> Traces;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyHomeUrl() {
		return companyHomeUrl;
	}

	public void setCompanyHomeUrl(String companyHomeUrl) {
		this.companyHomeUrl = companyHomeUrl;
	}

	public String getDvyFlowId() {
		return dvyFlowId;
	}

	public void setDvyFlowId(String dvyFlowId) {
		this.dvyFlowId = dvyFlowId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public List<DeliveryItemInfoVO> getTraces() {
		return Traces;
	}

	public void setTraces(List<DeliveryItemInfoVO> traces) {
		Traces = traces;
	}

	@Override
	public String toString() {
		return "DeliveryVO{" +
				"companyName='" + companyName + '\'' +
				", companyHomeUrl='" + companyHomeUrl + '\'' +
				", dvyFlowId='" + dvyFlowId + '\'' +
				", state=" + state +
				", Traces=" + Traces +
				'}';
	}
}
