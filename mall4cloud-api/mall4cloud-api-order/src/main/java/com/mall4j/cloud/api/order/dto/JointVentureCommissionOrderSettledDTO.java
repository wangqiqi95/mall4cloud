package com.mall4j.cloud.api.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class JointVentureCommissionOrderSettledDTO {
    private List<Long> orderIds;
    private Integer jointVentureCommissionStatus;
    private List<Long> refundIds;
}
