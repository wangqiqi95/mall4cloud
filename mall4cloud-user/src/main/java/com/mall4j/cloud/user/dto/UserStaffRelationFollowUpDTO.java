package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 会员跟进记录DTO
 *
 * @author FrozenWatermelon
 * @date 2023-11-13 17:37:14
 */
public class UserStaffRelationFollowUpDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("好友关联表id")
    private Long relationId;

    @ApiModelProperty("修改跟进引用的原跟进记录id")
    private Long followUpId;

    @ApiModelProperty("跟进话术内容")
    private String content;

    @ApiModelProperty("引用员工ids")
    private String staffIds;

    @ApiModelProperty("引用订单编号")
    private String orderId;

    @ApiModelProperty("引用聊天记录ids")
    private String chatIds;

    @ApiModelProperty("引用图片")
    private String imgsUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRelationId() {
		return relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}

	public Long getFollowUpId() {
		return followUpId;
	}

	public void setFollowUpId(Long followUpId) {
		this.followUpId = followUpId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStaffIds() {
		return staffIds;
	}

	public void setStaffIds(String staffIds) {
		this.staffIds = staffIds;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getChatIds() {
		return chatIds;
	}

	public void setChatIds(String chatIds) {
		this.chatIds = chatIds;
	}

	public String getImgsUrl() {
		return imgsUrl;
	}

	public void setImgsUrl(String imgsUrl) {
		this.imgsUrl = imgsUrl;
	}

	@Override
	public String toString() {
		return "UserStaffRelationFollowUpDTO{" +
				"id=" + id +
				",relationId=" + relationId +
				",followUpId=" + followUpId +
				",content=" + content +
				",staffIds=" + staffIds +
				",orderId=" + orderId +
				",chatIds=" + chatIds +
				",imgsUrl=" + imgsUrl +
				'}';
	}
}
