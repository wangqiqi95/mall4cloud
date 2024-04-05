package com.mall4j.cloud.user.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 用户积分获取记录VO
 *
 * @author FrozenWatermelon
 * @date 2021-05-17 17:17:14
 */
public class UserScoreGetLogVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("积分明细表")
    private Long userScoreGetLogId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("可用积分")
    private Long usableScore;

    @ApiModelProperty("业务id")
    private String bizId;

    @ApiModelProperty("过期时间")
    private Date expireTime;

    @ApiModelProperty("状态  -1过期 0订单抵现 1正常")
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
		return "UserScoreGetLogVO{" +
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
