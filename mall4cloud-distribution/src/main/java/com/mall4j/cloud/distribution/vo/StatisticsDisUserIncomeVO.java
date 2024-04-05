package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Citrus
 * @date 2021/8/19 15:09
 */
public class StatisticsDisUserIncomeVO {

    @ApiModelProperty("今日收益")
    private Double todayAmount;

    @ApiModelProperty("本月收益")
    private Double monthAmount;

    public Double getTodayAmount() {
        return todayAmount;
    }

    public void setTodayAmount(Double todayAmount) {
        this.todayAmount = todayAmount;
    }

    public Double getMonthAmount() {
        return monthAmount;
    }

    public void setMonthAmount(Double monthAmount) {
        this.monthAmount = monthAmount;
    }

    @Override
    public String toString() {
        return "StatisticsDisUserIncomeVO{" +
                "todayAmount=" + todayAmount +
                ", monthAmount=" + monthAmount +
                '}';
    }
}
