package com.mall4j.cloud.coupon.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Grady_Lu
 */
@Data
public class ChooseMemberEventExchangeRecordExcelVO {

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
    private String exchangeRecordId;

    @ExcelProperty(value = {"活动ID"}, index = 1)
    private String eventId;

    @ExcelProperty(value = {"活动名称"}, index = 2)
    private String eventTitle;

    @ExcelProperty(value = {"兑换类型"}, index = 3)
    private String exchangeType;

    @ExcelProperty(value = {"会员卡号"}, index = 4)
    private String userVipCode;

    @ExcelProperty(value = {"会员手机号"}, index = 5)
    private String mobile;

    @ExcelProperty(value = {"服务门店编码"}, index = 6)
    private String belongShopCode;

    @ExcelProperty(value = {"服务门店名称"}, index = 7)
    private String belongShopName;

    @ExcelProperty(value = {"发货状态"}, index = 8)
    private String deliveryStatus;

    @ExcelProperty(value = {"领取时间"}, index = 9)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ExcelProperty(value = {"用户收货信息"}, index = 10)
    private String deliveryInfo;

    @ExcelProperty(value = {"物流信息"}, index = 11)
    private String trackingNumber;

    @ExcelProperty(value = {"物流公司名称"}, index = 12)
    private String logisticsCompany;

}
