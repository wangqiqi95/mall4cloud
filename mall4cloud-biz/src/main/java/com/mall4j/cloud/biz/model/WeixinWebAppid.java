package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 微信公众号表
 *
 * @author FrozenWatermelon
 * @date 2021-12-28 17:28:24
 */
public class WeixinWebAppid extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String id;

    /**
     * 公众号
     */
    private String appId;

    /**
     * 名称
     */
    private String name;

    /**
     * 应用类型
     */
    private String applicationType;

    /**
     * 微信二维码图片
     */
    private String qrcodeimg;

    /**
     * 微信号
     */
    private String weixinNumber;

    /**
     * 微信AppId
     */
    private String weixinAppid;

    /**
     * 微信AppSecret
     */
    private String weixinAppsecret;

    /**
     * 公众号类型
     */
    private String accountType;

    /**
     * 是否认证
     */
    private String authStatus;

    /**
     * Access_Token
     */
    private String accessToken;

    /**
     * token获取的时间
     */
    private Date tokenGettime;

    /**
     * api凭证
     */
    private String apiticket;

    /**
     * apiticket获取时间
     */
    private Date apiticketGettime;

    /**
     * jsapiticket
     */
    private String jsapiticket;

    /**
     * jsapiticket获取时间
     */
    private Date jsapiticketGettime;

    /**
     * 类型：1手动授权，2扫码授权
     */
    private String authType;

    /**
     * 功能的开通状况：1代表已开通
     */
    private String businessInfo;

    /**
     * 公众号授权给开发者的权限集
     */
    private String funcInfo;

    /**
     * 授权方昵称
     */
    private String nickName;

    /**
     * 授权方头像
     */
    private String headimgurl;

    /**
     * 授权信息
     */
    private String authorizationInfo;

    /**
     * 刷新token
     */
    private String authorizerRefreshToken;

    /**
     * 令牌
     */
    private String token;

    /**
     * 授权状态（1正常，2已取消）
     */
    private String authorizationStatus;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public String getQrcodeimg() {
		return qrcodeimg;
	}

	public void setQrcodeimg(String qrcodeimg) {
		this.qrcodeimg = qrcodeimg;
	}

	public String getWeixinNumber() {
		return weixinNumber;
	}

	public void setWeixinNumber(String weixinNumber) {
		this.weixinNumber = weixinNumber;
	}

	public String getWeixinAppid() {
		return weixinAppid;
	}

	public void setWeixinAppid(String weixinAppid) {
		this.weixinAppid = weixinAppid;
	}

	public String getWeixinAppsecret() {
		return weixinAppsecret;
	}

	public void setWeixinAppsecret(String weixinAppsecret) {
		this.weixinAppsecret = weixinAppsecret;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(String authStatus) {
		this.authStatus = authStatus;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Date getTokenGettime() {
		return tokenGettime;
	}

	public void setTokenGettime(Date tokenGettime) {
		this.tokenGettime = tokenGettime;
	}

	public String getApiticket() {
		return apiticket;
	}

	public void setApiticket(String apiticket) {
		this.apiticket = apiticket;
	}

	public Date getApiticketGettime() {
		return apiticketGettime;
	}

	public void setApiticketGettime(Date apiticketGettime) {
		this.apiticketGettime = apiticketGettime;
	}

	public String getJsapiticket() {
		return jsapiticket;
	}

	public void setJsapiticket(String jsapiticket) {
		this.jsapiticket = jsapiticket;
	}

	public Date getJsapiticketGettime() {
		return jsapiticketGettime;
	}

	public void setJsapiticketGettime(Date jsapiticketGettime) {
		this.jsapiticketGettime = jsapiticketGettime;
	}

	public String getAuthType() {
		return authType;
	}

	public void setAuthType(String authType) {
		this.authType = authType;
	}

	public String getBusinessInfo() {
		return businessInfo;
	}

	public void setBusinessInfo(String businessInfo) {
		this.businessInfo = businessInfo;
	}

	public String getFuncInfo() {
		return funcInfo;
	}

	public void setFuncInfo(String funcInfo) {
		this.funcInfo = funcInfo;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getAuthorizationInfo() {
		return authorizationInfo;
	}

	public void setAuthorizationInfo(String authorizationInfo) {
		this.authorizationInfo = authorizationInfo;
	}

	public String getAuthorizerRefreshToken() {
		return authorizerRefreshToken;
	}

	public void setAuthorizerRefreshToken(String authorizerRefreshToken) {
		this.authorizerRefreshToken = authorizerRefreshToken;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAuthorizationStatus() {
		return authorizationStatus;
	}

	public void setAuthorizationStatus(String authorizationStatus) {
		this.authorizationStatus = authorizationStatus;
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
		return "WeixinWebAppid{" +
				"id=" + id +
				",appId=" + appId +
				",name=" + name +
				",applicationType=" + applicationType +
				",qrcodeimg=" + qrcodeimg +
				",weixinNumber=" + weixinNumber +
				",weixinAppid=" + weixinAppid +
				",weixinAppsecret=" + weixinAppsecret +
				",accountType=" + accountType +
				",authStatus=" + authStatus +
				",accessToken=" + accessToken +
				",tokenGettime=" + tokenGettime +
				",apiticket=" + apiticket +
				",apiticketGettime=" + apiticketGettime +
				",jsapiticket=" + jsapiticket +
				",jsapiticketGettime=" + jsapiticketGettime +
				",authType=" + authType +
				",businessInfo=" + businessInfo +
				",funcInfo=" + funcInfo +
				",nickName=" + nickName +
				",headimgurl=" + headimgurl +
				",authorizationInfo=" + authorizationInfo +
				",authorizerRefreshToken=" + authorizerRefreshToken +
				",token=" + token +
				",authorizationStatus=" + authorizationStatus +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",updateBy=" + updateBy +
				",updateTime=" + updateTime +
				",delFlag=" + delFlag +
				'}';
	}
}
