package com.mall4j.cloud.coupon.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CouponPackDrawDTO implements Serializable {
    private Integer id;
    private Long userId;
    private Long storeId;
}
