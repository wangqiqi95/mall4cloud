package com.mall4j.cloud.order.dto.platform.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ComplaintOrderPageResponse  {
    @ApiModelProperty("纠纷单号列表")
    @JsonProperty("orders")
    private List<ComplaintOrderSKX> orders;

    @ApiModelProperty("总数量")
    @JsonProperty("total")
    private Long total;
}
