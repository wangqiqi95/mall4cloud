package com.mall4j.cloud.biz.dto.cp;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
@NoArgsConstructor
public class StaffCodeExcelDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String EXCEL_NAME = "员工活码异常信息";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};


    @ExcelProperty(value = {"员工姓名"}, index = 0)
    private String  staffName;

     @ExcelProperty(value = {"员工工号"}, index = 1)
    private String  staffNo;

    @ExcelProperty(value = {"异常信息"}, index = 2)
    private String  errorMessage;

    private String errorCode;

    @ExcelIgnore
    private Long staffId;

    public StaffCodeExcelDTO(String  staffNo,String  staffName,String  errorMessage){
        this.staffNo = staffNo;
        this.staffName = staffName;
        this.errorMessage = errorMessage;
    }



}
