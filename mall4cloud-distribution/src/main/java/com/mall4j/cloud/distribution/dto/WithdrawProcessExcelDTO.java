package com.mall4j.cloud.distribution.dto;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @Author ZengFanChang
 * @Date 2021/12/10
 */
public class WithdrawProcessExcelDTO {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "提现处理信息";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"提现单号"}, index = 0)
    private String withdrawOrderNo;

    @ExcelProperty(value = {"审核意见"}, index = 1)
    private Integer withdrawStatus;

    @ExcelProperty(value = {"拒绝原因"}, index = 2)
    private String rejectReason;

    @ExcelProperty(value = {"转账方式"}, index = 3)
    private String transferType;

    public String getWithdrawOrderNo() {
        return withdrawOrderNo;
    }

    public void setWithdrawOrderNo(String withdrawOrderNo) {
        this.withdrawOrderNo = withdrawOrderNo;
    }

    public Integer getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(Integer withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    @Override
    public String toString() {
        return "WithdrawProcessExcelDTO{" +
                "withdrawOrderNo='" + withdrawOrderNo + '\'' +
                ", withdrawStatus=" + withdrawStatus +
                ", rejectReason='" + rejectReason + '\'' +
                ", transferType='" + transferType + '\'' +
                '}';
    }
}
