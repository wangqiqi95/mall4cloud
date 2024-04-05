package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 分销员绑定关系DTO
 *
 * @author cl
 * @date 2021-08-09 14:14:09
 */
public class DistributionUserBindDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户关系表")
    private Long bindId;

    @ApiModelProperty("分销员id")
    private Long distributionUserId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("当前绑定关系(-1失效 0 预绑定 1生效)")
    private Integer state;

    @ApiModelProperty("失效原因(0 超过有效期 1抢客 2 管理员更改 3.暂时封禁)")
    private Integer invalidReason;

    @ApiModelProperty("绑定时间")
    private Date bindTime;

    @ApiModelProperty("失效时间")
    private Date invalidTime;

    @ApiModelProperty("分销员昵称")
    private String nickName;

	@ApiModelProperty("排序字段：分销管理-分销员-关系查询： 0无 1绑定时间 2失效时间")
	private Integer sortParam;

	@ApiModelProperty("排序类型 0无 1 正序 2倒序")
	private Integer sortType;

	public Long getBindId() {
		return bindId;
	}

	public void setBindId(Long bindId) {
		this.bindId = bindId;
	}

	public Long getDistributionUserId() {
		return distributionUserId;
	}

	public void setDistributionUserId(Long distributionUserId) {
		this.distributionUserId = distributionUserId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getInvalidReason() {
		return invalidReason;
	}

	public void setInvalidReason(Integer invalidReason) {
		this.invalidReason = invalidReason;
	}

	public Date getBindTime() {
		return bindTime;
	}

	public void setBindTime(Date bindTime) {
		this.bindTime = bindTime;
	}

	public Date getInvalidTime() {
		return invalidTime;
	}

	public void setInvalidTime(Date invalidTime) {
		this.invalidTime = invalidTime;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getSortParam() {
		return sortParam;
	}

	public void setSortParam(Integer sortParam) {
		this.sortParam = sortParam;
	}

	public Integer getSortType() {
		return sortType;
	}

	public void setSortType(Integer sortType) {
		this.sortType = sortType;
	}

	@Override
	public String toString() {
		return "DistributionUserBindDTO{" +
				"bindId=" + bindId +
				", distributionUserId=" + distributionUserId +
				", userId=" + userId +
				", state=" + state +
				", invalidReason=" + invalidReason +
				", bindTime=" + bindTime +
				", invalidTime=" + invalidTime +
				", nickName='" + nickName + '\'' +
				", sortParam=" + sortParam +
				", sortType=" + sortType +
				'}';
	}
}
