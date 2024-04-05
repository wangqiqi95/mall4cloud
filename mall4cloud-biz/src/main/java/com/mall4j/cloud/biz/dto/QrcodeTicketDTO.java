package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 二维码数据信息DTO
 *
 * @author cl
 * @date 2021-08-13 15:32:12
 */
public class QrcodeTicketDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("二维码ticket id")
    private Long ticketId;

    @ApiModelProperty("二维码ticket")
    private String ticket;

    @ApiModelProperty("过期时间")
    private Date expireTime;

    @ApiModelProperty("二维码类型（1. 小程序团购页面）")
    private Integer type;

    @ApiModelProperty("二维码实际内容")
    private String content;

    @ApiModelProperty("这个二维码要跳转的url")
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
		return "QrcodeTicketDTO{" +
				"ticketId=" + ticketId +
				",ticket=" + ticket +
				",expireTime=" + expireTime +
				",type=" + type +
				",content=" + content +
				",ticketUrl=" + ticketUrl +
				'}';
	}
}
