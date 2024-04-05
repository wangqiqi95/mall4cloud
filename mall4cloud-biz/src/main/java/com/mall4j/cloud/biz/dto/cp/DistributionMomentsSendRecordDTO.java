package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 朋友圈 员工发送记录表DTO
 *
 * @author FrozenWatermelon
 * @date 2023-11-03 14:22:45
 */
public class DistributionMomentsSendRecordDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("朋友圈ID")
    private Long momentsId;

    @ApiModelProperty("员工id")
    private Long staffId;

    @ApiModelProperty("是否发送状态 0否 1已经发送")
    private Integer status;

    @ApiModelProperty("异步任务id，最大长度为64字节，24小时有效")
    private String qwJobId;

    @ApiModelProperty("企微朋友圈ID")
    private String qwMomentsId;

    @ApiModelProperty("企微发表状态 0:未发表 1：已发表")
    private Integer qwPublishStatus;

    @ApiModelProperty("企微朋友圈评论数量")
    private Integer qwCommentNum;

    @ApiModelProperty("企微朋友圈点赞数量")
    private Integer qwLikeNum;

    @ApiModelProperty("发送时间")
    private Date sendTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMomentsId() {
		return momentsId;
	}

	public void setMomentsId(Long momentsId) {
		this.momentsId = momentsId;
	}

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId) {
		this.staffId = staffId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getQwJobId() {
		return qwJobId;
	}

	public void setQwJobId(String qwJobId) {
		this.qwJobId = qwJobId;
	}

	public String getQwMomentsId() {
		return qwMomentsId;
	}

	public void setQwMomentsId(String qwMomentsId) {
		this.qwMomentsId = qwMomentsId;
	}

	public Integer getQwPublishStatus() {
		return qwPublishStatus;
	}

	public void setQwPublishStatus(Integer qwPublishStatus) {
		this.qwPublishStatus = qwPublishStatus;
	}

	public Integer getQwCommentNum() {
		return qwCommentNum;
	}

	public void setQwCommentNum(Integer qwCommentNum) {
		this.qwCommentNum = qwCommentNum;
	}

	public Integer getQwLikeNum() {
		return qwLikeNum;
	}

	public void setQwLikeNum(Integer qwLikeNum) {
		this.qwLikeNum = qwLikeNum;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	@Override
	public String toString() {
		return "DistributionMomentsSendRecordDTO{" +
				"id=" + id +
				",momentsId=" + momentsId +
				",staffId=" + staffId +
				",status=" + status +
				",qwJobId=" + qwJobId +
				",qwMomentsId=" + qwMomentsId +
				",qwPublishStatus=" + qwPublishStatus +
				",qwCommentNum=" + qwCommentNum +
				",qwLikeNum=" + qwLikeNum +
				",sendTime=" + sendTime +
				'}';
	}
}
