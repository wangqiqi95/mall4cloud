package com.mall4j.cloud.api.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 联营分佣查询dto
 *
 * @author
 */
@ApiModel("联营分佣成功支付及成功退款")
@Data
public class DistributionJointVentureOrderPayRefundRespDTO {

    @ApiModelProperty("总支付金额")
    private Long payActualTotal;

    @ApiModelProperty("总支付运费")
    private Long freightAmountTotal;

    @ApiModelProperty("总退款金额")
    private Long refundAmountTotal;

}
