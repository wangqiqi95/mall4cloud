package com.mall4j.cloud.distribution.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DistributionRecommendSpuExcelVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "推荐商品";
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

    @ExcelProperty(value = {"商品售价"}, index = 2)
    private Long priceFee;

    @ExcelProperty(value = {"市场价"}, index = 3)
    private Long marketPriceFee;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"有效期-开始时间"}, index = 4)
    private Date startTime;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"有效期-结束时间"}, index = 5)
    private Date endTime;

    @ExcelProperty(value = {"门店范围"}, index = 6)
    private String limitStore;

    @ExcelProperty(value = {"状态"}, index = 7)
    private String status;

}
