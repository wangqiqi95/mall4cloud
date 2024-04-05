package com.mall4j.cloud.product.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mall4j.cloud.common.model.ExcelModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * sku信息
 *
 * @author FrozenWatermelon
 * @date 2020-12-08 15:54:22
 */
@Data
public class SkuExcelLogVo extends ExcelModel {
    private static final long serialVersionUID = 1L;

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "商品sku价格异常信息";
    public static final String SHEET_NAME = "商品sku价格异常信息";
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
    @ExcelProperty(value = {"吊牌价"}, index = 3)
    private String marketPriceFee;
    @ExcelProperty(value = {"当前售价"}, index = 4)
    private String priceFee;
    @ExcelProperty(value = {"吊牌价*折扣"}, index = 5)
    private String discountPrice;
    @ExcelProperty(value = {"折扣等级"}, index = 6)
    private String discount;
    @ExcelProperty(value = {"历史吊牌价"}, index = 7)
    private String pastMarketPriceFee;

}
