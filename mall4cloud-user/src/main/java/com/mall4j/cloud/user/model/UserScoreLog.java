package com.mall4j.cloud.user.model;

import java.io.Serializable;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 用户积分记录
 *
 * @author YXF
 * @date 2020-12-08 10:16:53
 */
public class UserScoreLog extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 日志id
     */
    private Long logId;

    /**
     * 用户id
     */
    private Long userId;


    private Integer source;

    /**
     * 变动积分数额
     */
    private Long score;

    /**
     * 业务id
     */
    private Long bizId;

    /**
     * 出入类型 0=支出 1=收入
     */
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
		return "UserScoreLog{" +
				"logId=" + logId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",userId=" + userId +
				",source=" + source +
				",score=" + score +
				",bizId=" + bizId +
				",ioType=" + ioType +
				'}';
	}
}
