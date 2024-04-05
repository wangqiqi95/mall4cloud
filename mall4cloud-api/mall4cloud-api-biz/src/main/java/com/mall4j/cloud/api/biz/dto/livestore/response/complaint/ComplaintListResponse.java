package com.mall4j.cloud.api.biz.dto.livestore.response.complaint;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ComplaintListResponse extends BaseResponse {

    @ApiModelProperty("纠纷单号列表")
    @JsonProperty("orders")
    private List<ComplaintOrder> orders;

    @ApiModelProperty("总数量")
    @JsonProperty("total")
    private Long total;
}
