package com.mall4j.cloud.biz.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;

/**
 * 短信记录表
 *
 * @author lhd
 * @date 2021-01-04 13:36:52
 */
public class SmsLog extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 手机号码
     */
    private String userPhone;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 手机验证码
     */
    private String mobileCode;

    /**
     * 短信类型  1:注册  2:验证
     */
    private Integer type;

    /**
     * 发送短信返回码
     */
    private String responseCode;

    /**
     * 状态  1:有效  0：失效
     */
    private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMobileCode() {
		return mobileCode;
	}

	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "SmsLogVO{" +
				"id=" + id +
				",userId=" + userId +
				",userPhone=" + userPhone +
				",content=" + content +
				",mobileCode=" + mobileCode +
				",type=" + type +
				",responseCode=" + responseCode +
				",status=" + status +
				'}';
	}
}
