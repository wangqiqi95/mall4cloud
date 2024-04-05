package com.mall4j.cloud.order.dto.platform.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.biz.dto.livestore.response.complaint.ComplaintOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 纠纷单分页对象 斯凯奇内部转换对象
 */
@Data
public class ComplaintOrderSKX extends ComplaintOrder {

    @ApiModelProperty("订单编号")
    private String orderNumber;
    @ApiModelProperty("订单id")
    private Long skxOrderId;

    @ApiModelProperty("退单编号")
    private String refundNumber;

    @ApiModelProperty("纠纷单状态用户是否已读")
    private Long complaintUserIsRead;
    @ApiModelProperty("纠纷单状态平台是否已读")
    private Long complaintPlatformIsRead;

}
