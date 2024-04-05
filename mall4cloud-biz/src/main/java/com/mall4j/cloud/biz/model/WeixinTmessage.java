package com.mall4j.cloud.biz.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 微信消息模板
 *
 * @author FrozenWatermelon
 * @date 2022-02-08 16:17:14
 */
public class WeixinTmessage extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 消息内容标题
     */
    private String title;

	/**
	 * 模板标题
	 */
	private String templateTitle;

    /**
     * 模板所属行业的一级行业
     */
    private String primaryIndustry;

    /**
     * 模板所属行业的二级行业
     */
    private String deputyIndustry;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 模板示例
     */
    private String example;

    /**
     * 公众号id
     */
    private String appId;

    /**
     * 模板用途：1公众号 2小程序
     */
    private Integer dataSrc;

    /**
     * 0禁用 1启用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remarks;

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

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPrimaryIndustry() {
		return primaryIndustry;
	}

	public void setPrimaryIndustry(String primaryIndustry) {
		this.primaryIndustry = primaryIndustry;
	}

	public String getDeputyIndustry() {
		return deputyIndustry;
	}

	public void setDeputyIndustry(String deputyIndustry) {
		this.deputyIndustry = deputyIndustry;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getDataSrc() {
		return dataSrc;
	}

	public void setDataSrc(Integer dataSrc) {
		this.dataSrc = dataSrc;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getTemplateTitle() {
		return templateTitle;
	}

	public void setTemplateTitle(String templateTitle) {
		this.templateTitle = templateTitle;
	}

	@Override
	public String toString() {
		return "WeixinTmessage{" +
				"id=" + id +
				",templateId=" + templateId +
				",title=" + title +
				",primaryIndustry=" + primaryIndustry +
				",deputyIndustry=" + deputyIndustry +
				",content=" + content +
				",example=" + example +
				",appId=" + appId +
				",dataSrc=" + dataSrc +
				",status=" + status +
				",remarks=" + remarks +
				",createBy=" + createBy +
				",createTime=" + createTime +
				",updateBy=" + updateBy +
				",updateTime=" + updateTime +
				",delFlag=" + delFlag +
				'}';
	}
}
