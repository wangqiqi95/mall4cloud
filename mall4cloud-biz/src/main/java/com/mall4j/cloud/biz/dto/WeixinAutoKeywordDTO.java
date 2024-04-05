package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 微信关键字表DTO
 *
 * @author FrozenWatermelon
 * @date 2022-01-19 16:06:52
 */
public class WeixinAutoKeywordDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("规则名称")
    private String name;

    @ApiModelProperty("关键字(英文逗号,分隔开)")
    private String keyword;

    @ApiModelProperty("原始公众号id")
    private String appId;

    @ApiModelProperty("关键字类型1:全匹配  2：模糊匹配")
    private String keywordType;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String updateBy;

    @ApiModelProperty("是否启用： 0否 1是")
    private Integer isWork;

    @ApiModelProperty("删除标识0-正常,1-已删除")
    private Integer delFlag;

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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getKeywordType() {
		return keywordType;
	}

	public void setKeywordType(String keywordType) {
		this.keywordType = keywordType;
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

	public Integer getIsWork() {
		return isWork;
	}

	public void setIsWork(Integer isWork) {
		this.isWork = isWork;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public String toString() {
		return "WeixinAutoKeywordDTO{" +
				"id=" + id +
				",name=" + name +
				",keyword=" + keyword +
				",appId=" + appId +
				",keywordType=" + keywordType +
				",createBy=" + createBy +
				",updateBy=" + updateBy +
				",isWork=" + isWork +
				",delFlag=" + delFlag +
				'}';
	}
}
