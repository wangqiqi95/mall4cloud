package com.mall4j.cloud.user.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
/**
 * 积分锁定信息
 *
 * @author FrozenWatermelon
 * @date 2021-05-19 19:54:55
 */
public class UserScoreLock extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 状态-1已解锁 0待确定 1已锁定
     */
    private Integer status;

    /**
     * 锁定积分数量
     */
    private Long score;

	/**
	 * 变动的积分id集合
	 */
    private String scoreGetLogIds;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public String getScoreGetLogIds() {
		return scoreGetLogIds;
	}

	public void setScoreGetLogIds(String scoreGetLogIds) {
		this.scoreGetLogIds = scoreGetLogIds;
	}

	@Override
	public String toString() {
		return "UserScoreLock{" +
				"id=" + id +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",userId=" + userId +
				",orderId=" + orderId +
				",status=" + status +
				",score=" + score +
				",scoreGetLogIds=" + scoreGetLogIds +
				'}';
	}
}
