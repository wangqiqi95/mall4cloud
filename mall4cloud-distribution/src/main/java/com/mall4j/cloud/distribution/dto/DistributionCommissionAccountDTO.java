package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 佣金管理-佣金账户DTO
 *
 * @author ZengFanChang
 * @date 2021-12-05 19:44:22
 */
@ToString
@Data
public class DistributionCommissionAccountDTO{

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

    @ApiModelProperty("门店名称")
	private String storeName;

	@ApiModelProperty("待结算佣金")
	private Long waitCommission;

	@ApiModelProperty("待结算分销佣金")
	private Long waitDistributionCommission;

	@ApiModelProperty("待结算发展佣金")
	private Long waitDevelopingCommission;

	@ApiModelProperty("可提现佣金")
	private Long canWithdraw;

	@ApiModelProperty("可提现分销佣金")
	private Long canWithdrawDistribution;

	@ApiModelProperty("可提现发展佣金")
	private Long canWithdrawDeveloping;

	@ApiModelProperty("已提现佣金")
	private Long alreadyWithdraw;

	@ApiModelProperty("已提现需退还佣金")
	private Long withdrawNeedRefund;

	@ApiModelProperty("累计提现佣金")
	private Long totalWithdraw;

	@ApiModelProperty("状态 0禁用 1启用")
	private Integer status;

	@ApiModelProperty("提现最小数")
	private Integer withdrawMin;

    @ApiModelProperty("提现最大数")
    private Integer withdrawMax;
}
