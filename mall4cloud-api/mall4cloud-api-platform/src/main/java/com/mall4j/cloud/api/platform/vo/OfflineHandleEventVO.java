package com.mall4j.cloud.api.platform.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * 下线处理事件VO
 *
 * @author YXF
 * @date 2021-01-15 17:46:26
 */
public class OfflineHandleEventVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("事件id")
    private Long eventId;

    @ApiModelProperty("1 商品 2 店铺 3满减 4.优惠券 5.团购 6.分销 7.秒杀 9.自提点")
    private Integer handleType;

    @ApiModelProperty("处理id，如果是商品就是商品id，店铺就是店铺id")
    private Long handleId;

    @ApiModelProperty("关联店铺id")
    private Long shopId;

    @ApiModelProperty("处理人id")
    private Long handlerId;

    @ApiModelProperty("处理人")
    private String handler;

    @ApiModelProperty("处理状态 1平台进行下线 2 重新申请，等待审核 3.审核通过 4 审核未通过")
    private Integer status;

    @ApiModelProperty("下线原因")
    private String offlineReason;

    @ApiModelProperty("下线原因")
    private List<OfflineHandleEventItemVO> offlineHandleEventItemList;


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

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
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

	public List<OfflineHandleEventItemVO> getOfflineHandleEventItemList() {
		return offlineHandleEventItemList;
	}

	public void setOfflineHandleEventItemList(List<OfflineHandleEventItemVO> offlineHandleEventItemList) {
		this.offlineHandleEventItemList = offlineHandleEventItemList;
	}

	@Override
	public String toString() {
		return "OfflineHandleEventVO{" +
				"eventId=" + eventId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",handleType=" + handleType +
				",handleId=" + handleId +
				",shopId=" + shopId +
				",handlerId=" + handlerId +
				",status=" + status +
				",offlineReason=" + offlineReason +
				",handler=" + handler +
				",offlineHandleEventItemList=" + offlineHandleEventItemList +
				'}';
	}
}
