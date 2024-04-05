package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 佣金配置-统一佣金VO
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
public class DistributionCommissionUnityVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("组织ID")
    private Long orgId;

    @ApiModelProperty("导购佣金")
    private Double guideRate;

    @ApiModelProperty("微客佣金")
    private Double shareRate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Double getGuideRate() {
		return guideRate;
	}

	public void setGuideRate(Double guideRate) {
		this.guideRate = guideRate;
	}

	public Double getShareRate() {
		return shareRate;
	}

	public void setShareRate(Double shareRate) {
		this.shareRate = shareRate;
	}

	@Override
	public String toString() {
		return "DistributionCommissionUnityVO{" +
				"id=" + id +
				",orgId=" + orgId +
				",guideRate=" + guideRate +
				",shareRate=" + shareRate +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
