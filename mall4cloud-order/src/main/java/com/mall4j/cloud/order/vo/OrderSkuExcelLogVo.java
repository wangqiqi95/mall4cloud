package com.mall4j.cloud.order.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.mall4j.cloud.common.model.ExcelModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * sku信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-08 15:54:22
 */
@Data
public class OrderSkuExcelLogVo extends ExcelModel {
    private static final long serialVersionUID = 1L;

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "已支付订单价格异常信息";
    public static final String SHEET_NAME = "已支付订单价格异常信息";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"订单编号"}, index = 0)
    private String orderNumber;
    @ExcelProperty(value = {"商品货号"}, index = 1)
    private String spuCode;
    @ExcelProperty(value = {"skuCode"}, index = 2)
    private String skuCode;
    @ExcelProperty(value = {"priceCode"}, index = 3)
    private String priceCode;
    @ExcelProperty(value = {"实际支付总值"}, index = 4)
    private String actualTotal;
    @ExcelProperty(value = {"下单时商品售价"}, index = 5)
    private String priceFee;
    @ExcelProperty(value = {"吊牌价"}, index = 6)
    private String marketPriceFee;
    @ExcelProperty(value = {"吊牌价*折扣"}, index = 7)
    private String discountPrice;
    @ExcelProperty(value = {"折扣"}, index = 8)
    private String discount;
    @ExcelProperty(value = {"订单创建时间"}, index = 9)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ExcelProperty(value = {"订单支付时间"}, index = 10)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date payTime;
    @ExcelProperty(value = {"支付状态"}, index = 11)
    private String payStatus;
    @ExcelProperty(value = {"优惠券id"}, index = 12)
    private String couponId;
    @ExcelProperty(value = {"优惠券名称"}, index = 13)
    private String couponName;
    @ExcelProperty(value = {"优惠券抵扣金额"}, index = 14)
    private String couponAmount;

}
