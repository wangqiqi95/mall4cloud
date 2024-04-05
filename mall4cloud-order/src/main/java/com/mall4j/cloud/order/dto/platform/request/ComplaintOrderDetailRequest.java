package com.mall4j.cloud.order.dto.platform.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ComplaintOrderDetailRequest {
    @ApiModelProperty("纠纷单编号")
    private Long complaintOrderId;
}
