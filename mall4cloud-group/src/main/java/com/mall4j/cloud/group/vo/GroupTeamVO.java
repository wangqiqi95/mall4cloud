package com.mall4j.cloud.group.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 拼团团队表VO
 *
 * @author YXF
 * @date 2021-03-20 10:39:32
 */
public class GroupTeamVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("拼团团队id")
    private Long groupTeamId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("拼团活动id")
    private Long groupActivityId;

    @ApiModelProperty("活动商品Id")
    private Long groupSpuId;

    @ApiModelProperty("已参团人数")
    private Integer joinNum;

    @ApiModelProperty("拼团状态(0:待成团，1:拼团中，2:拼团成功，3:拼团失败)")
    private Integer status;

    @ApiModelProperty("团队订单总额")
    private Long totalPrice;

    @ApiModelProperty("开始时间（团长支付成功时间）")
    private Date startTime;

    @ApiModelProperty("剩余时间")
    private Date endTime;

    @ApiModelProperty("团长user_Id")
    private Long shareUserId;
	/**
	 * 已交易数量
	 */
	@ApiModelProperty("团长user_Id")
	private Integer spuCount;
	/**
	 * 团购订单状态
	 */
	private Integer groupOrderStatus;

	public Long getGroupTeamId() {
		return groupTeamId;
	}

	public void setGroupTeamId(Long groupTeamId) {
		this.groupTeamId = groupTeamId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getGroupActivityId() {
		return groupActivityId;
	}

	public void setGroupActivityId(Long groupActivityId) {
		this.groupActivityId = groupActivityId;
	}

	public Long getGroupSpuId() {
		return groupSpuId;
	}

	public void setGroupSpuId(Long groupSpuId) {
		this.groupSpuId = groupSpuId;
	}

	public Integer getJoinNum() {
		return joinNum;
	}

	public void setJoinNum(Integer joinNum) {
		this.joinNum = joinNum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Long totalPrice) {
		this.totalPrice = totalPrice;
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

	public Long getShareUserId() {
		return shareUserId;
	}

	public void setShareUserId(Long shareUserId) {
		this.shareUserId = shareUserId;
	}

	public Integer getGroupOrderStatus() {
		return groupOrderStatus;
	}

	public void setGroupOrderStatus(Integer groupOrderStatus) {
		this.groupOrderStatus = groupOrderStatus;
	}

	@Override
	public String toString() {
		return "GroupTeamVO{" +
				"groupTeamId=" + groupTeamId +
				",shopId=" + shopId +
				",groupActivityId=" + groupActivityId +
				",groupSpuId=" + groupSpuId +
				",joinNum=" + joinNum +
				",status=" + status +
				",totalPrice=" + totalPrice +
				",startTime=" + startTime +
				",endTime=" + endTime +
				",groupOrderStatus=" + groupOrderStatus +
				",createTime=" + createTime +
				",shareUserId=" + shareUserId +
				",updateTime=" + updateTime +
				'}';
	}
}
