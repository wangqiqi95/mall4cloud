package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 微信二维码表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-28 22:25:17
 */
public class WeixinQrcodeDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("二维码标题（详情）")
    private String actionInfo;

    @ApiModelProperty("二维码类型：QR_SCENE：临时整型，QR_STR_SCENE：临时字符串，QR_LIMIT_SCENE：永久整型，QR_LIMIT_STR_SCENE：永久字符串")
    private String actionName;

    @ApiModelProperty("整型场景值ID")
    private Integer sceneId;

    @ApiModelProperty("字符串场景值ID")
    private String sceneStr;

    @ApiModelProperty("场景值来源")
    private Integer sceneSrc;

    @ApiModelProperty("二维码ticket")
    private String ticket;

    @ApiModelProperty("有效时间(秒)")
    private Integer expireSeconds;

    @ApiModelProperty("过期时间")
    private Date expireDate;

    @ApiModelProperty("二维码地址")
    private String qrcodeUrl;

    @ApiModelProperty("公众帐号ID")
    private String appId;

    @ApiModelProperty("触发类型：text文本/news图文")
    private String msgType;

    @ApiModelProperty("文本内容")
    private String textContent;

    @ApiModelProperty("图文选择类型（1：自定义，2：选择模板）")
    private String actionNewsType;

    @ApiModelProperty("图文标题")
    private String newsTitle;

    @ApiModelProperty("图文摘要")
    private String newsDesc;

    @ApiModelProperty("图文封面图")
    private String newsImg;

    @ApiModelProperty("图文跳转链接")
    private String newsUrl;

    @ApiModelProperty("图文选择模板ID")
    private String newsTemplateid;

    @ApiModelProperty("标签，逗号隔开")
    private String tags;

    @ApiModelProperty("触点门店")
    private String storeId;

    @ApiModelProperty("二维码中间logo")
    private String logoUrl;

    @ApiModelProperty("有效期类型： 1永久 2临时")
    private Integer isExpire;

    @ApiModelProperty("状态： 0待审核 1审核通过")
    private Integer status;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String updateBy;

    @ApiModelProperty("删除标识0-正常,1-已删除")
    private Integer delFlag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActionInfo() {
		return actionInfo;
	}

	public void setActionInfo(String actionInfo) {
		this.actionInfo = actionInfo;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public Integer getSceneId() {
		return sceneId;
	}

	public void setSceneId(Integer sceneId) {
		this.sceneId = sceneId;
	}

	public String getSceneStr() {
		return sceneStr;
	}

	public void setSceneStr(String sceneStr) {
		this.sceneStr = sceneStr;
	}

	public Integer getSceneSrc() {
		return sceneSrc;
	}

	public void setSceneSrc(Integer sceneSrc) {
		this.sceneSrc = sceneSrc;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Integer getExpireSeconds() {
		return expireSeconds;
	}

	public void setExpireSeconds(Integer expireSeconds) {
		this.expireSeconds = expireSeconds;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getQrcodeUrl() {
		return qrcodeUrl;
	}

	public void setQrcodeUrl(String qrcodeUrl) {
		this.qrcodeUrl = qrcodeUrl;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public String getActionNewsType() {
		return actionNewsType;
	}

	public void setActionNewsType(String actionNewsType) {
		this.actionNewsType = actionNewsType;
	}

	public String getNewsTitle() {
		return newsTitle;
	}

	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	public String getNewsDesc() {
		return newsDesc;
	}

	public void setNewsDesc(String newsDesc) {
		this.newsDesc = newsDesc;
	}

	public String getNewsImg() {
		return newsImg;
	}

	public void setNewsImg(String newsImg) {
		this.newsImg = newsImg;
	}

	public String getNewsUrl() {
		return newsUrl;
	}

	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}

	public String getNewsTemplateid() {
		return newsTemplateid;
	}

	public void setNewsTemplateid(String newsTemplateid) {
		this.newsTemplateid = newsTemplateid;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public Integer getIsExpire() {
		return isExpire;
	}

	public void setIsExpire(Integer isExpire) {
		this.isExpire = isExpire;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		return "WeixinQrcodeDTO{" +
				"id=" + id +
				",actionInfo=" + actionInfo +
				",actionName=" + actionName +
				",sceneId=" + sceneId +
				",sceneStr=" + sceneStr +
				",sceneSrc=" + sceneSrc +
				",ticket=" + ticket +
				",expireSeconds=" + expireSeconds +
				",expireDate=" + expireDate +
				",qrcodeUrl=" + qrcodeUrl +
				",appId=" + appId +
				",msgType=" + msgType +
				",textContent=" + textContent +
				",actionNewsType=" + actionNewsType +
				",newsTitle=" + newsTitle +
				",newsDesc=" + newsDesc +
				",newsImg=" + newsImg +
				",newsUrl=" + newsUrl +
				",newsTemplateid=" + newsTemplateid +
				",tags=" + tags +
				",storeId=" + storeId +
				",logoUrl=" + logoUrl +
				",isExpire=" + isExpire +
				",status=" + status +
				",createBy=" + createBy +
				",updateBy=" + updateBy +
				",delFlag=" + delFlag +
				'}';
	}
}
