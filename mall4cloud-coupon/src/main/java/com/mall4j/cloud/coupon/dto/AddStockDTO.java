package com.mall4j.cloud.coupon.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddStockDTO implements Serializable {
    private Integer id;
    private Integer stock;
}
