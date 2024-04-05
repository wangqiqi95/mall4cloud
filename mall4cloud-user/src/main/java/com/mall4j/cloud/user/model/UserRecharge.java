package com.mall4j.cloud.user.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 余额充值级别表
 *
 * @author FrozenWatermelon
 * @date 2021-04-27 15:51:36
 */
public class UserRecharge extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 充值id
     */
    private Long rechargeId;

    /**
     * 充值余额标题
     */
    private String rechargeTitle;

    /**
     * 充值金额
     */
    private Long rechargeAmount;

    /**
     * 背景图片
     */
    private String img;

    /**
     * 赠送金额
     */
    private Long presAmount;

    /**
     * 赠送积分
     */
    private Long presScore;

    /**
     * 赠送成长值
     */
    private Integer presGrowth;

    /**
     * 顺序
     */
    private Integer seq;

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

	@Override
	public String toString() {
		return "UserRecharge{" +
				"rechargeId=" + rechargeId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",rechargeTitle=" + rechargeTitle +
				",rechargeAmount=" + rechargeAmount +
				",img=" + img +
				",presAmount=" + presAmount +
				",presScore=" + presScore +
				",presGrowth=" + presGrowth +
				",seq=" + seq +
				'}';
	}
}
