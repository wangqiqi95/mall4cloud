package com.mall4j.cloud.platform.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@TableName("download_center")
public class DownloadCenter extends BaseModel implements Serializable {

	private static final long serialVersionUID = -6748555379106098462L;
	@ApiModelProperty("id")
	@TableId(type = IdType.AUTO)
	private Long id;

	@ApiModelProperty("下载日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date downloadTime;

	@ApiModelProperty("文件名称")
	private String fileName;

	@ApiModelProperty("文件下载地址")
	private String fileUrl;

	@ApiModelProperty("备注")
	private String remarks;

	@ApiModelProperty("计算量")
	private Integer calCount;

	@ApiModelProperty("状态（0：计算中/1：成功/2：失败）")
	private Integer status;

	@ApiModelProperty("状态（0：计算中/1：成功/2：失败）")
	private Integer delStatus;

	@ApiModelProperty("操作人")
	private String operatorName;

	@ApiModelProperty("操作人工号")
	private String operatorNo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Integer getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "DownloadCenter{" + "id=" + id + ", downloadTime=" + downloadTime + ", fileName='" + fileName + '\'' + ", fileUrl='" + fileUrl + '\''
				+ ", calCount=" + calCount + ", status=" + status + ", delStatus=" + delStatus + ", operatorName='" + operatorName + '\'' + ", operatorNo='" + operatorNo + '\''
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + '}';
	}

	public enum DownLoadStatus {
		Calcing(0, "计算中"),
		Success(1, "成功"),
		Fail(2, "失败");

		private Integer value;
		private String desc;

		DownLoadStatus(Integer value, String desc) {
			this.value = value;
			this.desc = desc;
		}

		public Integer value() {
			return value;
		}

		public String desc() {
			return desc;
		}

		public static Optional<DownLoadStatus> getStatus(Integer value) {
			return Arrays.stream(values()).filter(v -> v.value() == value).findFirst();
		}
	}
}
