package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 佣金提现订单信息DTO
 *
 * @author ZengFanChang
 * @date 2021-12-11 19:35:15
 */
@Data
public class DistributionWithdrawOrderDTO{

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("身份类型 1导购 2威客")
    private Integer identityType;

    @ApiModelProperty("提现记录ID")
    private Long recordId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("订单号")
    private String  orderNo;

	@ApiModelProperty("商品名称")
	private String productName;

	@ApiModelProperty("分销关系 1分享关系 2服务关系 3自主下单 4代客下单")
	private Integer distributionRelation;
}
