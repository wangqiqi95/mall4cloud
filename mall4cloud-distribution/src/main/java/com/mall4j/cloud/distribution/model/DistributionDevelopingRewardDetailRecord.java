package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销推广-发展奖励发展明细
 *
 * @author ZengFanChang
 * @date 2021-12-26 21:39:02
 */
public class DistributionDevelopingRewardDetailRecord extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 发展奖励活动ID
     */
    private Long developingRewardId;

    /**
     * 发展奖励明细ID
     */
    private Long developingRewardDetailId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 是否通过 1是 0否
     */
    private Integer pass;

    /**
     * 通过时间
     */
    private Date passTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDevelopingRewardId() {
		return developingRewardId;
	}

	public void setDevelopingRewardId(Long developingRewardId) {
		this.developingRewardId = developingRewardId;
	}

	public Long getDevelopingRewardDetailId() {
		return developingRewardDetailId;
	}

	public void setDevelopingRewardDetailId(Long developingRewardDetailId) {
		this.developingRewardDetailId = developingRewardDetailId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Integer getPass() {
		return pass;
	}

	public void setPass(Integer pass) {
		this.pass = pass;
	}

	public Date getPassTime() {
		return passTime;
	}

	public void setPassTime(Date passTime) {
		this.passTime = passTime;
	}

	@Override
	public String toString() {
		return "DistributionDevelopingRewardDetailRecord{" +
				"id=" + id +
				",developingRewardId=" + developingRewardId +
				",developingRewardDetailId=" + developingRewardDetailId +
				",userId=" + userId +
				",userName=" + userName +
				",mobile=" + mobile +
				",applyTime=" + applyTime +
				",pass=" + pass +
				",passTime=" + passTime +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				'}';
	}
}
