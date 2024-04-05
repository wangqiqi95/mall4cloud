package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 微信图文模板表
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:54:22
 */
public class WeixinNewsTemplate extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private String id;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板类型：1单图文 2多图文
     */
    private String templateType;

    /**
     * 模板来源： 0微信图文素材 1自动回复素材
     */
    private Integer fromType;

    /**
     * 图文素材媒体id
     */
    private String mediaId;

    /**
     * 公众号原始id
     */
    private String appId;

    /**
     * 上传状态 "0"未上传，"1"上传中，"2"上传成功，"3"上传失败
     */
    private String uploadType;

    /**
     * 上传时间
     */
    private Date uploadTime;

    /**
     * 创建人名称
     */
    private String createBy;

    /**
     * 修改人名称
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

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public Integer getFromType() {
		return fromType;
	}

	public void setFromType(Integer fromType) {
		this.fromType = fromType;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
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
		return "WeixinNewsTemplate{" +
				"id=" + id +
				",templateName=" + templateName +
				",templateType=" + templateType +
				",fromType=" + fromType +
				",mediaId=" + mediaId +
				",appId=" + appId +
				",uploadType=" + uploadType +
				",uploadTime=" + uploadTime +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",updateBy=" + updateBy +
				",updateTime=" + updateTime +
				",delFlag=" + delFlag +
				'}';
	}
}
