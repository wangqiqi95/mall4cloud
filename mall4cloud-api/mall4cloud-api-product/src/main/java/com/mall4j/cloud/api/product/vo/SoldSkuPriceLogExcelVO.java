package com.mall4j.cloud.api.product.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.mall4j.cloud.common.model.ExcelModel;
import lombok.Data;

import java.util.Date;

/**
 */
@Data
public class SoldSkuPriceLogExcelVO extends ExcelModel {
	/**
	 * excel 信息
	 */
	public static final String EXCEL_NAME = "价格日志";
	public static final String SHEET_NAME = "价格日志";
	/**
	 * 哪一行开始导出
	 */
	public static final int MERGE_ROW_INDEX = 2;
	/**
	 * 需要合并的列数组
	 */
	public static final int[] MERGE_COLUMN_INDEX = {};

	@ExcelProperty(value = {"日志类型"}, index = 0)
	private String logType;
	@ExcelProperty(value = {"商品货号"}, index = 1)
	private String spuCode;
	@ExcelProperty(value = {"SKU"}, index = 2)
	private String priceCode;
	@ExcelProperty(value = {"条码"}, index = 3)
	private String skuCode;
	@ExcelProperty(value = {"门店编码"}, index = 4)
	private String storeCode;
	@ExcelProperty(value = {"价格"}, index = 5)
	private String price;
	@ExcelProperty(value = {"创建人"}, index = 6)
	private String updateBy;
	@ExcelProperty(value = {"创建时间"}, index = 7)
	@DateTimeFormat("yyyy-MM-dd HH:mm:ss")
	private Date updateTime;
}
