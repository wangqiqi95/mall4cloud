package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 微信触点二维码表
 *
 * @author FrozenWatermelon
 * @date 2022-03-09 16:04:27
 */
public class WeixinQrcodeTentacle extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 触点链接
     */
    private String tentaclePath;

    /**
     * 接收邮箱
     */
    private String recevieEmail;

	/**
	 * 二维码压缩包路径
	 */
    private String codeZipPath;

    /**
     * 状态： 0可用 1不可用
     */
    private Integer status;

    /**
     * 删除标识0-正常,1-已删除
     */
    private Integer delFlag;

	public String getCodeZipPath() {
		return codeZipPath;
	}

	public void setCodeZipPath(String codeZipPath) {
		this.codeZipPath = codeZipPath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTentaclePath() {
		return tentaclePath;
	}

	public void setTentaclePath(String tentaclePath) {
		this.tentaclePath = tentaclePath;
	}

	public String getRecevieEmail() {
		return recevieEmail;
	}

	public void setRecevieEmail(String recevieEmail) {
		this.recevieEmail = recevieEmail;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		return "WeixinQrcodeTentacle{" +
				"id=" + id +
				",name=" + name +
				",tentaclePath=" + tentaclePath +
				",recevieEmail=" + recevieEmail +
				",status=" + status +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",delFlag=" + delFlag +
				'}';
	}
}
