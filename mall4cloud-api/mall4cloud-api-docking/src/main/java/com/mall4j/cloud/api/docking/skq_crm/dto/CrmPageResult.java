package com.mall4j.cloud.api.docking.skq_crm.dto;

import io.swagger.annotations.ApiModelProperty;

public class CrmPageResult<T> extends CrmResult<T> {

	@ApiModelProperty(value = "总条数")
	private long total_count;

	public long getTotal_count() {
		return total_count;
	}

	public void setTotal_count(long total_count) {
		this.total_count = total_count;
	}

	@Override
	public String toString() {
		return super.toString() + ",CrmPageResult{" + "total_count=" + total_count + '}';
	}
}
