package com.mall4j.cloud.order.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class YesterdayOrderStatisticsVO {

    private float giveOrderAmountSum;
    private Integer giveOrderNumSum;
    private float paymentAmountSum;
    private Integer payedNumSum;

}
