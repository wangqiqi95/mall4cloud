package com.mall4j.cloud.product.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.mall4j.cloud.common.model.ExcelModel;
import lombok.Data;

/**
 * sku信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-08 15:54:22
 */
@Data
public class SkuStoreExcelLogVo extends ExcelModel {
    private static final long serialVersionUID = 1L;

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "门店商品sku价格异常信息";
    public static final String SHEET_NAME = "门店商品sku价格异常信息";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"商品货号"}, index = 0)
    private String spuCode;
    @ExcelProperty(value = {"skuCode"}, index = 1)
    private String skuCode;
    @ExcelProperty(value = {"priceCode"}, index = 2)
    private String priceCode;
    @ExcelProperty(value = {"门店编码"}, index = 3)
    private String storeCode;
//    @ExcelProperty(value = {"门店库存"}, index = 3)
//    private String storeStock;
    @ExcelProperty(value = {"吊牌价"}, index = 4)
    private String marketPriceFee;
    @ExcelProperty(value = {"当前售价"}, index = 5)
    private String priceFee;
    @ExcelProperty(value = {"吊牌价*折扣"}, index = 6)
    private String discountPrice;
    @ExcelProperty(value = {"折扣等级"}, index = 7)
    private String discount;
    @ExcelProperty(value = {"POS价"}, index = 8)
    private String posPrice;
    @ExcelProperty(value = {"历史吊牌价"}, index = 9)
    private String pastMarketPriceFee;

}
