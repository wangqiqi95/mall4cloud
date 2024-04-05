package com.mall4j.cloud.api.platform.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 类描述：required = true为必填项
 *
 * @date 2022/3/20 22:48：15
 */
public class CalcingDownloadRecordDTO implements Serializable {

	private static final long serialVersionUID = 4797712950738445964L;
	@ApiModelProperty(value = "下载日期", required = true)
	private Date downloadTime;

	@ApiModelProperty("文件名称")
	private String fileName;

	@ApiModelProperty(value = "计算量", required = true)
	private Integer calCount;

	@ApiModelProperty(value = "操作人", required = true)
	private String operatorName;

	@ApiModelProperty(value = "操作人工号", required = true)
	private String operatorNo;

	public Date getDownloadTime() {
		return downloadTime;
	}

	public void setDownloadTime(Date downloadTime) {
		this.downloadTime = downloadTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getCalCount() {
		return calCount;
	}

	public void setCalCount(Integer calCount) {
		this.calCount = calCount;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorNo() {
		return operatorNo;
	}

	public void setOperatorNo(String operatorNo) {
		this.operatorNo = operatorNo;
	}

	@Override
	public String toString() {
		return "CalcingDownloadRecordDTO{" + "downloadTime=" + downloadTime + ", fileName='" + fileName + '\'' + ", calCount=" + calCount + ", operatorName='"
				+ operatorName + '\'' + ", operatorNo='" + operatorNo + '\'' + '}';
	}
}
