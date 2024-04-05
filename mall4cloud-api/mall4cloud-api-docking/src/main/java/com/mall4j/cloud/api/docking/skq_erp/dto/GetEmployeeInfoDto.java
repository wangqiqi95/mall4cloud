package com.mall4j.cloud.api.docking.skq_erp.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 类描述：获取员工信息参数
 *
 * @date 2022/1/9 10:50：38
 */
public class GetEmployeeInfoDto implements Serializable {

	private static final long serialVersionUID = -8706641469311060706L;
	@ApiModelProperty(value = "页码")
	private Integer page;

	@ApiModelProperty(value = "每页数量")
	private Integer pageSize;

	@ApiModelProperty(value = "查询开始时间" + "格式：yyyy-MM-dd hh:mm:ss")
	private String beginTime;

	@ApiModelProperty(value = "查询结束时间," + "格式：yyyy-MM-dd hh:mm:ss")
	private String endTime;

	@ApiModelProperty(value = "员工编码")
	private String eCode;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String geteCode() {
		return eCode;
	}

	public void seteCode(String eCode) {
		this.eCode = eCode;
	}

	@Override
	public String toString() {
		return "GetShopInfoDto{" + "page=" + page + ", pageSize=" + pageSize + ", beginTime='" + beginTime + '\'' + ", endTime='" + endTime + '\''
				+ ", eCode='" + eCode + '\'' + '}';
	}
}
