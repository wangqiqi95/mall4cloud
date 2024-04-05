
package com.mall4j.cloud.api.biz.dto.livestore.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BrandRequest {

    @JsonProperty("audit_req")
    private BrandAuditReq auditReq;

}
