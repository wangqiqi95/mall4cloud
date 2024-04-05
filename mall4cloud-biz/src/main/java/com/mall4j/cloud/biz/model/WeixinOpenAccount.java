package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 微信第三方平台账号表
 *
 * @author FrozenWatermelon
 * @date 2021-12-29 11:05:26
 */
public class WeixinOpenAccount extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 
     */
    private String appId;

    /**
     * appSecret
     */
    private String appSecret;

    /**
     * 第三方平台推送 : ticket
     */
    private String ticket;

    /**
     * 
     */
    private Date getTicketTime;

    /**
     * 平台方access_token
     */
    private String componentAccessToken;

	/**
	 * 预授权码
	 */
    private String preAuthCode;

    private String authorizerAccessToken;
    private String authorizerRefreshToken;

	public String getAuthorizerAccessToken() {
		return authorizerAccessToken;
	}

	public void setAuthorizerAccessToken(String authorizerAccessToken) {
		this.authorizerAccessToken = authorizerAccessToken;
	}

	public String getAuthorizerRefreshToken() {
		return authorizerRefreshToken;
	}

	public void setAuthorizerRefreshToken(String authorizerRefreshToken) {
		this.authorizerRefreshToken = authorizerRefreshToken;
	}

	public String getAuthorizerAppId() {
		return authorizerAppId;
	}

	public void setAuthorizerAppId(String authorizerAppId) {
		this.authorizerAppId = authorizerAppId;
	}

	private String authorizerAppId;

    /**
     * 平台方获取access_token时间
     */
    private Date getAccessTokenTime;

    /**
     * 创建人登录名称
     */
    private String createBy;

    /**
     * 修改人登录名称
     */
    private String updateBy;

    /**
     * 删除标识0-正常,1-已删除
     */
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
		return appSecret;
	}

	public void setAppsecret(String appSecret) {
		this.appSecret = appSecret;
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

	public String getPreAuthCode() {
		return preAuthCode;
	}

	public void setPreAuthCode(String preAuthCode) {
		this.preAuthCode = preAuthCode;
	}

	@Override
	public String toString() {
		return "WeixinOpenAccount{" +
				"id=" + id +
				",appId=" + appId +
				",appSecret=" + appSecret +
				",ticket=" + ticket +
				",getTicketTime=" + getTicketTime +
				",componentAccessToken=" + componentAccessToken +
				",preAuthCode=" + preAuthCode +
				",getAccessTokenTime=" + getAccessTokenTime +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",updateBy=" + updateBy +
				",updateTime=" + updateTime +
				",delFlag=" + delFlag +
				'}';
	}
}
