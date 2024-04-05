package com.mall4j.cloud.api.order.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author ZengFanChang
 * @Date 2022/04/07
 */
@Data
public class DistributionWitkeyExcelVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "微客分销数据";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    private Long userId;

    private Long storeId;

    @ExcelProperty(value = {"微客分销数据", "分销门店"}, index = 0)
    private String storeName;

    @ExcelProperty(value = {"微客分销数据", "会员卡号"}, index = 1)
    private String userNo;

    @ExcelProperty(value = {"微客分销数据", "手机号"}, index = 2)
    private String mobile;

    @ExcelProperty(value = {"微客分销数据", "上级导购工号"}, index = 3)
    private String parentNo;

    @ExcelProperty(value = {"微客分销数据", "上级导购手机号"}, index = 4)
    private String parentMobile;

    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"微客分销数据", "成为威客时间"}, index = 5)
    private Date witkeyPassTime;

    private Long distributionSales;

    @ExcelProperty(value = {"微客分销数据", "业绩"}, index = 6)
    private BigDecimal sales;

    private Long distributionCommission;

    @ExcelProperty(value = {"微客分销数据", "累计佣金"}, index = 7)
    private BigDecimal totalCommission;

    @ExcelProperty(value = {"微客分销数据", "订单数"}, index = 8)
    private Integer orderNum;

    @ExcelProperty(value = {"微客分销数据", "退单数"}, index = 9)
    private Integer refundNum;


}
