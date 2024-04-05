package com.mall4j.cloud.biz.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 门店直播商品导出Excel
 *
 * @luzhengxiang
 * @create 2022-03-20 8:23 PM
 **/
@Data
public class LiveProdStoreExcelVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "直播商品";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};


    @ExcelProperty(value = {"直播商品", "商品名称"}, index = 2)
    private String name;
    @ExcelProperty(value = {"直播商品", "价格"}, index = 2)
    private String showPrice;
    @ExcelProperty(value = {"直播商品", "状态"}, index = 2)
    private String statusName;
    @ExcelProperty(value = {"直播商品", "创建时间"}, index = 2)
    private Date createTime;
    @ExcelProperty(value = {"直播商品", "店铺名称"}, index = 2)
    private Long storeName;

    @ExcelIgnore
    private Long prodId;

    /**
     * 价格类型，1：一口价（只需要传入price，price2不传） 2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传） 3：显示折扣价（price字段为原价，price2字段为现价， price和price2必传）
     */
    @ExcelIgnore
    private Integer priceType;
    /**
     * 商品价格(元)
     */
    @ExcelIgnore
    private Double price;
    /**
     * 商品价格(元)
     */
    @ExcelIgnore
    private Double price2;
}
