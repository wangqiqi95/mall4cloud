package com.mall4j.cloud.api.delivery.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 物流公司VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
public class DeliveryCompanyVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private Long deliveryCompanyId;

    @ApiModelProperty("物流公司名称")
    private String name;

    @ApiModelProperty("公司主页")
    private String homeUrl;

    @ApiModelProperty("物流公司编号(阿里)")
    private String aliNo;

    @ApiModelProperty("物流公司编号(快递鸟)")
    private String birdNo;

    @ApiModelProperty("物流公司编号(快递100)")
    private String hundredNo;

    @ApiModelProperty("物流公司编号(其他不知名公司)")
    private String otherNo;

	public Long getDeliveryCompanyId() {
		return deliveryCompanyId;
	}

	public void setDeliveryCompanyId(Long deliveryCompanyId) {
		this.deliveryCompanyId = deliveryCompanyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHomeUrl() {
		return homeUrl;
	}

	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}

	public String getAliNo() {
		return aliNo;
	}

	public void setAliNo(String aliNo) {
		this.aliNo = aliNo;
	}

	public String getBirdNo() {
		return birdNo;
	}

	public void setBirdNo(String birdNo) {
		this.birdNo = birdNo;
	}

	public String getHundredNo() {
		return hundredNo;
	}

	public void setHundredNo(String hundredNo) {
		this.hundredNo = hundredNo;
	}

	public String getOtherNo() {
		return otherNo;
	}

	public void setOtherNo(String otherNo) {
		this.otherNo = otherNo;
	}

	@Override
	public String toString() {
		return "DeliveryCompanyVO{" +
				"deliveryCompanyId=" + deliveryCompanyId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",name=" + name +
				",homeUrl=" + homeUrl +
				",aliNo=" + aliNo +
				",birdNo=" + birdNo +
				",hundredNo=" + hundredNo +
				",otherNo=" + otherNo +
				'}';
	}
}
