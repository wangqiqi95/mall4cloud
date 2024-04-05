package com.mall4j.cloud.api.biz.dto.livestore.request.complaint;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ComplaintDetailRequest {
    @ApiModelProperty("纠纷单号")
    private Long complaint_order_id;
}
