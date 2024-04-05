package com.mall4j.cloud.product.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 商品浏览记录表VO
 *
 * @author YXF
 * @date 2021-03-19 14:28:14
 */
public class SpuBrowseLogVO extends BaseVO{

	@ApiModelProperty("商品浏览记录id")
	private Long spuBrowseLogId;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty("商品浏览记录id")
	private Date browseTime;

	@ApiModelProperty("用户id")
	private Long userId;

	@ApiModelProperty("商品id")
	private Long spuId;

	@ApiModelProperty("商品名称")
	private String spuName;

	@JsonSerialize(using = ImgJsonSerializer.class)
	@ApiModelProperty("商品主图")
	private String mainImgUrl;

	@ApiModelProperty("商品价格")
	private Long priceFee;

	@ApiModelProperty("积分价格")
	private Long scoreFee;

	@ApiModelProperty("商品状态 1:上架，其他：下架")
	private Integer spuStatus;

	@ApiModelProperty("商品类型")
	private Integer spuType;

	public Long getSpuBrowseLogId() {
		return spuBrowseLogId;
	}

	public void setSpuBrowseLogId(Long spuBrowseLogId) {
		this.spuBrowseLogId = spuBrowseLogId;
	}

	public Date getBrowseTime() {
		return browseTime;
	}

	public void setBrowseTime(Date browseTime) {
		this.browseTime = browseTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getSpuId() {
		return spuId;
	}

	public void setSpuId(Long spuId) {
		this.spuId = spuId;
	}

	public String getSpuName() {
		return spuName;
	}

	public void setSpuName(String spuName) {
		this.spuName = spuName;
	}

	public String getMainImgUrl() {
		return mainImgUrl;
	}

	public void setMainImgUrl(String mainImgUrl) {
		this.mainImgUrl = mainImgUrl;
	}

	public Long getPriceFee() {
		return priceFee;
	}

	public void setPriceFee(Long priceFee) {
		this.priceFee = priceFee;
	}

	public Integer getSpuStatus() {
		return spuStatus;
	}

	public void setSpuStatus(Integer spuStatus) {
		this.spuStatus = spuStatus;
	}

	public Integer getSpuType() {
		return spuType;
	}

	public void setSpuType(Integer spuType) {
		this.spuType = spuType;
	}

	public Long getScoreFee() {
		return scoreFee;
	}

	public void setScoreFee(Long scoreFee) {
		this.scoreFee = scoreFee;
	}

	@Override
	public String toString() {
		return "SpuBrowseLogVO{" +
				"spuBrowseLogId=" + spuBrowseLogId +
				", browseTime=" + browseTime +
				", userId=" + userId +
				", spuId=" + spuId +
				", spuName='" + spuName + '\'' +
				", mainImgUrl='" + mainImgUrl + '\'' +
				", priceFee=" + priceFee +
				", spuStatus=" + spuStatus +
				", spuType=" + spuType +
				", scoreFee =" + scoreFee +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				'}';
	}
}
