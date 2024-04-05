package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单分销佣金日志订单子项
 *
 * @author Zhang Fan
 * @date 2022/9/9 14:18
 */
@ApiModel("订单分销佣金日志订单子项")
@Data
public class DistributionOrderCommissionLogItemVO {

    @ApiModelProperty(value = "中文商品名称")
    private String spuName;

    @ApiModelProperty(value = "商品佣金比例")
    private BigDecimal commissionRate;
}
