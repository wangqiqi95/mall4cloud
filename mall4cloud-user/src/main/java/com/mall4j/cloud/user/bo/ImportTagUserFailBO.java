package com.mall4j.cloud.user.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ImportTagUserFailBO {

    public static final String EXCEL_NAME = "导入失败会员名单";
    public static final String SHEET_NAME = "导入失败会员名单";

    @ExcelProperty(value = {"会员卡号"},index = 0)
    private String vipCode;

    @ExcelProperty(value = {"失败原因"},index = 1)
    private String message;
}
