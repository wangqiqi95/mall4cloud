package com.mall4j.cloud.distribution.vo;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * @Author ZengFanChang
 * @Date 2021/12/11
 */
public class MemberWithdrawRecordExcelVo extends WithdrawRecordExcelVo {

    @ExcelProperty(value = {"微客名称"}, index = 0)
    private String name;

    @ExcelProperty(value = {"微客卡号"}, index = 1)
    private String number;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "MemberWithdrawRecordExcelVo{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
