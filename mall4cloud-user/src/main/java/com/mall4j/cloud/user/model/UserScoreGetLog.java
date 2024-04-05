package com.mall4j.cloud.user.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 用户积分获取记录
 *
 * @author FrozenWatermelon
 * @date 2021-05-17 17:17:14
 */
public class UserScoreGetLog extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 积分明细表
     */
    private Long userScoreGetLogId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 可用积分
     */
    private Long usableScore;

    /**
     * 业务id
     */
    private String bizId;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 状态  -1过期 0订单抵现 1正常
     */
    private Integer status;

	public Long getUserScoreGetLogId() {
		return userScoreGetLogId;
	}

	public void setUserScoreGetLogId(Long userScoreGetLogId) {
		this.userScoreGetLogId = userScoreGetLogId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUsableScore() {
		return usableScore;
	}

	public void setUsableScore(Long usableScore) {
		this.usableScore = usableScore;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserScoreGetLog{" +
				"userScoreGetLogId=" + userScoreGetLogId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",userId=" + userId +
				",usableScore=" + usableScore +
				",bizId=" + bizId +
				",expireTime=" + expireTime +
				",status=" + status +
				'}';
	}
}
