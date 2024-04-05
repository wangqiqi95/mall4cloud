package com.mall4j.cloud.api.user.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author
 */
public class MemberContributeRespVO {

    @ApiModelProperty("普通会员")
    private MemberContributeValueVO publicMember;

    @ApiModelProperty("付费会员")
    private MemberContributeValueVO paidMember;

    @ApiModelProperty("符合条件的普通会员userIds")
    private List<Long> userIds;

    @ApiModelProperty("符合条件的付费会员userIds")
    private List<Long> paidUserIds;

    public List<Long> getPaidUserIds() {
        return paidUserIds;
    }

    public void setPaidUserIds(List<Long> paidUserIds) {
        this.paidUserIds = paidUserIds;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
    public MemberContributeValueVO getPublicMember() {
        return publicMember;
    }

    public void setPublicMember(MemberContributeValueVO publicMember) {
        this.publicMember = publicMember;
    }

    public MemberContributeValueVO getPaidMember() {
        return paidMember;
    }

    public void setPaidMember(MemberContributeValueVO paidMember) {
        this.paidMember = paidMember;
    }

    @Override
    public String toString() {
        return "MemberContributeRespVO{" +
                "publicMember=" + publicMember +
                ", paidMember=" + paidMember +
                ", paidUserIds=" + paidUserIds +
                ", userIds=" + userIds +
                '}';
    }
}
