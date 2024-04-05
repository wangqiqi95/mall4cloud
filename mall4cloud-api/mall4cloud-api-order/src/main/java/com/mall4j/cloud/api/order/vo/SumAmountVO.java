package com.mall4j.cloud.api.order.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author Citrus
 * @date 2021/8/16 9:10
 */
public class SumAmountVO {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户消费笔数")
    private Double expenseNumber;

    @ApiModelProperty("用户消费金额")
    private Double sumOfConsumption;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getExpenseNumber() {
        return expenseNumber;
    }

    public void setExpenseNumber(Double expenseNumber) {
        this.expenseNumber = expenseNumber;
    }

    public Double getSumOfConsumption() {
        return sumOfConsumption;
    }

    public void setSumOfConsumption(Double sumOfConsumption) {
        this.sumOfConsumption = sumOfConsumption;
    }

    @Override
    public String toString() {
        return "SumAmountVO{" +
                "userId=" + userId +
                ", expenseNumber=" + expenseNumber +
                ", sumOfConsumption=" + sumOfConsumption +
                '}';
    }
}
