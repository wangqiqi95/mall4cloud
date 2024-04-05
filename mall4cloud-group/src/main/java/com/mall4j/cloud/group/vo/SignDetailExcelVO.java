package com.mall4j.cloud.group.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SignDetailExcelVO implements Serializable {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "签到明细报表";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"签到时间"},index = 0)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date signTime;
    @ExcelProperty(value = {"手机号"},index = 1)
    private String mobile;
    @ExcelProperty(value = {"服务门店名称"},index = 2)
    private String shopName;
    @ExcelProperty(value = {"服务门店编号"},index = 3)
    private String shopCode;
    @ExcelProperty(value = {"奖励类型"},index = 4)
    private String signType;
    @ExcelProperty(value = {"积分"},index = 5)
    private Integer pointNum;
    @ExcelProperty(value = {"优惠券"},index = 6)
    private String couponName;
}
