package com.mall4j.cloud.api.distribution.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author ZengFanChang
 * @Date 2022/04/07
 */
@Data
public class DistributionActivityShareExcelVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "活动分享数据";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    /**
     * 活动ID
     */
    @ExcelProperty(value = {"活动分享数据", "活动ID"}, index = 0)
    private Long activityId;

    /**
     * 商品ID
     */
    @ExcelProperty(value = {"活动分享数据", "商品ID"}, index = 1)
    private String spuCode;

    /**
     * 活动名称
     */
    @ExcelProperty(value = {"活动分享数据", "活动名称"}, index = 2)
    private String activityName;

    /**
     * 商品名称
     */
    @ExcelProperty(value = {"活动分享数据", "商品名称"}, index = 3)
    private String spuName;

    /**
     * 活动类型 1海报 2专题 3朋友圈 4商品
     */
    @ExcelProperty(value = {"活动分享数据", "类型"}, index = 4)
    private String type;

    /**
     * 分享次数
     */
    @ExcelProperty(value = {"活动分享数据", "分享次数"}, index = 5)
    private Integer shareNum;

    /**
     * 浏览次数
     */
    @ExcelProperty(value = {"活动分享数据", "PV"}, index = 6)
    private Integer browseNum;

    /**
     * 浏览人次
     */
    @ExcelProperty(value = {"活动分享数据", "UV"}, index = 7)
    private Integer browseUserNum;

    /**
     * 加购人次
     */
    @ExcelProperty(value = {"活动分享数据", "加购人次"}, index = 8)
    private Integer purchaseUserNum;

    /**
     * 加购次数
     */
    @ExcelProperty(value = {"活动分享数据", "加购次数"}, index = 9)
    private Integer purchaseNum;

}
