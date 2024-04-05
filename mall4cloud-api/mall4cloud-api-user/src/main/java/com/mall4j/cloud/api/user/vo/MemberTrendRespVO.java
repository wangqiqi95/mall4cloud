package com.mall4j.cloud.api.user.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author lhd
 */
public class MemberTrendRespVO {

    @ApiModelProperty("时间，格式例如：20200721")
    private Long currentDay;

    @ApiModelProperty("注册会员数量")
    private Integer memberNum;

    @ApiModelProperty("注册会员数在统计时间类的所有注册会员人数的占比")
    private Double memberNumRate;

    public Long getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(Long currentDay) {
        this.currentDay = currentDay;
    }

    public Integer getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(Integer memberNum) {
        this.memberNum = memberNum;
    }

    public Double getMemberNumRate() {
        return memberNumRate;
    }

    public void setMemberNumRate(Double memberNumRate) {
        this.memberNumRate = memberNumRate;
    }

    @Override
    public String toString() {
        return "MemberTrendRespVO{" +
                "currentDay=" + currentDay +
                ", memberNum=" + memberNum +
                ", memberNumRate=" + memberNumRate +
                '}';
    }
}
