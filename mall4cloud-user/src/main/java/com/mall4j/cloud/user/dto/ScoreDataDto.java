package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;


/**
 * @author CDT
 * @date 2021/4/12
 */

public class ScoreDataDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 签到积分列表
     */
    @ApiModelProperty(value = "签到积分列表")
    private List<Integer> scoreList;
    /**
     * 用户积分
     */
    @ApiModelProperty(value = "用户积分")
    private Long score;

    /**
     * 当天签到可领积分
     */
    @ApiModelProperty(value = "当天签到可领积分")
    private Integer curScore;

    /**
     * 上次结算过期的积分
     */
    @ApiModelProperty(value = "上次结算过期的积分")
    private Integer expireScore;
    /**
     * 过期时间(年)
     */
    @ApiModelProperty(value = "过期时间(年)")
    private Integer expireYear;
    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称")
    private String levelName;
    /**
     * 等级类型 0 普通会员 1 付费会员
     */
    @ApiModelProperty(value = "等级类型 0 普通会员 1 付费会员")
    private Integer levelType;
    /**
     * 用户当前成长值
     */
    @ApiModelProperty(value = "用户当前成长值")
    private Integer growth;
    /**
     * 注册可获取积分
     */
    @ApiModelProperty(value = "注册可获取积分")
    private Integer registerScore;
    /**
     * 购物可获取积分
     */
    @ApiModelProperty(value = "购物可获取积分")
    private Double shopScore;

    /**
     * 是否已经签到 1是 0否
     */
    @ApiModelProperty(value = "是否已经签到 1是 0否")
    private Integer isSignIn;

    /**
     * 是否已经注册 1是 0否
     */
    @ApiModelProperty(value = "是否已经注册 1是 0否")
    private Integer isRegister;

    /**
     * 签到第几天
     */
    @ApiModelProperty(value = "签到第几天")
    private Integer signInCount;

    public Integer getCurScore() {
        return curScore;
    }

    public void setCurScore(Integer curScore) {
        this.curScore = curScore;
    }

    public List<Integer> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Integer> scoreList) {
        this.scoreList = scoreList;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Integer getExpireScore() {
        return expireScore;
    }

    public void setExpireScore(Integer expireScore) {
        this.expireScore = expireScore;
    }

    public Integer getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(Integer expireYear) {
        this.expireYear = expireYear;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Integer getLevelType() {
        return levelType;
    }

    public void setLevelType(Integer levelType) {
        this.levelType = levelType;
    }

    public Integer getGrowth() {
        return growth;
    }

    public void setGrowth(Integer growth) {
        this.growth = growth;
    }

    public Integer getRegisterScore() {
        return registerScore;
    }

    public void setRegisterScore(Integer registerScore) {
        this.registerScore = registerScore;
    }

    public Double getShopScore() {
        return shopScore;
    }

    public void setShopScore(Double shopScore) {
        this.shopScore = shopScore;
    }

    public Integer getIsSignIn() {
        return isSignIn;
    }

    public void setIsSignIn(Integer isSignIn) {
        this.isSignIn = isSignIn;
    }

    public Integer getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(Integer isRegister) {
        this.isRegister = isRegister;
    }

    public Integer getSignInCount() {
        return signInCount;
    }

    public void setSignInCount(Integer signInCount) {
        this.signInCount = signInCount;
    }

    @Override
    public String toString() {
        return "ScoreDataDto{" +
                "scoreList=" + scoreList +
                ", score=" + score +
                ", curScore=" + curScore +
                ", expireScore=" + expireScore +
                ", expireYear=" + expireYear +
                ", levelName='" + levelName + '\'' +
                ", levelType=" + levelType +
                ", growth=" + growth +
                ", registerScore=" + registerScore +
                ", shopScore=" + shopScore +
                ", isSignIn=" + isSignIn +
                ", isRegister=" + isRegister +
                ", signInCount=" + signInCount +
                '}';
    }
}
