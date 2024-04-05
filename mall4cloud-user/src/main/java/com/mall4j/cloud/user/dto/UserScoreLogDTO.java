package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * 用户积分记录DTO
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public class UserScoreLogDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("日志id")
    private Long logId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("1.购物 2.会员等级提升加积分 3.签到加积分 4.购物抵扣使用积分 5.积分过期")
    private Integer source;

    @ApiModelProperty("变动积分数额")
    private Long score;

    @ApiModelProperty("业务id")
    private Long bizId;

    @ApiModelProperty("出入类型 0=支出 1=收入")
    private Integer ioType;

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Long getBizId() {
		return bizId;
	}

	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}

	public Integer getIoType() {
		return ioType;
	}

	public void setIoType(Integer ioType) {
		this.ioType = ioType;
	}

	@Override
	public String toString() {
		return "UserScoreLogDTO{" +
				"logId=" + logId +
				",userId=" + userId +
				",source=" + source +
				",score=" + score +
				",bizId=" + bizId +
				",ioType=" + ioType +
				'}';
	}
}
