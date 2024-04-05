package com.mall4j.cloud.api.biz.dto.livestore.response.complaint;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.BaseResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ComplaintDetailResponse  extends BaseResponse {

    @ApiModelProperty("纠纷单信息")
    @JsonProperty("order")
    private ComplaintOrder order;

    @ApiModelProperty("纠纷单流程信息")
    @JsonProperty("flow_info")
    private FlowInfoList flow_info;

}
