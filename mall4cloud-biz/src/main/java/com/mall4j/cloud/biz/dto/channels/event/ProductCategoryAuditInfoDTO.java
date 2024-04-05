package com.mall4j.cloud.biz.dto.channels.event;

import lombok.Data;

/**
 * @author baiqingtao
 * @date 2023/3/3
 */
@Data
public class ProductCategoryAuditInfoDTO {

    /**
     * 审核Id
     */
    private String auditId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 相关原因
     */
    private String reason;

}
