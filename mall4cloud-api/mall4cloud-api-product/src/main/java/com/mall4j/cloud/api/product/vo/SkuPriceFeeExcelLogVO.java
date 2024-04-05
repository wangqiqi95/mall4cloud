package com.mall4j.cloud.api.product.vo;

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
public class SkuPriceFeeExcelLogVO extends ExcelModel {
	/**
	 * excel 信息
	 */
	public static final String EXCEL_NAME = "批量修改商品价格日志";
	public static final String SHEET_NAME = "商品";
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

	@ExcelProperty(value = {"商品货号"}, index = 1)
	private String spuCode;

	@ExcelProperty(value = {"商品条形码"}, index = 2)
	private String priceCode;

	@ExcelProperty(value = {"商品售价"}, index = 3)
	private String priceFee;

	@ExcelProperty(value = {"上下架状态"}, index = 4)
	private String status;

	@ExcelProperty(value = {"状态"}, index = 5)
	private String importStatus;

	@ExcelProperty(value = {"备注"}, index = 6)
	private String importRemarks;
}
