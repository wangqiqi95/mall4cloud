package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-04-14 13:57:21
 */
public class EtoGiftsRecord implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Long userid;

    /**
     * 
     */
    private String number;

    /**
     * 
     */
    private String mobile;

    /**
     * 
     */
    private String costIntegral;

    /**
     * 
     */
    private String giftId;

    /**
     * 
     */
    private String createdAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCostIntegral() {
		return costIntegral;
	}

	public void setCostIntegral(String costIntegral) {
		this.costIntegral = costIntegral;
	}

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "EtoGiftsRecord{" +
				"id=" + id +
				",userid=" + userid +
				",number=" + number +
				",mobile=" + mobile +
				",costIntegral=" + costIntegral +
				",giftId=" + giftId +
				",createdAt=" + createdAt +
				'}';
	}
}
