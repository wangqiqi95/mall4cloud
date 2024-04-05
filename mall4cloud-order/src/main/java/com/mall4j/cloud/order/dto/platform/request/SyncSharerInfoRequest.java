package com.mall4j.cloud.order.dto.platform.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 视频号订单
 */
@Data
public class SyncSharerInfoRequest {

    @ApiModelProperty("orderId")
    private Long orderId;
    @ApiModelProperty("sharerOpenid")
    private String sharerOpenid;

}
