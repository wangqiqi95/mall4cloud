
package com.mall4j.cloud.api.biz.dto.livestore.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AuditInfo {

    @JsonProperty("audit_time")
    private String auditTime;
    @JsonProperty("reject_reason")
    private String rejectReason;
    @JsonProperty("submit_time")
    private String submitTime;

}
