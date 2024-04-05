package com.mall4j.cloud.api.product.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.mall4j.cloud.common.model.ExcelModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 活动sku导入参数对象
 *
 * @luzhengxiang
 * @create 2022-03-16 11:55 PM
 **/
@Data
public class ActivitySpuSkuExportLogDTO extends ExcelModel {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "虚拟门店调价日志";
    public static final String SHEET_NAME = "商品";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"序号"}, index = 0)
    private Integer seq;

    @ExcelProperty(value = {"商品名称"}, index = 1)
    private String skuName;

    @ExcelProperty(value = {"商品货号"}, index = 2)
    private String spuBarcode;

    @ExcelProperty(value = {"skuCode"}, index = 3)
    private String priceCode;

    @ExcelProperty(value = {"商品条形码"}, index = 4)
    private String skuBarcode;

    @ExcelProperty(value = {"商品原价"}, index = 5)
    private BigDecimal price;

    @ExcelProperty(value = {"商品调价"}, index = 6)
    private BigDecimal discountPrice;

    @ExcelProperty(value = {"状态"}, index = 7)
    private String importStatus;

    @ExcelProperty(value = {"备注"}, index = 8)
    private String importRemarks;

}
