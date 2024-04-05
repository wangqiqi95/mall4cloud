package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 联营分佣申请dto
 *
 * @author Zhang Fan
 * @date 2022/8/5 15:12
 */
@ApiModel("联营分佣申请dto")
@Data
public class DistributionJointVentureCommissionApplyDTO {

    @ApiModelProperty("订单时间上界")
    @NotNull(message = "订单时间上界不能为空")
    private Date orderTimeUpperBound;

    @ApiModelProperty("订单时间下界")
    @NotNull(message = "订单时间下界不能为空")
    private Date orderTimeLowerBound;

    @ApiModelProperty("订单id集合")
    private List<Long> selectedOrderIdList;

    @ApiModelProperty("订单成交金额")
    @NotNull(message = "订单成交金额不能为空")
    private Long orderTurnover;

    @ApiModelProperty("分佣比例")
    @NotNull(message = "分佣比例不能为空")
    private Long commissionRate;

    @ApiModelProperty("分佣金额")
    @NotNull(message = "分佣金额不能为空")
    private Long commissionAmount;

    @ApiModelProperty("客户id")
    @NotNull(message = "客户id不能为空")
    private Long customerId;

    @ApiModelProperty("客户姓名")
    private String customerName;

    @ApiModelProperty("客户手机号")
    private String customerPhone;

}
