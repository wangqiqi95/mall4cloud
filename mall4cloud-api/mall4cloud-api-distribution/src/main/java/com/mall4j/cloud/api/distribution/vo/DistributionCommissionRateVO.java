package com.mall4j.cloud.api.distribution.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributionCommissionRateVO {

    @ApiModelProperty("导购佣金比例活动ID")
    private Long guideRateActivityId;
    @ApiModelProperty("微客佣金比例活动ID")
    private Long shareRateActivityId;
    @ApiModelProperty("发展佣金比例活动ID")
    private Long developRateActivityId;

    @ApiModelProperty("导购佣金比例")
    private BigDecimal guideRate;
    @ApiModelProperty("微客佣金比例")
    private BigDecimal shareRate;
    @ApiModelProperty("发展佣金比例")
    private BigDecimal developRate;

    @ApiModelProperty("导购佣金状态 0-禁用 1-启用")
    private Integer guideRateStatus;
    @ApiModelProperty("微客佣金状态 0-禁用 1-启用")
    private Integer shareRateStatus;
    @ApiModelProperty("发展佣金状态 0-禁用 1-启用")
    private Integer developRateStatus;

    public DistributionCommissionRateVO(BigDecimal guideRate, BigDecimal shareRate, BigDecimal developRate, Integer guideRateStatus, Integer shareRateStatus,
                                        Integer developRateStatus) {
        this.guideRate = guideRate;
        this.shareRate = shareRate;
        this.developRate = developRate;
        this.guideRateStatus = guideRateStatus;
        this.shareRateStatus = shareRateStatus;
        this.developRateStatus = developRateStatus;
    }
}
