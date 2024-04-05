package com.mall4j.cloud.group.vo;

import com.alibaba.excel.annotation.ExcelProperty;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LotteryAwardRecordExportVO implements Serializable {
    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "中奖记录表";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"奖品名称"}, index = 0)
    private String prizeName;
    @ExcelProperty(value = {"券号"}, index = 1)
    private String couponCode;
    @ExcelProperty(value = {"券状态"}, index = 2)
    private String couponStatus;
    @ExcelProperty(value = {"会员"}, index = 3)
    private String nickname;
    @ExcelProperty(value = {"手机号"}, index = 4)
    private String mobile;
    @ExcelProperty(value = {"时间"}, index = 5)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date awardTime;
    @ExcelProperty(value = {"服务门店"}, index = 6)
    private String shopName;
    @ExcelProperty(value = {"收件人"}, index = 7)
    private String username;
    @ExcelProperty(value = {"收件人电话号码"}, index = 8)
    private String phone;
    @ExcelProperty(value = {"收货地址"}, index = 9)
    private String address;
}
