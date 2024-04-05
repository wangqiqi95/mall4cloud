package com.mall4j.cloud.api.user.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author
 */
public class MemberDealRespVO {

    @ApiModelProperty("全部成交会员")
    private MemberDealVO allMember;

    @ApiModelProperty("新成交会员")
    private MemberDealVO newMember;

    @ApiModelProperty("老成交会员")
    private MemberDealVO oldMember;

    @ApiModelProperty("图标参数")
    private List<MemberDealTreadVO> trendParam;

    public MemberDealVO getNewMember() {
        return newMember;
    }

    public void setNewMember(MemberDealVO newMember) {
        this.newMember = newMember;
    }

    public MemberDealVO getOldMember() {
        return oldMember;
    }

    public void setOldMember(MemberDealVO oldMember) {
        this.oldMember = oldMember;
    }

    public List<MemberDealTreadVO> getTrendParam() {
        return trendParam;
    }

    public void setTrendParam(List<MemberDealTreadVO> trendParam) {
        this.trendParam = trendParam;
    }

    public MemberDealVO getAllMember() {
        return allMember;
    }

    public void setAllMember(MemberDealVO allMember) {
        this.allMember = allMember;
    }

    @Override
    public String toString() {
        return "MemberDealRespVO{" +
                "allMember=" + allMember +
                ", newMember=" + newMember +
                ", oldMember=" + oldMember +
                ", trendParam=" + trendParam +
                '}';
    }
}
