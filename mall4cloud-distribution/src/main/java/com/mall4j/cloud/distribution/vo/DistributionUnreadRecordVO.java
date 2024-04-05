package com.mall4j.cloud.distribution.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销推广-用户未读信息VO
 *
 * @author ZengFanChang
 * @date 2022-01-23 22:16:06
 */
public class DistributionUnreadRecordVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("身份类型 1导购 2微客")
    private Integer identityType;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("浏览未读数量")
    private Integer unreadBrowseNum;

    @ApiModelProperty("加购未读数量")
    private Integer unreadPurchaseNum;

    @ApiModelProperty("下单未读数量")
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
		return "DistributionUnreadRecordVO{" +
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
