package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 佣金配置-统一佣金DTO
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
public class DistributionCommissionUnityDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("导购佣金比例")
	@NotNull(message = "导购佣金比例不能为空")
    private BigDecimal guideRate;

    @ApiModelProperty("微客佣金比例")
	@NotNull(message = "微客佣金比例不能为空")
    private BigDecimal shareRate;

	@ApiModelProperty("发展佣金比例")
	@NotNull(message = "发展佣金比例不能为空")
	private BigDecimal developRate;

	public BigDecimal getGuideRate() {
		return guideRate;
	}

	public void setGuideRate(BigDecimal guideRate) {
		this.guideRate = guideRate;
	}

	public BigDecimal getShareRate() {
		return shareRate;
	}

	public void setShareRate(BigDecimal shareRate) {
		this.shareRate = shareRate;
	}

	public BigDecimal getDevelopRate() {
		return developRate;
	}

	public void setDevelopRate(BigDecimal developRate) {
		this.developRate = developRate;
	}

	@Override
	public String toString() {
		return "DistributionCommissionUnityDTO{" +
				"guideRate=" + guideRate +
				",shareRate=" + shareRate +
				'}';
	}
}
