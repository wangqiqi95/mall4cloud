package com.mall4j.cloud.product.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.mall4j.cloud.common.model.ExcelModel;
import com.mall4j.cloud.common.util.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 **/
@Data
public class ProtectPriceSpuLogExportDTO extends ExcelModel {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "电商保护价日志";
    public static final String SHEET_NAME = "电商保护价";
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

    @ExcelProperty(value = {"电商保护价"}, index = 2)
    private String protectPrice;

    @ExcelProperty(value = {"开始时间"}, index = 3)
    private String startTime;

    @ExcelProperty(value = {"结束时间"}, index = 4)
    private String endTime;

    @ExcelProperty(value = {"备注"}, index = 5)
    private String importRemarks;

}
