package com.mall4j.cloud.api.biz.dto.channels.response;

import com.mall4j.cloud.api.biz.dto.channels.EcCategoryAuditResult;
import lombok.Data;

@Data
public class EcGetCategoryAuditResultResponse extends EcBaseResponse {
    private EcCategoryAuditResult data;
}
