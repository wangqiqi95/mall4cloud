package com.mall4j.cloud.api.biz.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.mall4j.cloud.common.model.ExcelModel;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author FrozenWatermelon
 * @date 2020-10-28 15:27:24
 */
@Data
public class SoldStaffCodeErrorExcelVO extends ExcelModel {
	/**
	 * excel 信息
	 */
	public static final String EXCEL_NAME = "活码失败记录";
	public static final String SHEET_NAME = "活码失败记录";
	/**
	 * 哪一行开始导出
	 */
	public static final int MERGE_ROW_INDEX = 2;
	/**
	 * 需要合并的列数组
	 */
	public static final int[] MERGE_COLUMN_INDEX = {};

	@ExcelProperty(value = {"使用员工"}, index = 0)
	private String staffName;
	@ExcelProperty(value = {"员工工号"}, index = 1)
	private String staffNo;
	@ExcelProperty(value = {"失败原因"}, index = 2)
	private String logs;
	@ExcelProperty(value = {"创建人"}, index = 3)
	private String createBy;
	@ExcelProperty(value = {"创建时间"}, index = 4)
	@DateTimeFormat("yyyy-MM-dd HH:mm:ss")
	private Date createTime;

}
