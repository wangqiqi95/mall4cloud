package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 公众号事件推送回复日志DTO
 *
 * @author FrozenWatermelon
 * @date 2022-02-07 09:43:36
 */
public class WeixinActoinReplyLogsDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("推送日志id")
    private String actionLog;

    @ApiModelProperty("开发者微信号")
    private String toUserName;

    @ApiModelProperty("发送方账号(openId)")
    private String fromUserName;

    @ApiModelProperty("消息类型(文本:text/图片:image/语音:voice/视频:video/小视频:shortvideo/地理位置:location/链接:link/事件:event)")
    private String msgType;

    @ApiModelProperty("文本消息内容")
    private String content;

    @ApiModelProperty("消息ID")
    private String msgId;

    @ApiModelProperty("图片链接")
    private String picUrl;

    @ApiModelProperty("消息媒体id")
    private String mediaId;

    @ApiModelProperty("消息缩略图媒体id")
    private String thumbmediaid;

    @ApiModelProperty("语音格式")
    private String format;

    @ApiModelProperty("地理位置纬度")
    private String locatioinX;

    @ApiModelProperty("地理位置经度")
    private String locatioinY;

    @ApiModelProperty("地理位置经度")
    private String precision;

    @ApiModelProperty("地图缩放大小")
    private String scale;

    @ApiModelProperty("地理位置信息")
    private String label;

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty("语音识别结果")
    private String recognition;

    @ApiModelProperty("消息描述")
    private String description;

    @ApiModelProperty("消息链接")
    private String url;

    @ApiModelProperty("事件类型：subscribe:订阅/unsubscribe:取消订阅/SCAN:扫码/LOCATION:上报地理位置/CLICK/VIEW")
    private String event;

    @ApiModelProperty("事件key值")
    private String eventKey;

    @ApiModelProperty("二维码ticket")
    private String ticket;

    @ApiModelProperty("0正常 1删除")
    private Integer delFlag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActionLog() {
		return actionLog;
	}

	public void setActionLog(String actionLog) {
		this.actionLog = actionLog;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getThumbmediaid() {
		return thumbmediaid;
	}

	public void setThumbmediaid(String thumbmediaid) {
		this.thumbmediaid = thumbmediaid;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getLocatioinX() {
		return locatioinX;
	}

	public void setLocatioinX(String locatioinX) {
		this.locatioinX = locatioinX;
	}

	public String getLocatioinY() {
		return locatioinY;
	}

	public void setLocatioinY(String locatioinY) {
		this.locatioinY = locatioinY;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRecognition() {
		return recognition;
	}

	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		return "WeixinActoinReplyLogsDTO{" +
				"id=" + id +
				",actionLog=" + actionLog +
				",toUserName=" + toUserName +
				",fromUserName=" + fromUserName +
				",msgType=" + msgType +
				",content=" + content +
				",msgId=" + msgId +
				",picUrl=" + picUrl +
				",mediaId=" + mediaId +
				",thumbmediaid=" + thumbmediaid +
				",format=" + format +
				",locatioinX=" + locatioinX +
				",locatioinY=" + locatioinY +
				",precision=" + precision +
				",scale=" + scale +
				",label=" + label +
				",title=" + title +
				",recognition=" + recognition +
				",description=" + description +
				",url=" + url +
				",event=" + event +
				",eventKey=" + eventKey +
				",ticket=" + ticket +
				",delFlag=" + delFlag +
				'}';
	}
}
