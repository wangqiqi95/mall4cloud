package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 微信二维码扫码记录表
 *
 * @author FrozenWatermelon
 * @date 2022-01-25 18:13:49
 */
public class WeixinQrcodeScanRecord extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String headimgurl;

    /**
     * openid
     */
    private String openid;

    /**
     * 扫码时间
     */
    private Date scanTime;

    /**
     * 场景值ID
     */
    private String sceneId;

    /**
     * 场景值来源
     */
    private Integer sceneSrc;

    /**
     * 公众号ID
     */
    private String appId;

    /**
     * 是否扫码关注 0:非扫码关注,1:扫码关注
     */
    private String isScanSubscribe;

	/**
	 * 二维码id
	 */
	private String qrcodeId;

	/**
	 * 二维码ticket
	 */
	private String ticket;

	/**
	 * 扫码自动回复规则id
	 */
	private String autoScanId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 修改人
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

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Date getScanTime() {
		return scanTime;
	}

	public void setScanTime(Date scanTime) {
		this.scanTime = scanTime;
	}

	public String getSceneId() {
		return sceneId;
	}

	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}

	public Integer getSceneSrc() {
		return sceneSrc;
	}

	public void setSceneSrc(Integer sceneSrc) {
		this.sceneSrc = sceneSrc;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getIsScanSubscribe() {
		return isScanSubscribe;
	}

	public void setIsScanSubscribe(String isScanSubscribe) {
		this.isScanSubscribe = isScanSubscribe;
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

	public String getQrcodeId() {
		return qrcodeId;
	}

	public void setQrcodeId(String qrcodeId) {
		this.qrcodeId = qrcodeId;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getAutoScanId() {
		return autoScanId;
	}

	public void setAutoScanId(String autoScanId) {
		this.autoScanId = autoScanId;
	}

	@Override
	public String toString() {
		return "WeixinQrcodeScanRecord{" +
				"id=" + id +
				",nickName=" + nickName +
				",headimgurl=" + headimgurl +
				",openid=" + openid +
				",scanTime=" + scanTime +
				",sceneId=" + sceneId +
				",sceneSrc=" + sceneSrc +
				",appId=" + appId +
				",isScanSubscribe=" + isScanSubscribe +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",updateBy=" + updateBy +
				",updateTime=" + updateTime +
				",delFlag=" + delFlag +
				'}';
	}
}
