package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 二维码数据信息
 *
 * @author cl
 * @date 2021-08-13 15:32:12
 */
public class QrcodeTicket extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 二维码ticket id
     */
    private Long ticketId;

    /**
     * 二维码ticket
     */
    private String ticket;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 二维码类型（1. 小程序团购页面）
     */
    private Integer type;

    /**
     * 二维码实际内容
     */
    private String content;

    /**
     * 这个二维码要跳转的url
     */
    private String ticketUrl;

	public Long getTicketId() {
		return ticketId;
	}

	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTicketUrl() {
		return ticketUrl;
	}

	public void setTicketUrl(String ticketUrl) {
		this.ticketUrl = ticketUrl;
	}

	@Override
	public String toString() {
		return "QrcodeTicket{" +
				"ticketId=" + ticketId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",ticket=" + ticket +
				",expireTime=" + expireTime +
				",type=" + type +
				",content=" + content +
				",ticketUrl=" + ticketUrl +
				'}';
	}
}
