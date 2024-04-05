package com.mall4j.cloud.order.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.mall4j.cloud.common.model.ExcelModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * sku信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-08 15:54:22
 */
@Data
public class SkuOrderPriceErrorLogVo extends ExcelModel {
    private static final long serialVersionUID = 1L;

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "商品预警信息";
    public static final String SHEET_NAME = "商品预警信息";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"商品货号"}, index = 0)
    private String spuCode;
    @ExcelProperty(value = {"sku色码"}, index = 1)
    private String priceCode;
    @ExcelProperty(value = {"吊牌价"}, index = 2)
    private String marketPriceFee;
    @ExcelProperty(value = {"销售价"}, index = 3)
    private String priceFee;
    @ExcelProperty(value = {"销售数量"}, index = 4)
    private BigDecimal saleNum;
//    @ExcelProperty(value = {"统计起始时间"}, index = 5)
////    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
//    private String startTime;
//    @ExcelProperty(value = {"统计结束时间"}, index = 6)
////    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
//    private String endTime;

}
