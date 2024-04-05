package com.mall4j.cloud.platform.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 下线处理事件
 *
 * @author YXF
 * @date 2021-01-15 17:46:26
 */
public class OfflineHandleEvent extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 事件id
     */
    private Long eventId;

    /**
     * 1 商品 2 店铺 3满减 4.优惠券 5.团购 6.分销 7.秒杀 9.自提点
     */
    private Integer handleType;

    /**
     * 处理id，如果是商品就是商品id，店铺就是店铺id
     */
    private Long handleId;

    /**
     * 关联店铺id
     */
    private Long shopId;

    /**
     * 处理人id
     */
    private Long handlerId;

    /**
     * 处理状态 1平台进行下线 2 重新申请，等待审核 3.审核通过 4 审核未通过
     */
    private Integer status;

    /**
     * 下线原因
     */
    private String offlineReason;

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

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
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

	@Override
	public String toString() {
		return "OfflineHandleEvent{" +
				"eventId=" + eventId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",handleType=" + handleType +
				",handleId=" + handleId +
				",shopId=" + shopId +
				",handlerId=" + handlerId +
				",status=" + status +
				",offlineReason=" + offlineReason +
				'}';
	}

}
