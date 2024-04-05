package com.mall4j.cloud.api.user.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 用户标签VO
 * @author: cl
 * @date: 2021-04-12 15:56
 */
public class UserTagApiVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long tagId;

    @ApiModelProperty("标签名称")
    private String tagName;

    @ApiModelProperty("标签类型0手动1条件2系统3导购自定义")
    private Integer tagType;

    @ApiModelProperty("标签下的所有用户id")
    private List<Long> userIds;

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Integer getTagType() {
        return tagType;
    }

    public void setTagType(Integer tagType) {
        this.tagType = tagType;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    @Override
    public String toString() {
        return "UserTagApiVO{" +
                "tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                ", tagType=" + tagType +
                ", userIds=" + userIds +
                '}';
    }
}
