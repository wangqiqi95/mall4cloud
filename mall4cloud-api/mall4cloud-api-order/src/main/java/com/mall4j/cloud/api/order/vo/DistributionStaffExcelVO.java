package com.mall4j.cloud.api.order.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author ZengFanChang
 * @Date 2022/04/05
 */
@Data
public class DistributionStaffExcelVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "导购分销数据";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    private Long staffId;

    private Long storeId;

    @ExcelProperty(value = {"导购分销数据", "门店编号"}, index = 0)
    private String storeCode;

    @ExcelProperty(value = {"导购分销数据", "门店名称"}, index = 1)
    private String storeName;

    @ExcelProperty(value = {"导购分销数据", "导购工号"}, index = 2)
    private String staffNo;

    @ExcelProperty(value = {"导购分销数据", "导购姓名"}, index = 3)
    private String staffName;

    @ExcelProperty(value = {"导购分销数据", "导购业绩"}, index = 4)
    private Long distributionSales;

    @ExcelProperty(value = {"导购分销数据", "发展业绩"}, index = 5)
    private Long developingSales;

    @ExcelProperty(value = {"导购分销数据", "手机号"}, index = 6)
    private String mobile;

    @ExcelProperty(value = {"导购分销数据", "订单数"}, index = 7)
    private Integer orderNum;

    @ExcelProperty(value = {"导购分销数据", "成功退单数"}, index = 8)
    private Integer refundNum;

    @ExcelProperty(value = {"导购分销数据", "邀请会员数"}, index = 9)
    private Integer userNum;

    @ExcelProperty(value = {"导购分销数据", "邀请威客数"}, index = 10)
    private Integer witkeyNum;

}
