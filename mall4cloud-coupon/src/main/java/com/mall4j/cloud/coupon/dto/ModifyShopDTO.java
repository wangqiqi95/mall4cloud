package com.mall4j.cloud.coupon.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ModifyShopDTO implements Serializable {
    private List<Long> shopIds;
    private Integer activityId;
}
