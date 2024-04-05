package com.mall4j.cloud.api.distribution.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author ZengFanChang
 * @Date 2022/04/07
 */
@Data
public class DistributionActivityShareDetailsExcelVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "活动分享明细数据";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"活动分享明细数据", "导购姓名"}, index = 0)
    private String staffName;

    @ExcelProperty(value = {"活动分享明细数据", "导购工号"}, index = 1)
    private String staffNo;

    @ExcelProperty(value = {"活动分享明细数据", "活动/商品Id"}, index = 2)
    private Long activityId;

    @ExcelProperty(value = {"活动分享明细数据", "活动/商品名称"}, index = 3)
    private String activityName;

    @ExcelProperty(value = {"活动分享明细数据", "类型"}, index = 4)
    private String type;

    /**
     * 分享次数
     */
    @ExcelProperty(value = {"活动分享明细数据", "分享次数"}, index = 5)
    private Integer shareNum;

    /**
     * 浏览次数
     */
    @ExcelProperty(value = {"活动分享明细数据", "PV"}, index = 6)
    private Integer browseNum;

    /**
     * 浏览人次
     */
    @ExcelProperty(value = {"活动分享明细数据", "UV"}, index = 7)
    private Integer browseUserNum;

    /**
     * 加购人次
     */
    @ExcelProperty(value = {"活动分享明细数据", "加购人次"}, index = 8)
    private Integer purchaseUserNum;

    /**
     * 加购次数
     */
    @ExcelProperty(value = {"活动分享明细数据", "加购次数"}, index = 9)
    private Integer purchaseNum;

}
