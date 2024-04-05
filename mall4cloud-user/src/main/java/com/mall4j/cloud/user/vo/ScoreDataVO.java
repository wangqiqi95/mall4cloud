package com.mall4j.cloud.user.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author CDT
 * @date 2021/4/15
 */
public class ScoreDataVO {
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
    private Long expireScore;
    /**
     * 过期时间(年)
     */
    @ApiModelProperty(value = "过期时间(年)")
    private Integer expireYear;
    /**
     * 积分过期开关
     */
    @ApiModelProperty(value = "积分过期开关")
    private Boolean scoreExpireSwitch;
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
    private Long registerScore;
    /**
     * 购物可获取积分
     */
    @ApiModelProperty(value = "购物可获取积分")
    private Long shoppingGetScore;

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

    /**
     * 购物使用积分开关是否开启 true:开启 false:关闭
     */
    @ApiModelProperty(value = "购物使用积分开关是否开启 true:开启 false:关闭")
    private Boolean shoppingScoreSwitch;

    public Boolean getScoreExpireSwitch() {
        return scoreExpireSwitch;
    }

    public void setScoreExpireSwitch(Boolean scoreExpireSwitch) {
        this.scoreExpireSwitch = scoreExpireSwitch;
    }

    public Boolean getShoppingScoreSwitch() {
        return shoppingScoreSwitch;
    }

    public void setShoppingScoreSwitch(Boolean shoppingScoreSwitch) {
        this.shoppingScoreSwitch = shoppingScoreSwitch;
    }

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

    public Long getExpireScore() {
        return expireScore;
    }

    public void setExpireScore(Long expireScore) {
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

    public Long getRegisterScore() {
        return registerScore;
    }

    public void setRegisterScore(Long registerScore) {
        this.registerScore = registerScore;
    }

    public Long getShoppingGetScore() {
        return shoppingGetScore;
    }

    public void setShoppingGetScore(Long shoppingGetScore) {
        this.shoppingGetScore = shoppingGetScore;
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
        return "ScoreDataVO{" +
                "scoreList=" + scoreList +
                ", score=" + score +
                ", curScore=" + curScore +
                ", expireScore=" + expireScore +
                ", expireYear=" + expireYear +
                ", scoreExpireSwitch=" + scoreExpireSwitch +
                ", levelName='" + levelName + '\'' +
                ", levelType=" + levelType +
                ", growth=" + growth +
                ", registerScore=" + registerScore +
                ", shoppingGetScore=" + shoppingGetScore +
                ", isSignIn=" + isSignIn +
                ", isRegister=" + isRegister +
                ", signInCount=" + signInCount +
                ", shoppingScoreSwitch=" + shoppingScoreSwitch +
                '}';
    }
}
