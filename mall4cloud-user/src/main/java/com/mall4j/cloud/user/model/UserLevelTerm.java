package com.mall4j.cloud.user.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;

/**
 * 
 *
 * @author FrozenWatermelon
 * @date 2021-04-25 16:01:58
 */
public class UserLevelTerm extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long levelTermId;

    /**
     * 等级id
     */
    private Long userLevelId;

    /**
     * 期数类型(0:月,1:季,2:年)
     */
    private Integer termType;

    /**
     * 价格
     */
    private Long needAmount;

	/**
	 * 状态(0: 禁用1: 启用)
	 */
	private Integer status;

	public Long getLevelTermId() {
		return levelTermId;
	}

	public void setLevelTermId(Long levelTermId) {
		this.levelTermId = levelTermId;
	}

	public Long getUserLevelId() {
		return userLevelId;
	}

	public void setUserLevelId(Long userLevelId) {
		this.userLevelId = userLevelId;
	}

	public Integer getTermType() {
		return termType;
	}

	public void setTermType(Integer termType) {
		this.termType = termType;
	}

	public Long getNeedAmount() {
		return needAmount;
	}

	public void setNeedAmount(Long needAmount) {
		this.needAmount = needAmount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "UserLevelTerm{" +
				"levelTermId=" + levelTermId +
				",createTime=" + createTime +
				",userLevelId=" + userLevelId +
				",termType=" + termType +
				",needAmount=" + needAmount +
				",status=" + status +
				'}';
	}
}
