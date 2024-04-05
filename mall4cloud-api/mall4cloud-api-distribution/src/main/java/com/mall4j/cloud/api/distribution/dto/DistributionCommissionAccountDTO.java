package com.mall4j.cloud.api.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 佣金管理-佣金账户DTO
 *
 * @author ZengFanChang
 * @date 2021-12-05 19:44:22
 */
@Data
public class DistributionCommissionAccountDTO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("身份类型 1导购 2威客")
    private Integer identityType;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("姓名")
    private String username;

    @ApiModelProperty("工号/卡号")
    private String userNumber;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("门店ID")
    private Long storeId;

    @ApiModelProperty("待结算佣金")
    private Long waitCommission;

    @ApiModelProperty("可提现佣金")
    private Long canWithdraw;

    @ApiModelProperty("已提现佣金")
    private Long alreadyWithdraw;

    @ApiModelProperty("累计提现佣金")
    private Long totalWithdraw;

    @ApiModelProperty("提现待回退佣金")
    private Long withdrawNeedRefund;

    @ApiModelProperty("状态 0禁用 1启用")
    private Integer status;

    @ApiModelProperty("提现最小数")
    private Integer withdrawMin;

    @ApiModelProperty("提现最大数")
    private Integer withdrawMax;

}
