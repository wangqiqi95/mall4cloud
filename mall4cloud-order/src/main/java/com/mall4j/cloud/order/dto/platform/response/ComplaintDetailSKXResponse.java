package com.mall4j.cloud.order.dto.platform.response;

import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintDetailResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ComplaintDetailSKXResponse extends ComplaintDetailResponse {
    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("退单编号")
    private String refundNumber;
}
