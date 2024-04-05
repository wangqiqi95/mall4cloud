package com.mall4j.cloud.user.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 用户会员中心信息VO
 *
 * @author LTH
 */
public class UserMemberInfoVO {

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("头像图片路径")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty(value = "用户积分")
    private Long score;

    @ApiModelProperty("过期时间")
    private Date expireTime;

    @ApiModelProperty("用户普通会员等级信息")
    private UserLevelVO userOrdinaryLevel;

    @ApiModelProperty("用户付费会员等级信息")
    private UserLevelVO userPayedLevel;

    @ApiModelProperty(value = "用户当前成长值")
    private Integer growth;

    @ApiModelProperty(value = "购物可获取成长值")
    private Double shopScore;

    @ApiModelProperty(value = "签到第几天")
    private Integer signInCount;

    @ApiModelProperty("会员等级说明列表")
    private List<UserLevelVO> userLevelList;

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getGrowth() {
        return growth;
    }

    public void setGrowth(Integer growth) {
        this.growth = growth;
    }

    public Double getShopScore() {
        return shopScore;
    }

    public void setShopScore(Double shopScore) {
        this.shopScore = shopScore;
    }

    public Integer getSignInCount() {
        return signInCount;
    }

    public void setSignInCount(Integer signInCount) {
        this.signInCount = signInCount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public List<UserLevelVO> getUserLevelList() {
        return userLevelList;
    }

    public void setUserLevelList(List<UserLevelVO> userLevelList) {
        this.userLevelList = userLevelList;
    }

    public UserLevelVO getUserOrdinaryLevel() {
        return userOrdinaryLevel;
    }

    public void setUserOrdinaryLevel(UserLevelVO userOrdinaryLevel) {
        this.userOrdinaryLevel = userOrdinaryLevel;
    }

    public UserLevelVO getUserPayedLevel() {
        return userPayedLevel;
    }

    public void setUserPayedLevel(UserLevelVO userPayedLevel) {
        this.userPayedLevel = userPayedLevel;
    }

    @Override
    public String toString() {
        return "UserMemberInfoVO{" +
                "nickName='" + nickName + '\'' +
                ", pic='" + pic + '\'' +
                ", score=" + score +
                ", expireTime=" + expireTime +
                ", userOrdinaryLevel=" + userOrdinaryLevel +
                ", userPayedLevel=" + userPayedLevel +
                ", growth=" + growth +
                ", shopScore=" + shopScore +
                ", signInCount=" + signInCount +
                ", userLevelList=" + userLevelList +
                '}';
    }

}
