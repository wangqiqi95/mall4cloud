package com.mall4j.cloud.product.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SimpleSpuExcelVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "全量商品数据导出";
    public static final String SHEET_NAME = "全量商品数据导出";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"商品名称"}, index = 0)
    private String spuName;

    @ExcelProperty(value = {"商品货号"}, index = 1)
    private String spuCode;

    //sku
    @ExcelProperty(value = {"销售属性"}, index = 2)
    private String properties;
    @ExcelProperty(value = {"销售属性值"}, index = 3)
    private String propertiesValues;
    @ExcelProperty(value = {"SKU编码(款+色)"}, index = 4)
    private String priceCode;

    @ExcelProperty(value = {"商品条形码"}, index = 5)
    private String skuCode;

    @ExcelProperty(value = {"外部条形码"}, index = 6)
    private String modelId;

    @ExcelProperty(value = {"商品分类"}, index = 7)
    private String platformCategory;

    @ExcelProperty(value = {"商品导航"}, index = 8)
    private String shopCategory;

    @ExcelProperty(value = {"吊牌价"}, index = 9)
    private String marketPriceFee;

    @ExcelProperty(value = {"当前售价"}, index = 10)
    private String priceFee;

    @ExcelProperty(value = {"小程序库存"}, index = 11)
    private String stock;

    @ExcelProperty(value = {"小程序销量"}, index = 12)
    private Integer saleNum;

    @ExcelProperty(value = {"barcode状态：-1未启用/1启用"}, index = 13)
    private String skucodeStatus;

    @ExcelProperty(value = {"商品状态"}, index = 14)
    private String spuStatus;

    @ExcelProperty(value = {"创建时间"}, index = 15)
	@DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ExcelProperty(value = {"上架时间"}, index = 16)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ExcelProperty(value = {"运费模板"}, index = 17)
    private String transName;

    @ExcelProperty(value = {"小程序链接"}, index = 18)
    private String spuUrl;


    @ExcelIgnore
    private Long spuId;
    @ExcelIgnore
    private Long deliveryTemplateId;
    @ExcelIgnore
    private Long categoryId;
    @ExcelIgnore
    private Long shopCategoryId;

}
