package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 联营分佣申请查询dto
 *
 * @author Zhang Fan
 * @date 2022/8/5 15:10
 */
@ApiModel("联营分佣申请查询dto")
@Data
public class DistributionJointVentureCommissionSearchDTO {

    @ApiModelProperty("申请编号")
    private String applyNo;

    @ApiModelProperty("订单时间上界")
    private Date orderTimeUpperBound;

    @ApiModelProperty("订单时间下界")
    private Date orderTimeLowerBound;

    @ApiModelProperty("客户id")
    private Long customerId;

    @ApiModelProperty("客户姓名")
    private String customerName;

    @ApiModelProperty("客户手机号")
    private String customerPhone;

    @ApiModelProperty("状态 0-待审核 1-待付款 2-付款成功 9-审核失败")
    private Integer status;
}
