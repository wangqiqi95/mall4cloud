package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 佣金配置-活动佣金VO
 *
 * @author gww
 * @date 2021-12-09 18:01:37
 */
public class DistributionCommissionActivityVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("活动名称")
    private String activityName;

    @ApiModelProperty("组织ID")
    private Long orgId;

    @ApiModelProperty("导购佣金状态 0-禁用 1-启用")
    private Integer guideRateStatus;

    @ApiModelProperty("导购佣金")
    private Double guideRate;

    @ApiModelProperty("微客佣金状态 0-禁用 1-启用")
    private Integer shareRateStatus;

    @ApiModelProperty("微客佣金")
    private Double shareRate;

    @ApiModelProperty("活动状态 0-失效 1-生效")
    private Integer status;

    @ApiModelProperty("活动时间类型 0-长期生效 1-指定时间段生效")
    private Integer limitTimeType;

    @ApiModelProperty("适用门店类型 0-所有门店 1-指定门店 ")
    private Integer limitStoreType;

    @ApiModelProperty("商品范围类型 0-所有商品 1-指定商品 ")
    private Integer limitSpuType;

    @ApiModelProperty("活动开始时间")
    private Date startTime;

    @ApiModelProperty("活动结束时间")
    private Date endTime;

    @ApiModelProperty("备注")
    private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Integer getGuideRateStatus() {
		return guideRateStatus;
	}

	public void setGuideRateStatus(Integer guideRateStatus) {
		this.guideRateStatus = guideRateStatus;
	}

	public Double getGuideRate() {
		return guideRate;
	}

	public void setGuideRate(Double guideRate) {
		this.guideRate = guideRate;
	}

	public Integer getShareRateStatus() {
		return shareRateStatus;
	}

	public void setShareRateStatus(Integer shareRateStatus) {
		this.shareRateStatus = shareRateStatus;
	}

	public Double getShareRate() {
		return shareRate;
	}

	public void setShareRate(Double shareRate) {
		this.shareRate = shareRate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getLimitTimeType() {
		return limitTimeType;
	}

	public void setLimitTimeType(Integer limitTimeType) {
		this.limitTimeType = limitTimeType;
	}

	public Integer getLimitStoreType() {
		return limitStoreType;
	}

	public void setLimitStoreType(Integer limitStoreType) {
		this.limitStoreType = limitStoreType;
	}

	public Integer getLimitSpuType() {
		return limitSpuType;
	}

	public void setLimitSpuType(Integer limitSpuType) {
		this.limitSpuType = limitSpuType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "DistributionCommissionActivityVO{" +
				"id=" + id +
				",activityName=" + activityName +
				",orgId=" + orgId +
				",guideRateStatus=" + guideRateStatus +
				",guideRate=" + guideRate +
				",shareRateStatus=" + shareRateStatus +
				",shareRate=" + shareRate +
				",status=" + status +
				",limitTimeType=" + limitTimeType +
				",limitStoreType=" + limitStoreType +
				",limitSpuType=" + limitSpuType +
				",startTime=" + startTime +
				",endTime=" + endTime +
				",remark=" + remark +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
