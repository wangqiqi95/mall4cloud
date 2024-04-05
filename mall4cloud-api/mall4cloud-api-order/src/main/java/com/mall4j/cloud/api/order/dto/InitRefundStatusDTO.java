package com.mall4j.cloud.api.order.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class InitRefundStatusDTO {

    private Long aftersaleId;
    private Integer status;

    private String buyerDesc;
    private String img_urls;

    private Integer applyType;

    private Long refundAmount;
}
