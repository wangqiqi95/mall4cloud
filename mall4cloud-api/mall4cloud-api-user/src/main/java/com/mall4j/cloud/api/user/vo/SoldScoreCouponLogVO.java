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
public class SoldScoreCouponLogVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "兑换记录";
    public static final String SHEET_NAME = "兑换记录";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};


    @ExcelProperty(value = {"兑换记录ID"}, index = 0)
    private String id;

    @ExcelProperty(value = {"活动ID"}, index = 1)
    private String convertId;
    @ExcelProperty(value = {"活动名称"}, index = 2)
    private String convertTitle;
    @ExcelProperty(value = {"礼品名称"}, index = 3)
    private String prizeName;

    @ExcelProperty(value = {"用户卡号"}, index = 4)
    private String userCardNumber;

    @ExcelProperty(value = {"会员手机号"}, index = 5)
    private String userPhone;

    @ExcelProperty(value = {"兑换积分数"}, index = 6)
    private String convertScore;

    @ExcelProperty(value = {"兑换类型"}, index = 7)
    private String type;

    @ExcelProperty(value = {"兑换门店"}, index = 8)
    private String storeName;

    @ExcelProperty(value = {"状态"}, index = 9)
    private String status;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"兑换时间"}, index = 10)
    private Date createTime;

    @ExcelProperty(value = {"用户收货信息"}, index = 11)
    private String addr;

    @ExcelProperty(value = {"物流信息"}, index = 12)
    private String logisticsNo;

    @ExcelProperty(value = {"物流公司名称"}, index = 13)
    private String company;

}
