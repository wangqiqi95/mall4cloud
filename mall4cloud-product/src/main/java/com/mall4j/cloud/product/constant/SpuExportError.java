package com.mall4j.cloud.product.constant;

/**
 * 属性类型
 * @author yxf
 * @date 2020/11/20
 */
public enum SpuExportError {

	/**
	 * 商品名称
	 */
	SPU_NAME(1, "商品名称错误的序号为："),

	/**
	 * sku名称
	 */
	SKU_NAME(2, "sku名称错误的序号为："),

	/**
	 * 平台分类
	 */
	PLATFORM_CATEGORY(3, "平台分类错误的序号为："),

	/**
	 * 店铺分类
	 */
	SHOP_CATEGORY(4, "店铺分类错误的序号为："),

	/**
	 * 运费模板
	 */
	DELIVER(6, "运费模板错误的序号为："),

	/**
	 * 属性
	 */
	PROPERTIES(7, "销售属性错误的序号为："),

	/**
	 * 商品保存异常
	 */
	PRODUCT_DATA(8, "商品数据不正确的序号为："),

	/**
	 * 其他异常
	 */
	OTHER(100, "销售属性错误的序号为：")
	;

	private final Integer value;

	private final String errorInfo;

	public Integer value() {
		return value;
	}

	public String errorInfo() {
		return errorInfo;
	}

	SpuExportError(Integer value, String errorInfo) {
		this.value = value;
		this.errorInfo = errorInfo;
	}

	public static SpuExportError instance(Integer value) {
		SpuExportError[] enums = values();
		for (SpuExportError spuExportError : enums) {
			if (spuExportError.value().equals(value)) {
				return spuExportError;
			}
		}
		return null;
	}

}
