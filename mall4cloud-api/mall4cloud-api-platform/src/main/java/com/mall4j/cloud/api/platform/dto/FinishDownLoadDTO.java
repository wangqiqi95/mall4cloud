package com.mall4j.cloud.api.platform.dto;

import io.swagger.annotations.ApiModelProperty;

public class FinishDownLoadDTO {

	@ApiModelProperty(value = "id", required = true)
	private Long id;

	@ApiModelProperty(value = "文件名称",required = true)
	private String fileName;

	@ApiModelProperty(value = "文件下载地址", required = true)
	private String fileUrl;

	@ApiModelProperty(value = "备注", required = false)
	private String remarks;

	@ApiModelProperty("计算量")
	private Integer calCount;

	@ApiModelProperty(value = "状态（0：计算中/1：成功/2：失败）", required = true)
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Integer getCalCount() {
		return calCount;
	}

	public void setCalCount(Integer calCount) {
		this.calCount = calCount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "FinishDownLoadDTO{" + "id=" + id + ", fileName='" + fileName + '\'' + ", fileUrl='" + fileUrl + '\'' + ", calCount=" + calCount + ", status="
				+ status + '}';
	}
}
