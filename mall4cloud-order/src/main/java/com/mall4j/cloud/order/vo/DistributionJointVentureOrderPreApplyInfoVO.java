package com.mall4j.cloud.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 联营分佣待申请信息
 *
 * @author Zhang Fan
 * @date 2022/8/12 16:02
 */
@ApiModel("联营分佣待申请信息")
@Data
public class DistributionJointVentureOrderPreApplyInfoVO {

    @ApiModelProperty("订单成交金额")
    private Long orderTurnover;

    @ApiModelProperty("总订单成交金额")
    private Long totalOrderTurnover;

    @ApiModelProperty("已分佣订单成交金额")
    private Long dividedOrderTurnover;

    @ApiModelProperty("分佣比例")
    private Long commissionRate;

    @ApiModelProperty("分佣金额")
    private Long commissionAmount;

    @ApiModelProperty("已分佣订单分佣金额")
    private Long dividedCommissionAmount;
}
