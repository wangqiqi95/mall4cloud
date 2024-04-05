package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 微信触点门店二维码表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-03-09 16:05:09
 */
public class WeixinQrcodeTentacleStoreDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("触点id")
    private String tentacleId;

    @ApiModelProperty("门店id")
    private String storeId;

    @ApiModelProperty("跳转路径")
    private String path;

    @ApiModelProperty("二维码尺寸")
    private Integer codeWidth;

    @ApiModelProperty("二维码路径")
    private String qrcodePath;

    @ApiModelProperty("状态： 0可用 1不可用")
    private Integer status;

    @ApiModelProperty("删除标识0-正常,1-已删除")
    private Integer delFlag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTentacleId() {
		return tentacleId;
	}

	public void setTentacleId(String tentacleId) {
		this.tentacleId = tentacleId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getCodeWidth() {
		return codeWidth;
	}

	public void setCodeWidth(Integer codeWidth) {
		this.codeWidth = codeWidth;
	}

	public String getQrcodePath() {
		return qrcodePath;
	}

	public void setQrcodePath(String qrcodePath) {
		this.qrcodePath = qrcodePath;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		return "WeixinQrcodeTentacleStoreDTO{" +
				"id=" + id +
				",tentacleId=" + tentacleId +
				",storeId=" + storeId +
				",path=" + path +
				",codeWidth=" + codeWidth +
				",qrcodePath=" + qrcodePath +
				",status=" + status +
				",delFlag=" + delFlag +
				'}';
	}
}
