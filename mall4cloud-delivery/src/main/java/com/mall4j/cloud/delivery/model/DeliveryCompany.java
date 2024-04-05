package com.mall4j.cloud.delivery.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 物流公司
 *
 * @author FrozenWatermelon
 * @date 2020-12-07 15:10:00
 */
public class DeliveryCompany extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long deliveryCompanyId;

    /**
     * 物流公司名称
     */
    private String name;

    /**
     * 公司主页
     */
    private String homeUrl;

    /**
     * 物流公司编号(阿里)
     */
    private String aliNo;

    /**
     * 物流公司编号(快递鸟)
     */
    private String birdNo;

    /**
     * 物流公司编号(快递100)
     */
    private String hundredNo;

    /**
     * 物流公司编号(其他不知名公司)
     */
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
		return "DeliveryCompany{" +
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
