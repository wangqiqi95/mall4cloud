package com.mall4j.cloud.api.user.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Date 2022年8月9日, 0009 14:10
 * @Created by eury
 */
@Data
public class SoldScoreBannerShopVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "重复门店数据";
    public static final String SHEET_NAME = "重复门店数据";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};


    @ExcelProperty(value = {"活动ID"}, index = 2)
    private Long activityId;
    @ExcelProperty(value = {"活动名称"}, index = 3)
    private String activityName;
    @ExcelProperty(value = {"门店编码"}, index = 0)
    private String storeCode;
    @ExcelProperty(value = {"门店名称"}, index = 1)
    private String storeName;

}
