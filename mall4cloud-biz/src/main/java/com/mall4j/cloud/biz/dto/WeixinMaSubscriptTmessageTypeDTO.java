package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 微信小程序订阅模版消息类型DTO
 *
 * @author FrozenWatermelon
 * @date 2022-03-06 13:14:50
 */
public class WeixinMaSubscriptTmessageTypeDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("小程序appid")
    private String appId;

    @ApiModelProperty("0禁用 1启用")
    private Integer status;

    @ApiModelProperty("删除标识0-正常,1-已删除")
    private Integer delFlag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
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
		return "WeixinMaSubscriptTmessageTypeDTO{" +
				"id=" + id +
				",title=" + title +
				",appId=" + appId +
				",status=" + status +
				",delFlag=" + delFlag +
				'}';
	}
}
