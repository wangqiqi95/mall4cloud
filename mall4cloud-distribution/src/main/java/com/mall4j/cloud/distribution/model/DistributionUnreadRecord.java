package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销推广-用户未读信息
 *
 * @author ZengFanChang
 * @date 2022-01-23 22:16:06
 */
public class DistributionUnreadRecord extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 身份类型 1导购 2微客
     */
    private Integer identityType;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 浏览未读数量
     */
    private Integer unreadBrowseNum;

    /**
     * 加购未读数量
     */
    private Integer unreadPurchaseNum;

    /**
     * 下单未读数量
     */
    private Integer unreadBuyNum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getIdentityType() {
		return identityType;
	}

	public void setIdentityType(Integer identityType) {
		this.identityType = identityType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getUnreadBrowseNum() {
		return unreadBrowseNum;
	}

	public void setUnreadBrowseNum(Integer unreadBrowseNum) {
		this.unreadBrowseNum = unreadBrowseNum;
	}

	public Integer getUnreadPurchaseNum() {
		return unreadPurchaseNum;
	}

	public void setUnreadPurchaseNum(Integer unreadPurchaseNum) {
		this.unreadPurchaseNum = unreadPurchaseNum;
	}

	public Integer getUnreadBuyNum() {
		return unreadBuyNum;
	}

	public void setUnreadBuyNum(Integer unreadBuyNum) {
		this.unreadBuyNum = unreadBuyNum;
	}

	@Override
	public String toString() {
		return "DistributionUnreadRecord{" +
				"id=" + id +
				",identityType=" + identityType +
				",userId=" + userId +
				",unreadBrowseNum=" + unreadBrowseNum +
				",unreadPurchaseNum=" + unreadPurchaseNum +
				",unreadBuyNum=" + unreadBuyNum +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
