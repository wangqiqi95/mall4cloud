package com.mall4j.cloud.distribution.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 分销公共信息
 *
 * @author cl
 * @date 2021-08-09 14:14:05
 */
public class DistributionMsg extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 公告表
     */
    private Long msgId;

    /**
     * 公告标题
     */
    private String msgTitle;

    /**
     * 指定上线时间
     */
    private Date startTime;

    /**
     * 指定下线时间
     */
    private Date endTime;

    /**
     * 是否置顶(0 不置顶 1 置顶)
     */
    private Integer isTop;

    /**
     * 公告内容
     */
    private String content;

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "DistributionMsg{" +
				"msgId=" + msgId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",msgTitle=" + msgTitle +
				",startTime=" + startTime +
				",endTime=" + endTime +
				",isTop=" + isTop +
				",content=" + content +
				'}';
	}
}
