package com.mall4j.cloud.api.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @author cl
 * @date 2021-05-22 14:30:11
 */
public class CustomerRetainVO {

    @ApiModelProperty("当前月")
    private String currentMonth;

    @ApiModelProperty("新访问/成交客户数")
    private Integer newCustomers;

    @ApiModelProperty("第1月留存")
    private Integer firstMonthRemain;
    @ApiModelProperty("第1月留存率")
    private BigDecimal firstMonthRemainRate;

    @ApiModelProperty("第2月留存")
    private Integer secondMonthRemain;
    @ApiModelProperty("第2月留存率")
    private BigDecimal secondMonthRemainRate;

    @ApiModelProperty("第3月留存")
    private Integer thirdMonthRemain;
    @ApiModelProperty("第3月留存率")
    private BigDecimal thirdMonthRemainRate;

    @ApiModelProperty("第4月留存")
    private Integer fourthMonthRemain;
    @ApiModelProperty("第4月留存率")
    private BigDecimal fourthMonthRemainRate;

    @ApiModelProperty("第5月留存")
    private Integer fifthMonthRemain;
    @ApiModelProperty("第5月留存率")
    private BigDecimal fifthMonthRemainRate;

    @ApiModelProperty("第6月留存")
    private Integer sixthMonthRemain;
    @ApiModelProperty("第6月留存率")
    private BigDecimal sixthMonthRemainRate;


    public String getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(String currentMonth) {
        this.currentMonth = currentMonth;
    }

    public Integer getNewCustomers() {
        return newCustomers;
    }

    public void setNewCustomers(Integer newCustomers) {
        this.newCustomers = newCustomers;
    }

    public Integer getFirstMonthRemain() {
        return firstMonthRemain;
    }

    public void setFirstMonthRemain(Integer firstMonthRemain) {
        this.firstMonthRemain = firstMonthRemain;
    }

    public BigDecimal getFirstMonthRemainRate() {
        return firstMonthRemainRate;
    }

    public void setFirstMonthRemainRate(BigDecimal firstMonthRemainRate) {
        this.firstMonthRemainRate = firstMonthRemainRate;
    }

    public Integer getSecondMonthRemain() {
        return secondMonthRemain;
    }

    public void setSecondMonthRemain(Integer secondMonthRemain) {
        this.secondMonthRemain = secondMonthRemain;
    }

    public BigDecimal getSecondMonthRemainRate() {
        return secondMonthRemainRate;
    }

    public void setSecondMonthRemainRate(BigDecimal secondMonthRemainRate) {
        this.secondMonthRemainRate = secondMonthRemainRate;
    }

    public Integer getThirdMonthRemain() {
        return thirdMonthRemain;
    }

    public void setThirdMonthRemain(Integer thirdMonthRemain) {
        this.thirdMonthRemain = thirdMonthRemain;
    }

    public BigDecimal getThirdMonthRemainRate() {
        return thirdMonthRemainRate;
    }

    public void setThirdMonthRemainRate(BigDecimal thirdMonthRemainRate) {
        this.thirdMonthRemainRate = thirdMonthRemainRate;
    }

    public Integer getFourthMonthRemain() {
        return fourthMonthRemain;
    }

    public void setFourthMonthRemain(Integer fourthMonthRemain) {
        this.fourthMonthRemain = fourthMonthRemain;
    }

    public BigDecimal getFourthMonthRemainRate() {
        return fourthMonthRemainRate;
    }

    public void setFourthMonthRemainRate(BigDecimal fourthMonthRemainRate) {
        this.fourthMonthRemainRate = fourthMonthRemainRate;
    }

    public Integer getFifthMonthRemain() {
        return fifthMonthRemain;
    }

    public void setFifthMonthRemain(Integer fifthMonthRemain) {
        this.fifthMonthRemain = fifthMonthRemain;
    }

    public BigDecimal getFifthMonthRemainRate() {
        return fifthMonthRemainRate;
    }

    public void setFifthMonthRemainRate(BigDecimal fifthMonthRemainRate) {
        this.fifthMonthRemainRate = fifthMonthRemainRate;
    }

    public Integer getSixthMonthRemain() {
        return sixthMonthRemain;
    }

    public void setSixthMonthRemain(Integer sixthMonthRemain) {
        this.sixthMonthRemain = sixthMonthRemain;
    }

    public BigDecimal getSixthMonthRemainRate() {
        return sixthMonthRemainRate;
    }

    public void setSixthMonthRemainRate(BigDecimal sixthMonthRemainRate) {
        this.sixthMonthRemainRate = sixthMonthRemainRate;
    }

    @Override
    public String toString() {
        return "CustomerRetainVO{" +
                "currentMonth=" + currentMonth +
                ", newCustomers=" + newCustomers +
                ", firstMonthRemain=" + firstMonthRemain +
                ", firstMonthRemainRate=" + firstMonthRemainRate +
                ", secondMonthRemain=" + secondMonthRemain +
                ", secondMonthRemainRate=" + secondMonthRemainRate +
                ", thirdMonthRemain=" + thirdMonthRemain +
                ", thirdMonthRemainRate=" + thirdMonthRemainRate +
                ", fourthMonthRemain=" + fourthMonthRemain +
                ", fourthMonthRemainRate=" + fourthMonthRemainRate +
                ", fifthMonthRemain=" + fifthMonthRemain +
                ", fifthMonthRemainRate=" + fifthMonthRemainRate +
                ", sixthMonthRemain=" + sixthMonthRemain +
                ", sixthMonthRemainRate=" + sixthMonthRemainRate +
                '}';
    }
}
