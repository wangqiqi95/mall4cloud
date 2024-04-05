package com.mall4j.cloud.common.model;

import java.io.Serializable;

/**
 * excel类模板
 *
 * @author YXF
 *
 */
public class ExcelModel implements Serializable {

	/**
	 * excel 信息
	 */
	public static final String EXCEL_NAME = "excel";
	public static final String SHEET_NAME = "sheet";
	/**
	 * 哪一行开始导出
	 */
	public static final int MERGE_ROW_INDEX = 2;
	/**
	 * 需要合并的列数组
	 */
	public static final int[] MERGE_COLUMN_INDEX = {};

}
