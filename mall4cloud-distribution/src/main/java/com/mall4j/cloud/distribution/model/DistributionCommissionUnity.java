package com.mall4j.cloud.distribution.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 佣金配置-统一佣金
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
public class DistributionCommissionUnity extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 导购佣金
     */
    private BigDecimal guideRate;

    /**
     * 微客佣金
     */
    private BigDecimal shareRate;

	/**
	 * 发展佣金
	 */
	private BigDecimal developRate;

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
		return "DistributionCommissionUnity{" +
				"id=" + id +
				",orgId=" + orgId +
				",guideRate=" + guideRate +
				",shareRate=" + shareRate +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
