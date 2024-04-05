package com.mall4j.cloud.api.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Zhang Fan
 * @date 2022/9/9 09:58
 */
@Data
public class CalculateDistributionCommissionResultVO {

    @ApiModelProperty("待结算分销佣金")
    private Long waitDistributionCommission;

    @ApiModelProperty("待结算发展佣金")
    private Long waitDevelopingCommission;

    @ApiModelProperty("可提现分销佣金")
    private Long canWithdrawDistribution;

    @ApiModelProperty("可提现发展佣金")
    private Long canWithdrawDeveloping;
}
