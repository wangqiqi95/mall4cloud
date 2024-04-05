package com.mall4j.cloud.api.user.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * @author lhd
 */
public class MemberReqDTO extends CustomerReqDTO {

    @ApiModelProperty("0 全部会员 1免费会员(普通会员) 2付费会员")
    private Integer memberType;

    @ApiModelProperty("符合条件的userIds")
    private List<Long> userIds;

    @ApiModelProperty("几年前")
    private Date beforeYear;

    public Date getBeforeYear() {
        return beforeYear;
    }

    public void setBeforeYear(Date beforeYear) {
        this.beforeYear = beforeYear;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public Integer getMemberType() {
        return memberType;
    }

    public void setMemberType(Integer memberType) {
        this.memberType = memberType;
    }

    @Override
    public String toString() {
        return "MemberReqDTO{" +
                "memberType=" + memberType +
                "userIds=" + userIds +
                "beforeYear=" + beforeYear +
                '}';
    }
}
