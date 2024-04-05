package com.mall4j.cloud.api.platform.vo;

public class StoreCodeVO {

	private Long  storeId;

	private String storeCode;

	private String stationName;

	public StoreCodeVO() {
	}

	public StoreCodeVO(Long storeId, String storeCode) {
		this.storeId = storeId;
		this.storeCode = storeCode;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	@Override
	public String toString() {
		return "StoreCodeVO{" + "storeId=" + storeId + ", storeCode='" + storeCode + '\'' + '}';
	}
}
