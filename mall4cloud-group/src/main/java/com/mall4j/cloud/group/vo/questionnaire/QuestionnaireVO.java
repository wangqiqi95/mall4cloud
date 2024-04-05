package com.mall4j.cloud.group.vo.questionnaire;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 问卷信息表VO
 *
 * @author FrozenWatermelon
 * @date 2023-05-08 14:10:59
 */
public class QuestionnaireVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("问卷id")
    private Long id;

    @ApiModelProperty("状态 0-未启用，1-已启用")
    private Integer status;

    @ApiModelProperty("问卷名称")
    private String name;

    @ApiModelProperty("活动开始时间")
    private Date activityBeginTime;

    @ApiModelProperty("活动截止时间")
    private Date activityEndTime;

    @ApiModelProperty("是否全部门店")
    private Integer isAllShop;

    @ApiModelProperty("备注")
    private String remarks;

    @ApiModelProperty("不在白名单提示")
    private String unInWhite;

    @ApiModelProperty("未注册会员提示")
    private String unRegTip;

    @ApiModelProperty("奖品发放提示")
    private String giftGrantTip;

    @ApiModelProperty("活动开始提示")
    private String beginTip;

    @ApiModelProperty("")
    private Long createId;

    @ApiModelProperty("")
    private String createName;

    @ApiModelProperty("")
    private Long updateId;

    @ApiModelProperty("")
    private String updateName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getActivityBeginTime() {
		return activityBeginTime;
	}

	public void setActivityBeginTime(Date activityBeginTime) {
		this.activityBeginTime = activityBeginTime;
	}

	public Date getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(Date activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	public Integer getIsAllShop() {
		return isAllShop;
	}

	public void setIsAllShop(Integer isAllShop) {
		this.isAllShop = isAllShop;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUnInWhite() {
		return unInWhite;
	}

	public void setUnInWhite(String unInWhite) {
		this.unInWhite = unInWhite;
	}

	public String getUnRegTip() {
		return unRegTip;
	}

	public void setUnRegTip(String unRegTip) {
		this.unRegTip = unRegTip;
	}

	public String getGiftGrantTip() {
		return giftGrantTip;
	}

	public void setGiftGrantTip(String giftGrantTip) {
		this.giftGrantTip = giftGrantTip;
	}

	public String getBeginTip() {
		return beginTip;
	}

	public void setBeginTip(String beginTip) {
		this.beginTip = beginTip;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	@Override
	public String toString() {
		return "QuestionnaireVO{" +
				"id=" + id +
				",status=" + status +
				",name=" + name +
				",activityBeginTime=" + activityBeginTime +
				",activityEndTime=" + activityEndTime +
				",isAllShop=" + isAllShop +
				",remarks=" + remarks +
				",unInWhite=" + unInWhite +
				",unRegTip=" + unRegTip +
				",giftGrantTip=" + giftGrantTip +
				",beginTip=" + beginTip +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",createId=" + createId +
				",createName=" + createName +
				",updateId=" + updateId +
				",updateName=" + updateName +
				'}';
	}
}
