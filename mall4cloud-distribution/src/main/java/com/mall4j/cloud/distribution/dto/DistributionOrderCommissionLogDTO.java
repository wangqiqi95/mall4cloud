package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 订单分销佣金日志VO
 *
 * @author Zhang Fan
 * @date 2022/9/9 14:27
 */
@ApiModel("订单分销佣金日志VO")
@Data
public class DistributionOrderCommissionLogDTO {

    @ApiModelProperty("支付时间开始")
    private Date payTimeStart;

    @ApiModelProperty("支付时间结束")
    private Date payTimeEnd;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("订单id集")
    private List<Long> orderIdList;

    @ApiModelProperty("导购姓名/手机号/工号")
    private String userSearchKey;

    @ApiModelProperty("佣金类型 1-分销佣金 2-发展佣金")
    @NotNull(message = "佣金类型字段不能为空")
    private Integer commissionType;

    @ApiModelProperty("佣金状态 1-已结算 2-已提现 3-退佣金")
    private Integer commissionStatus;

    @ApiModelProperty("所属门店id集")
    private List<Long> storeIdList;

}
