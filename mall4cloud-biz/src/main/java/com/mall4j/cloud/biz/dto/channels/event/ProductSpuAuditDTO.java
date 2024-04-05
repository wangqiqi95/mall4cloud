package com.mall4j.cloud.biz.dto.channels.event;

import lombok.Data;

/**
 * @date 2023/3/9
 */
@Data
public class ProductSpuAuditDTO {
    private String productId;

    private Integer status;

    private String reason;
}
