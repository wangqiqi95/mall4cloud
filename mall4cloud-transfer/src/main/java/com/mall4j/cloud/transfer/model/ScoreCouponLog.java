package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 积分换券兑换记录
 *
 * @author FrozenWatermelon
 * @date 2022-04-14 15:16:46
 */
public class ScoreCouponLog extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long logId;

    /**
     * 兑换活动id
     */
    private Long convertId;

    /**
     * 积分换券活动类型（0：积分兑礼/1：积分换券）
     */
    private Integer type;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户卡号 (冗余字段)
     */
    private String userCardNumber;

    /**
     * 用户手机号 (冗余字段)
     */
    private String userPhone;

    /**
     * 兑换积分数
     */
    private Long convertScore;

    /**
     * 备注
     */
    private String note;

    /**
     * 创建人ID
     */
    private Long createUserId;

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Long getConvertId() {
		return convertId;
	}

	public void setConvertId(Long convertId) {
		this.convertId = convertId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserCardNumber() {
		return userCardNumber;
	}

	public void setUserCardNumber(String userCardNumber) {
		this.userCardNumber = userCardNumber;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public Long getConvertScore() {
		return convertScore;
	}

	public void setConvertScore(Long convertScore) {
		this.convertScore = convertScore;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	@Override
	public String toString() {
		return "ScoreCouponLog{" +
				"logId=" + logId +
				",convertId=" + convertId +
				",type=" + type +
				",userId=" + userId +
				",userCardNumber=" + userCardNumber +
				",userPhone=" + userPhone +
				",convertScore=" + convertScore +
				",note=" + note +
				",createUserId=" + createUserId +
				",createTime=" + createTime +
				'}';
	}
}
