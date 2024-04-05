package com.mall4j.cloud.distribution.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DistributionStoreActivityUserExcelVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "微客信息";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"会员名称"}, index = 0)
    private String userName;
    @ExcelProperty(value = {"年龄"}, index = 1)
    private Integer userAge;
    @ExcelProperty(value = {"性别"}, index = 2)
    private String gender;
    @ExcelProperty(value = {"会员手机号"}, index = 3)
    private String userMobile;
    @ExcelProperty(value = {"身份证号"}, index = 4)
    private String userIdCard;
    @ExcelProperty(value = {"所属导购"}, index = 5)
    private String userStaffName;
    @ExcelProperty(value = {"报名时间"}, index = 6)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ExcelProperty(value = {"签到时间"}, index = 7)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date signTime;
    @ExcelProperty(value = {"报名门店"}, index = 8)
    private String storeName;
    @ExcelProperty(value = {"衣服尺码"}, index = 9)
    private String clothesSize;
    @ExcelProperty(value = {"鞋子尺码"}, index = 10)
    private String shoesSize;
    @ExcelProperty(value = {"报名状态"}, index = 11)
    private String activityStatus;
    @ExcelProperty(value = {"取消时间"}, index = 12)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date cancelTime;

}
