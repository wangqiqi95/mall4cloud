package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 用户社交登陆信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-06 14:24:20
 */
public class AuthSocial extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 本系统uid
     */
    private Long uid;

    /**
     * 当账户未绑定时，临时的uid
     */
    private String tempUid;

    /**
     * 员工ID
     */
    private Long staffUid;

    /**
     * 第三方系统类型 1小程序 2 公众号
     */
    private Integer socialType;

    /**
     * 第三方系统昵称
     */
    private String nickName;

    /**
     * 第三方系统头像
     */
    private String imageUrl;

    /**
     * 第三方系统userid
     */
    private String bizUserId;

    /**
     * 第三方系统unionid
     */
    private String bizUnionid;

    /**
     * 有些时候第三方系统授权之后，会有个临时的key，比如小程序的session_key
     */
    private String bizTempSession;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getTempUid() {
		return tempUid;
	}

	public void setTempUid(String tempUid) {
		this.tempUid = tempUid;
	}

	public Long getStaffUid() {
		return staffUid;
	}

	public void setStaffUid(Long staffUid) {
		this.staffUid = staffUid;
	}

	public Integer getSocialType() {
		return socialType;
	}

	public void setSocialType(Integer socialType) {
		this.socialType = socialType;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getBizUserId() {
		return bizUserId;
	}

	public void setBizUserId(String bizUserId) {
		this.bizUserId = bizUserId;
	}

	public String getBizUnionid() {
		return bizUnionid;
	}

	public void setBizUnionid(String bizUnionid) {
		this.bizUnionid = bizUnionid;
	}

	public String getBizTempSession() {
		return bizTempSession;
	}

	public void setBizTempSession(String bizTempSession) {
		this.bizTempSession = bizTempSession;
	}

	@Override
	public String toString() {
		return "AuthSocial{" +
				"id=" + id +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",uid=" + uid +
				",tempUid=" + tempUid +
				",staffUid=" + staffUid +
				",socialType=" + socialType +
				",nickName=" + nickName +
				",imageUrl=" + imageUrl +
				",bizUserId=" + bizUserId +
				",bizUnionid=" + bizUnionid +
				",bizTempSession=" + bizTempSession +
				'}';
	}
}
