
package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CategoryRequest {

    @JsonProperty("audit_req")
    private CategoryAuditReq auditReq;

}
