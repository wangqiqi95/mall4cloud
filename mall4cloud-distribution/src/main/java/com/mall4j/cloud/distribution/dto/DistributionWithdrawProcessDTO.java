package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 佣金处理批次DTO
 *
 * @author ZengFanChang
 * @date 2021-12-10 22:02:48
 */
@Data
public class DistributionWithdrawProcessDTO{

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("表格名称")
    private String batchName;

    @ApiModelProperty("成功数量")
    private Integer successQuantity;

    @ApiModelProperty("失败数量")
    private Integer failQuantity;

    @ApiModelProperty("导入时间")
    private Date importDate;

    @ApiModelProperty("执行状态 0待执行 1已执行")
    private Integer executeStatus;

	@ApiModelProperty("导入开始时间")
	private Date importStartDate;

	@ApiModelProperty("导入结束时间")
	private Date importEndDate;

}
