package com.mall4j.cloud.user.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

@Data
public class VeekerExcelVO {

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


    @ExcelProperty(value = {"微客信息", "微客名称"}, index = 0)
    private String name;
    @ExcelProperty(value = {"微客信息", "微客手机号"}, index = 1)
    private String phone;
    @ExcelProperty(value = {"微客信息", "微客会员卡号"}, index = 2)
    private String cardNo;
    @ExcelProperty(value = {"微客信息", "微客门店编码"}, index = 3)
    private String storeCode;
    @ExcelProperty(value = {"微客信息", "微客门店名称"}, index = 4)
    private String storeName;
    @ExcelProperty(value = {"微客信息", "微客申请时间"}, index = 5)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date veekerApplyTime;
    @ExcelProperty(value = {"微客信息", "是否添加微信"}, index = 6)
    private String addWechat;
    @ExcelProperty(value = {"微客信息", "微客状态"}, index = 7)
    private String veekerStatus;
    @ExcelProperty(value = {"微客信息", "微客审核状态"}, index = 8)
    private String veekerAuditStatus;
    @ExcelProperty(value = {"微客信息", "发展人名称"}, index = 9)
    private String staffName;
    @ExcelProperty(value = {"微客信息", "发展人状态"}, index = 10)
    private String staffStatus;
    @ExcelProperty(value = {"微客信息", "发展人手机号"}, index = 11)
    private String staffMobile;
    @ExcelProperty(value = {"微客信息", "发展人工号"}, index = 12)
    private String staffNo;
    @ExcelProperty(value = {"微客信息", "发展人门店编码"}, index = 13)
    private String staffStoreCode;
    @ExcelProperty(value = {"微客信息", "发展人门店名称"}, index = 14)
    private String staffStoreName;

}
