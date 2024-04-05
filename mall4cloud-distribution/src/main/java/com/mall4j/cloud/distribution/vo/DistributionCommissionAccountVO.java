package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 佣金管理-佣金账户VO
 *
 * @author ZengFanChang
 * @date 2021-12-05 19:44:22
 */
@Data
public class DistributionCommissionAccountVO{

    @ApiModelProperty("累计佣金(分)")
    private Long totalCommission = 0l;

    @ApiModelProperty("待结算佣金(分)")
    private Long waitCommission = 0l;

    @ApiModelProperty("已结算佣金(分)")
    private Long alreadyCommission = 0l;

    @ApiModelProperty("可提现佣金(分)")
    private Long canWithdraw = 0l;

    @ApiModelProperty("已提现佣金(分)")
    private Long alreadyWithdraw  = 0l;

    @ApiModelProperty("佣金显示开关 0-否 1-是")
    private Integer commissionSwitch;

}
