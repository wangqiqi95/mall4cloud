package com.mall4j.cloud.api.order.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 分销订单导出VO对象
 *
 * @luzhengxiang
 * @create 2022-03-25 11:59 PM
 **/
@Data
public class OrderDistributionExcelVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "分销订单信息";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"分类"}, index = 0)
    private String orderKindName;
    @ExcelProperty(value = {"商品名称"}, index = 1)
    private String spuName;
    @ExcelProperty(value = { "订单类型"}, index = 2)
    private String orderType;
    @ExcelProperty(value = {"退款单号"}, index = 3)
    private String orderRefundNumber;
    @ExcelProperty(value = {"订单号"}, index = 4)
    private String orderNumber;
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"下单时间"}, index = 5)
    private Date createTime;
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"分销订单创建时间"}, index = 6)
    private Date distributionCreateTime;
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"支付时间"}, index = 7)
    private Date payTime;
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"发货时间"}, index = 8)
    private Date deliveryTime;
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"收货时间"}, index = 9)
    private Date finallyTime;
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"入账时间"}, index = 10)
    private Date settledTime;
    @ExcelProperty(value = {"佣金状态"}, index = 11)
    private String distributionStatus;
    @ExcelProperty(value = { "购买/退款数量"}, index = 12)
    private Integer count;
    @ExcelProperty(value = { "实付/退款（元）"}, index = 13)
    private String amount;
    @ExcelProperty(value = {"分销佣金（元）"}, index = 14)
    private String distributionAmount;
    @ExcelProperty(value = {"发展佣金（元）"}, index = 15)
    private String developingAmount;
//    @ExcelProperty(value = {"下单人手机号"}, index = 16)
//    private String phone;
    @ExcelProperty(value = { "分销关系"}, index = 16)
    private String distributionRelationName;
    @ExcelProperty(value = {"下单门店"}, index = 17)
    private String storeName;
    @ExcelProperty(value = {"发展人"}, index = 18)
    private String developingName;
    @ExcelProperty(value = {"发展人员工编号"}, index = 19)
    private String developingCode;
    @ExcelProperty(value = {"分销员"}, index = 20)
    private String distributionName;
    @ExcelProperty(value = {"分销员员工编号"}, index = 21)
    private String distributionCode;
//    @ExcelProperty(value = {"分销员微客手机号"}, index = 23)
//    private String distributionPhone;
    @ExcelProperty(value = {"代客下单人"}, index = 22)
    private String valetOrderStaffName;
    @ExcelProperty(value = {"代客下单人手机号"}, index = 23)
    private String valetOrderStaffPhone;
    @ExcelProperty(value = {"微信侧订单编号"}, index = 24)
    private Long wechatOrderId;


    @ExcelIgnore
    private String distributionRelation;

}
