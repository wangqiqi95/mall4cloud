package com.mall4j.cloud.biz.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 微信图文模板表VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-18 10:54:22
 */
public class WeixinNewsTemplateVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("模板类型：1单图文 2多图文")
    private String templateType;

    @ApiModelProperty("模板来源： 0微信图文素材 1自动回复素材")
    private Integer fromType;

    @ApiModelProperty("图文素材媒体id")
    private String mediaId;

    @ApiModelProperty("公众号原始id")
    private String appId;

    @ApiModelProperty("上传状态 0未上传，1上传中，2上传成功，3上传失败")
    private String uploadType;

    @ApiModelProperty("上传时间")
    private Date uploadTime;

    @ApiModelProperty("创建人名称")
    private String createBy;

    @ApiModelProperty("修改人名称")
    private String updateBy;

    @ApiModelProperty("删除标识0-正常,1-已删除")
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
		return "WeixinNewsTemplateVO{" +
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
