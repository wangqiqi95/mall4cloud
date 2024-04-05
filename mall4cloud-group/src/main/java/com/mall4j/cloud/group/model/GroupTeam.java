package com.mall4j.cloud.group.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 拼团团队表
 *
 * @author YXF
 * @date 2021-03-20 10:39:32
 */
public class GroupTeam extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 拼团团队id
     */
    private Long groupTeamId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 拼团活动id
     */
    private Long groupActivityId;

    /**
     * 活动商品Id
     */
    private Long groupSpuId;

    /**
     * 已参团人数
     */
    private Integer joinNum;

    /**
     * 拼团状态(0:待成团，1:拼团中，2:拼团成功，3:拼团失败)
     */
    private Integer status;

    /**
     * 团队订单总额
     */
    private Long totalPrice;

    /**
     * 开始时间（团长支付成功时间）
     */
    private Date startTime;

    /**
     * 剩余时间
     */
    private Date endTime;

    /**
     * 团长user_Id
     */
    private Long shareUserId;

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

	@Override
	public String toString() {
		return "GroupTeam{" +
				"groupTeamId=" + groupTeamId +
				",shopId=" + shopId +
				",groupActivityId=" + groupActivityId +
				",groupSpuId=" + groupSpuId +
				",joinNum=" + joinNum +
				",status=" + status +
				",totalPrice=" + totalPrice +
				",startTime=" + startTime +
				",endTime=" + endTime +
				",createTime=" + createTime +
				",shareUserId=" + shareUserId +
				",updateTime=" + updateTime +
				'}';
	}
}
