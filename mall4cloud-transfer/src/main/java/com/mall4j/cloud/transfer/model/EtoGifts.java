package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2022-04-14 13:57:20
 */
public class EtoGifts implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private String code;

    /**
     * 
     */
    private String cost;

    /**
     * 
     */
    private String status;

    /**
     * 
     */
    private String subTitle;

    /**
     * 
     */
    private String expiryDate;

    /**
     * 
     */
    private String receiveNum;

    /**
     * 
     */
    private String revice;

    /**
     * 
     */
    private String stockType;

    /**
     * 
     */
    private String writeOffSetting;

    /**
     * 
     */
    private String validDateType;

    /**
     * 
     */
    private String type;

    /**
     * 
     */
    private String reservedFields;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getReceiveNum() {
		return receiveNum;
	}

	public void setReceiveNum(String receiveNum) {
		this.receiveNum = receiveNum;
	}

	public String getRevice() {
		return revice;
	}

	public void setRevice(String revice) {
		this.revice = revice;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getWriteOffSetting() {
		return writeOffSetting;
	}

	public void setWriteOffSetting(String writeOffSetting) {
		this.writeOffSetting = writeOffSetting;
	}

	public String getValidDateType() {
		return validDateType;
	}

	public void setValidDateType(String validDateType) {
		this.validDateType = validDateType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReservedFields() {
		return reservedFields;
	}

	public void setReservedFields(String reservedFields) {
		this.reservedFields = reservedFields;
	}

	@Override
	public String toString() {
		return "EtoGifts{" +
				"id=" + id +
				",title=" + title +
				",code=" + code +
				",cost=" + cost +
				",status=" + status +
				",subTitle=" + subTitle +
				",expiryDate=" + expiryDate +
				",receiveNum=" + receiveNum +
				",revice=" + revice +
				",stockType=" + stockType +
				",writeOffSetting=" + writeOffSetting +
				",validDateType=" + validDateType +
				",type=" + type +
				",reservedFields=" + reservedFields +
				'}';
	}
}
