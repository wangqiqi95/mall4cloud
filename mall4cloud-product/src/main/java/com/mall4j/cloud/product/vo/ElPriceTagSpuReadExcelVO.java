package com.mall4j.cloud.product.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.mall4j.cloud.common.model.ExcelModel;
import lombok.Data;

/**
 * spu信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Data
public class ElPriceTagSpuReadExcelVO extends ExcelModel {
	/**
	 * excel 信息
	 */
	public static final String EXCEL_NAME = "电子价签商品信息";
	public static final String SHEET_NAME = "电子价签商品信息";
	/**
	 * 哪一行开始导出
	 */
	public static final int MERGE_ROW_INDEX = 2;
	/**
	 * 需要合并的列数组
	 */
	public static final int[] MERGE_COLUMN_INDEX = {};

	@ExcelProperty(value = {"商品名称"}, index = 0)
	private String spuName;

	@ExcelProperty(value = {"商品SPU编码"}, index = 1)
	private String spuCode;

	@ExcelProperty(value = {"商品sku编码"}, index = 2)
	private String skuPriceCode;

	@ExcelProperty(value = {"商品原价"}, index = 3)
	private String spuPriceFee;

}
