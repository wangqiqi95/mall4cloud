package com.mall4j.cloud.api.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class DownloadCenterQueryDTO {

	@ApiModelProperty(value = "文件名称")
	private String fileName;
	@ApiModelProperty(value = "操作人或操作人工号")
	private String operatorNoOrName;

	@ApiModelProperty("开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startTime;
	/**
	 * 结束时间
	 */
	@ApiModelProperty("结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endTime;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOperatorNoOrName() {
		return operatorNoOrName;
	}

	public void setOperatorNoOrName(String operatorNoOrName) {
		this.operatorNoOrName = operatorNoOrName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "DownloadCenterQueryDTO{" + "fileName='" + fileName + '\'' + ", operatorNoOrName='" + operatorNoOrName + '\'' + ", startTime=" + startTime
				+ ", endTime=" + endTime + '}';
	}
}
