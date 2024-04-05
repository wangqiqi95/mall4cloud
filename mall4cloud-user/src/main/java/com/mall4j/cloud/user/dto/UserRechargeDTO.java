package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

/**
 * 余额充值级别表DTO
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
public class UserRechargeDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("充值id")
    private Long rechargeId;

    @ApiModelProperty("充值余额标题")
    private String rechargeTitle;

    @ApiModelProperty("充值金额")
    private Long rechargeAmount;

    @ApiModelProperty("背景图片")
    private String img;

    @ApiModelProperty("赠送金额")
    private Long presAmount;

    @ApiModelProperty("赠送积分")
    private Long presScore;

    @ApiModelProperty("赠送成长值")
    private Integer presGrowth;

    @ApiModelProperty("顺序")
    private Integer seq;

	@ApiModelProperty("优惠券信息")
	private List<RechargeCouponDTO> couponList;

	public Long getRechargeId() {
		return rechargeId;
	}

	public void setRechargeId(Long rechargeId) {
		this.rechargeId = rechargeId;
	}

	public String getRechargeTitle() {
		return rechargeTitle;
	}

	public void setRechargeTitle(String rechargeTitle) {
		this.rechargeTitle = rechargeTitle;
	}

	public Long getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(Long rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Long getPresAmount() {
		return presAmount;
	}

	public void setPresAmount(Long presAmount) {
		this.presAmount = presAmount;
	}

	public Long getPresScore() {
		return presScore;
	}

	public void setPresScore(Long presScore) {
		this.presScore = presScore;
	}

	public Integer getPresGrowth() {
		return presGrowth;
	}

	public void setPresGrowth(Integer presGrowth) {
		this.presGrowth = presGrowth;
	}

	public Integer getSeq() {
		return seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public List<RechargeCouponDTO> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<RechargeCouponDTO> couponList) {
		this.couponList = couponList;
	}

	@Override
	public String toString() {
		return "UserRechargeDTO{" +
				"rechargeId=" + rechargeId +
				",rechargeTitle=" + rechargeTitle +
				",rechargeAmount=" + rechargeAmount +
				",img=" + img +
				",presAmount=" + presAmount +
				",presScore=" + presScore +
				",presGrowth=" + presGrowth +
				",seq=" + seq +
				",couponList=" + couponList +
				'}';
	}
}
