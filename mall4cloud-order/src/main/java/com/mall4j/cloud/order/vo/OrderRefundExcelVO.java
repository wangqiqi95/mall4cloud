package com.mall4j.cloud.order.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.mall4j.cloud.common.order.vo.RefundOrderItemVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 退款订单导出excel参数对象
 *
 * @luzhengxiang
 * @create 2022-03-18 4:38 PM
 **/
@Getter
@Setter
@EqualsAndHashCode
public class OrderRefundExcelVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "退款单信息";
    public static final String SHEET_NAME = "退款单";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = "退款编号", index = 0)
    private String refundNumber;

    @ExcelProperty(value = "退款单类型", index = 1)
    private String refundTypeName;

    @ExcelProperty(value = "退款方式", index = 2)
    private String applyTypeName;

    @ExcelProperty(value = {"订单编号"}, index = 3)
    private String orderNumber;

    @ExcelProperty(value = "实付总金额(元)", index = 4)
    private String actualTotal;

    @ExcelProperty(value = {"退款商品SPU"}, index = 5)
    private String spuCode;

    @ExcelProperty(value = {"退款商品SKU"}, index = 6)
    private String skuCode;

    @ExcelProperty(value = {"退款商品条码"}, index = 7)
    private String barcode;

    @ExcelProperty(value = "退款金额(元)", index = 8)
    private String refundAmount;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "申请时间", index = 9)
    protected Date createTime;

    @ExcelProperty(value = "退款原因", index = 10)
    private String buyerReasonValue;

    @ExcelProperty(value = "商品数量(0为全部商品)", index = 11)
    private Integer count;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "退款成功时间", index = 12)
    protected Date refundTime;

    @ExcelProperty(value = {"状态名称"}, index = 13)
    private String statusName;

    @ExcelProperty(value = {"下单门店编码"}, index = 14)
    private String storeCode;

    @ExcelProperty(value = {"下单门店名称"}, index = 15)
    private String storeName;

    @ExcelProperty(value = {"订单备注内容"}, index = 16)
    private String platformRemarks;

    @ExcelProperty(value = {"退单物流单号"}, index = 17)
    private String deliveryNo;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"物流单号填写时间"}, index = 18)
    private Date deliveryCreateTime;

    @ExcelProperty(value = {"代客下单人"}, index = 19)
    private String valetOrderStaffName;

    @ExcelProperty(value = {"代客下单人手机号"}, index = 20)
    private String valetOrderStaffPhone;

    @ExcelProperty(value = {"视频号售后单号"}, index = 21)
    private String aftersaleId;

    @ExcelProperty(value = {"视频号订单号"}, index = 22)
    private String wechatOrderId;

    @ExcelProperty(value = {"需售后处理时间"}, index = 23)
    private String afterSalesTime;



    @ExcelIgnore
    @ExcelProperty(value = {"退款商品"})
    private String spuName;

    @ExcelIgnore
    @ExcelProperty(value = "商品单价")
    private String price;

    @ExcelIgnore
    @ExcelProperty(value = "商品总价")
    private String totalPrice;

    @ExcelIgnore
    @ApiModelProperty("订单项")
    private List<RefundOrderItemVO> orderItems;

    @ExcelIgnore
    @ApiModelProperty("运费金额")
    private Long freifhtAmount;

}
