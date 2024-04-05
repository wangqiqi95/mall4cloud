package com.mall4j.cloud.product.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.product.vo.BrandLangVO;
import com.mall4j.cloud.common.product.vo.SpuLangVO;
import com.mall4j.cloud.common.product.vo.SpuSkuAttrValueVO;
import com.mall4j.cloud.common.util.annotation.Excel;
import lombok.Data;

import java.util.List;

/**
 * spu信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Data
public class SpuCodeReadExcelVO {
//public class SpuCodeReadExcelVO extends ExcelModel {
	/**
	 * excel 信息
	 */
//	public static final String EXCEL_NAME = "商品spuCode信息";
//	public static final String SHEET_NAME = "商品";
//	/**
//	 * 哪一行开始导出
//	 */
//	public static final int MERGE_ROW_INDEX = 2;
//	/**
//	 * 需要合并的列数组
//	 */
//	public static final int[] MERGE_COLUMN_INDEX = {};
//
//	@ExcelProperty(value = {"序号"}, index = 0)
//	private Integer seq;
//
//	@ExcelProperty(value = {"商品货号"}, index = 1)
//	private String spuCode;

	@Excel(name = "序号")
	private Integer seq;

	@Excel(name = "商品货号")
	private String spuCode;

}
