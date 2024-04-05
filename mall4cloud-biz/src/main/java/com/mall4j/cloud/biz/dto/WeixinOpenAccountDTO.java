package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 微信第三方平台账号表DTO
 *
 * @author FrozenWatermelon
 * @date 2021-12-29 11:05:26
 */
public class WeixinOpenAccountDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("")
    private String appId;

    @ApiModelProperty("")
    private String appsecret;

    @ApiModelProperty("第三方平台推送 : ticket")
    private String ticket;

    @ApiModelProperty("")
    private Date getTicketTime;

    @ApiModelProperty("平台方access_token")
    private String componentAccessToken;

    @ApiModelProperty("平台方获取access_token时间")
    private Date getAccessTokenTime;

    @ApiModelProperty("创建人登录名称")
    private String createBy;

    @ApiModelProperty("修改人登录名称")
    private String updateBy;

    @ApiModelProperty("删除标识0-正常,1-已删除")
    private Integer delFlag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppsecret() {
		return appsecret;
	}

	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Date getGetTicketTime() {
		return getTicketTime;
	}

	public void setGetTicketTime(Date getTicketTime) {
		this.getTicketTime = getTicketTime;
	}

	public String getComponentAccessToken() {
		return componentAccessToken;
	}

	public void setComponentAccessToken(String componentAccessToken) {
		this.componentAccessToken = componentAccessToken;
	}

	public Date getGetAccessTokenTime() {
		return getAccessTokenTime;
	}

	public void setGetAccessTokenTime(Date getAccessTokenTime) {
		this.getAccessTokenTime = getAccessTokenTime;
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
		return "WeixinOpenAccountDTO{" +
				"id=" + id +
				",appId=" + appId +
				",appsecret=" + appsecret +
				",ticket=" + ticket +
				",getTicketTime=" + getTicketTime +
				",componentAccessToken=" + componentAccessToken +
				",getAccessTokenTime=" + getAccessTokenTime +
				",createBy=" + createBy +
				",updateBy=" + updateBy +
				",delFlag=" + delFlag +
				'}';
	}
}
