package com.mall4j.cloud.api.order.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author ZengFanChang
 * @Date 2022/04/05
 */
@Data
public class DistributionStoreExcelVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "门店分销数据";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};


    @ExcelProperty(value = {"门店分销数据", "门店编号"}, index = 0)
    private String storeCode;

    @ExcelProperty(value = {"门店分销数据", "门店名称"}, index = 1)
    private String storeName;

    @ExcelProperty(value = {"门店分销数据", "导购业绩"}, index = 2)
    private Long distributionSales;

    @ExcelProperty(value = {"门店分销数据", "发展业绩"}, index = 3)
    private Long developingSales;

    @ExcelProperty(value = {"门店分销数据", "订单数"}, index = 4)
    private Integer orderNum;

    @ExcelProperty(value = {"门店分销数据", "成功退单数"}, index = 5)
    private Integer refundNum;

    @ExcelProperty(value = {"门店分销数据", "开单导购"}, index = 6)
    private Integer staffNum;

}
