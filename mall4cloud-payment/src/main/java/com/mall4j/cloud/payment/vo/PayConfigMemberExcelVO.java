package com.mall4j.cloud.payment.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class PayConfigMemberExcelVO {
	
	/**
	 * excel 信息
	 */
	public static final String EXCEL_NAME = "会员名单";
	public static final String SHEET_NAME = "会员名单";
	
	/**
	 * 哪一行开始导出
	 */
	public static final int MERGE_ROW_INDEX = 2;
	/**
	 * 需要合并的列数组
	 */
	public static final int[] MERGE_COLUMN_INDEX = {};
	
	
	@ExcelProperty(value = {"会员手机号"}, index = 0)
	private String phone;
}
