package com.mall4j.cloud.distribution.vo;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @Author ZengFanChang
 * @Date 2021/12/11
 */
public class CommissionAccountExcelVo {

    /**
     * excel 信息
     */
    public static final String EXCEL_NAME = "佣金信息";
    /**
     * 哪一行开始导出
     */
    public static final int MERGE_ROW_INDEX = 2;
    /**
     * 需要合并的列数组
     */
    public static final int[] MERGE_COLUMN_INDEX = {};

    @ExcelProperty(value = {"手机号"}, index = 2)
    private String mobile;

    @ExcelProperty(value = {"分销门店名称"}, index = 3)
    private String storeName;

    @ExcelProperty(value = {"分销门店编号"}, index = 4)
    private String storeCode;

    /**
     * 待结算佣金
     */
    @ExcelProperty(value = {"待入账(元)"}, index = 5)
    private String waitCommission;

    /**
     * 可提现佣金
     */
    @ExcelProperty(value = {"可提现(元)"}, index = 6)
    private String canWithdraw;

    /**
     * 累计提现佣金
     */
    @ExcelProperty(value = {"累计提现(元)"}, index = 7)
    private String totalWithdraw;

    /**
     * 累计提现佣金
     */
    @ExcelProperty(value = {"已提现需退还佣金"}, index = 8)
    private String withdrawNeedRefund;

    /**
     * 状态 0禁用 1启用
     */
    @ExcelProperty(value = {"状态"}, index = 9)
    private String status;

    public String getWithdrawNeedRefund() {
        return withdrawNeedRefund;
    }

    public void setWithdrawNeedRefund(String withdrawNeedRefund) {
        this.withdrawNeedRefund = withdrawNeedRefund;
    }

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

    public String getWaitCommission() {
        return waitCommission;
    }

    public void setWaitCommission(String waitCommission) {
        this.waitCommission = waitCommission;
    }

    public String getCanWithdraw() {
        return canWithdraw;
    }

    public void setCanWithdraw(String canWithdraw) {
        this.canWithdraw = canWithdraw;
    }

    public String getTotalWithdraw() {
        return totalWithdraw;
    }

    public void setTotalWithdraw(String totalWithdraw) {
        this.totalWithdraw = totalWithdraw;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CommissionAccountExcelVo{" +
                "mobile='" + mobile + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storeCode='" + storeCode + '\'' +
                ", waitCommission=" + waitCommission +
                ", canWithdraw=" + canWithdraw +
                ", totalWithdraw=" + totalWithdraw +
                ", status=" + status +
                '}';
    }
}
