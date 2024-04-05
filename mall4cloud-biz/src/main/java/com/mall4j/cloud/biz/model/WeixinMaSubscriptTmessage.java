package com.mall4j.cloud.biz.model;

import com.mall4j.cloud.common.model.BaseModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
/**
 * 微信小程序订阅模版消息
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 17:03:44
 */
public class WeixinMaSubscriptTmessage extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

	/**
	 * 业务类型：1会员业务 2优惠券业务 3订单业务 4退单业务 5线下活动  6积分商城 7每日签到 8直播通知 9微客审核
	 */
	private Integer businessType;

    /**
     * 模版业务类型id
     */
    private String templateTypeId;
    /**
     * 小程序模版code
     */
    private String templateCode;

    /**
     * 100.会员生日到期提醒 200.优惠券到期提醒 201.优惠券到账提醒 300.积分变更提醒 301.等级到期提醒  400.订单发货提醒  401.订单签收提醒 500.退单审核结果提醒 501.退单完成提醒 600.活动上新提醒 601.活动开始提醒
     */
    private Integer sendType;

    /**
     * 模板标题
     */
    private String templateTitle;

    /**
     * 模板示例
     */
    private String example;

    /**
     * 小程序appid
     */
    private String appId;

    /**
     * 要跳转的页面地址，可以拼接参数。
     */
    private String page;

	/**
	 * 系统模板名称
	 */
    private String title;

	/**
	 * 模版类型，2 为一次性订阅，3 为长期订阅
	 */
    private Integer type;

	/**
	 * 模版内容
	 */
    private String content;

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

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTemplateTypeId() {
		return templateTypeId;
	}

	public void setTemplateTypeId(String templateTypeId) {
		this.templateTypeId = templateTypeId;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public String getTemplateTitle() {
		return templateTitle;
	}

	public void setTemplateTitle(String templateTitle) {
		this.templateTitle = templateTitle;
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

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
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

	@Override
	public String toString() {
		return "WeixinMaSubscriptTmessage{" +
				"id=" + id +
				",templateTypeId=" + templateTypeId +
				",sendType=" + sendType +
				",templateTitle=" + templateTitle +
				",example=" + example +
				",appId=" + appId +
				",page=" + page +
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
