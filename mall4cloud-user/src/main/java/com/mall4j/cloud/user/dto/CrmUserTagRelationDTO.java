package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

/**
 * 数云维护 用户标签关系表DTO
 *
 * @author FrozenWatermelon
 * @date 2023-11-25 10:41:27
 */
public class CrmUserTagRelationDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("")
    private String id;

    @ApiModelProperty("目录id")
    private String categoryid;

    @ApiModelProperty("标签id")
    private String tagid;

    @ApiModelProperty("标签名称")
    private String tagname;

    @ApiModelProperty("标签值")
    private Long tagvalue;

    @ApiModelProperty("原始用户id")
    private String originid;

    @ApiModelProperty("微信unionId")
    private Integer unionid;

    @ApiModelProperty("是否有效Y/N")
    private String enable;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}

	public String getTagid() {
		return tagid;
	}

	public void setTagid(String tagid) {
		this.tagid = tagid;
	}

	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	public Long getTagvalue() {
		return tagvalue;
	}

	public void setTagvalue(Long tagvalue) {
		this.tagvalue = tagvalue;
	}

	public String getOriginid() {
		return originid;
	}

	public void setOriginid(String originid) {
		this.originid = originid;
	}

	public Integer getUnionid() {
		return unionid;
	}

	public void setUnionid(Integer unionid) {
		this.unionid = unionid;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {
		return "CrmUserTagRelationDTO{" +
				"id=" + id +
				",categoryid=" + categoryid +
				",tagid=" + tagid +
				",tagname=" + tagname +
				",tagvalue=" + tagvalue +
				",originid=" + originid +
				",unionid=" + unionid +
				",enable=" + enable +
				'}';
	}
}
