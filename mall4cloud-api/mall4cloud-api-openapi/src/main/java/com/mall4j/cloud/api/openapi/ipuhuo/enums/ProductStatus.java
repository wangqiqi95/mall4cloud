package com.mall4j.cloud.api.openapi.ipuhuo.enums;

/**
 * 类描述：商品状态
 */
public enum ProductStatus {
	ONSALE("onsale", "销售中"),
	INSTOCK("instock", "下架"),
	SOLDOUT("soldout", "售罄");

	private String status;

	private String desc;

	ProductStatus(String status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	public String getStatus() {
		return status;
	}

	public String getDesc() {
		return desc;
	}

	public static ProductStatus getReqMethodType(String status) {
		ProductStatus[] enums = values();
		for (ProductStatus productStatus : enums) {
			if (productStatus.getStatus().equals(status)) {
				return productStatus;
			}
		}
		return null;
	}
}
