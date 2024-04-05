package com.mall4j.cloud.distribution.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;

import java.util.Date;

/**
 * @Author ZengFanChang
 * @Date 2021/12/11
 */
public class WithdrawRecordExcelVo {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "提现记录信息";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    /**
     * 手机号
     */
    @ExcelProperty(value = {"手机号"}, index = 2)
    private String mobile;

    /**
     * 门店名称
     */
    @ExcelProperty(value = {"分销门店名称"}, index = 3)
    private String storeName;

    /**
     * 门店编号
     */
    @ExcelProperty(value = {"分销门店编号"}, index = 4)
    private String storeCode;

    /**
     * 提现单号
     */
    @ExcelProperty(value = {"提现单号"}, index = 5)
    private String withdrawOrderNo;

    /**
     * 提现金额
     */
    @ExcelProperty(value = {"提现金额"}, index = 6)
    private String withdrawAmount;

    /**
     * 转账方式 1默认三方转账
     */
    @ExcelProperty(value = {"转账方式"}, index = 7)
    private String transferType;

    /**
     * 申请时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"申请时间"}, index = 8)
    private Date applyTime;

    /**
     * 处理时间
     */
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"处理时间"}, index = 9)
    private Date processTime;

    /**
     * 提现状态 0待处理 1提现中 2提现成功 3提现失败 4拒绝提现
     */
    @ExcelProperty(value = {"提现状态"}, index = 10)
    private String withdrawStatus;

    /**
     * 拒绝原因
     */
    @ExcelProperty(value = {"拒绝原因"}, index = 11)
    private String rejectReason;

    /**
     * 转账失败原因
     */
    @ExcelProperty(value = {"转账失败原因"}, index = 12)
    private String transferFailReason;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getWithdrawOrderNo() {
        return withdrawOrderNo;
    }

    public void setWithdrawOrderNo(String withdrawOrderNo) {
        this.withdrawOrderNo = withdrawOrderNo;
    }

    public String getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(String withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public String getWithdrawStatus() {
        return withdrawStatus;
    }

    public void setWithdrawStatus(String withdrawStatus) {
        this.withdrawStatus = withdrawStatus;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getTransferFailReason() {
        return transferFailReason;
    }

    public void setTransferFailReason(String transferFailReason) {
        this.transferFailReason = transferFailReason;
    }

    @Override
    public String toString() {
        return "WithdrawRecordExcelVo{" +
                "mobile='" + mobile + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storeCode='" + storeCode + '\'' +
                ", withdrawOrderNo='" + withdrawOrderNo + '\'' +
                ", withdrawAmount='" + withdrawAmount + '\'' +
                ", transferType=" + transferType +
                ", applyTime=" + applyTime +
                ", processTime=" + processTime +
                ", withdrawStatus='" + withdrawStatus + '\'' +
                ", rejectReason='" + rejectReason + '\'' +
                ", transferFailReason='" + transferFailReason + '\'' +
                '}';
    }
}
