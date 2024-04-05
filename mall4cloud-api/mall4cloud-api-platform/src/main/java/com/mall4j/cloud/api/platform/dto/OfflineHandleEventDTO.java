package com.mall4j.cloud.api.platform.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 下线处理事件DTO
 *
 * @author YXF
 * @date 2021-01-15 17:46:26
 */
public class OfflineHandleEventDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("事件id")
    private Long eventId;

	@ApiModelProperty("处理id——spuId/shopId...")
    private Long handleId;

	@ApiModelProperty("下线原因")
    private String offlineReason;

	@ApiModelProperty("拒绝理由")
    private String refuseReason;

	@ApiModelProperty("申请理由")
    private String reapplyReason;

	@ApiModelProperty("3.审核通过 4 审核未通过")
	private Integer status;
	/**
	 * 处理人id
	 */
	private Long handlerId;

	@ApiModelProperty("关联的店铺id")
	private Long shopId;

	/**
	 * 1 商品 2 店铺 3满减 4.优惠券 5.团购 6.分销 7.秒杀 9.自提点
	 */
	private Integer handleType;

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Integer getHandleType() {
		return handleType;
	}

	public void setHandleType(Integer handleType) {
		this.handleType = handleType;
	}

	public Long getHandleId() {
		return handleId;
	}

	public void setHandleId(Long handleId) {
		this.handleId = handleId;
	}

	public Long getHandlerId() {
		return handlerId;
	}

	public void setHandlerId(Long handlerId) {
		this.handlerId = handlerId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOfflineReason() {
		return offlineReason;
	}

	public void setOfflineReason(String offlineReason) {
		this.offlineReason = offlineReason;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public String getReapplyReason() {
		return reapplyReason;
	}

	public void setReapplyReason(String reapplyReason) {
		this.reapplyReason = reapplyReason;
	}

	@Override
	public String toString() {
		return "OfflineHandleEventDTO{" +
				"eventId=" + eventId +
				", handleId=" + handleId +
				", offlineReason='" + offlineReason + '\'' +
				", refuseReason='" + refuseReason + '\'' +
				", reapplyReason='" + reapplyReason + '\'' +
				", status=" + status +
				", handlerId=" + handlerId +
				", shopId=" + shopId +
				", handleType=" + handleType +
				'}';
	}
}
