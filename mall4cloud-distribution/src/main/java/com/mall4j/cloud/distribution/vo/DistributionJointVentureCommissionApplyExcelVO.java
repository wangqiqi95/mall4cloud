package com.mall4j.cloud.distribution.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author Zhang Fan
 * @date 2022/8/5 17:16
 */
@Data
public class DistributionJointVentureCommissionApplyExcelVO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "联营分佣申请信息";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    /**
     * 客户姓名
     */
    @ExcelProperty(value = "客户姓名", order = 0)
    private String customerName;

    /**
     * 客户手机号
     */
    @ExcelProperty(value = "手机号", order = 1)
    private String customerPhone;

    /**
     * 申请编号
     */
    @ExcelProperty(value = "申请编号", order = 2)
    private String applyNo;

    /**
     * 订单时间
     */
    @ExcelProperty(value = "订单时间", order = 3)
    private String orderTime;

    /**
     * 订单成交金额
     */
    @ExcelProperty(value = "成交金额", order = 4)
    private String orderTurnover;

    /**
     * 分佣比例
     */
    @ExcelProperty(value = "分佣比例", order = 5)
    private String commissionRate;

    /**
     * 分佣金额
     */
    @ExcelProperty(value = "分佣金额", order = 6)
    private String commissionAmount;

    /**
     * 状态 0-待审核 1-待付款 2-付款成功 9-审核失败
     */
    @ExcelProperty(value = "状态", order = 7)
    private String status;
}
