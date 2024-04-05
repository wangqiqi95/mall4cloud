package com.mall4j.cloud.api.distribution.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * @Author ZengFanChang
 * @Date 2022/04/07
 */
@Data
public class DistributionGuideShareExcelVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "导购分享数据";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    /**
     * 导购号
     */
    @ExcelProperty(value = {"导购分享数据", "导购工号"}, index = 0)
    private String guideNo;

    /**
     * 导购名称
     */
    @ExcelProperty(value = {"导购分享数据", "导购姓名"}, index = 1)
    private String guideName;

    /**
     * 数据日期
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"导购分享数据", "日期"}, index = 2)
    private Date dataTime;

    /**
     * 门店编号
     */
    @ExcelProperty(value = {"导购分享数据", "分销门店编号"}, index = 3)
    private String storeCode;

    /**
     * 门店名称
     */
    @ExcelProperty(value = {"导购分享数据", "分销门店名称"}, index = 4)
    private String storeName;

    /**
     * 活动类型 1海报 2专题 3朋友圈 4商品
     */
    @ExcelProperty(value = {"导购分享数据", "类型"}, index = 5)
    private String type;

    /**
     * 分享次数
     */
    @ExcelProperty(value = {"导购分享数据", "分享次数"}, index = 6)
    private Integer shareNum;

    /**
     * 浏览次数
     */
    @ExcelProperty(value = {"导购分享数据", "PV"}, index = 7)
    private Integer browseNum;

    /**
     * 浏览人次
     */
    @ExcelProperty(value = {"导购分享数据", "UV"}, index = 8)
    private Integer browseUserNum;

    /**
     * 加购人次
     */
    @ExcelProperty(value = {"导购分享数据", "加购人次"}, index = 9)
    private Integer purchaseUserNum;

}
