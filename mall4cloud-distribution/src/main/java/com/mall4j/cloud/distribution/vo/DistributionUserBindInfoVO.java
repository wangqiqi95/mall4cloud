package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author cl
 * @date 2021-08-14 14:38:23
 */
public class DistributionUserBindInfoVO {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "-1失效 0 预绑定 1生效")
    private Integer state;

    @ApiModelProperty(value = "1 管理员更改 2.封禁")
    private Integer invalidReason;

    @ApiModelProperty(value = "变动时间")
    private Date updateTime;

    @ApiModelProperty(value = "绑定时间")
    private Date bindTime;

    @ApiModelProperty(value = "失效时间")
    private Date invalidTime;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户手机号")
    private String userMobile;

    @ApiModelProperty(value = "用户头像")
    private String pic;

    public Integer getState() {
        return state;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getInvalidReason() {
        return invalidReason;
    }

    public void setInvalidReason(Integer invalidReason) {
        this.invalidReason = invalidReason;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }

    public Date getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Date invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "DistributionUserBindInfoVO{" +
                "userId=" + userId +
                ", state=" + state +
                ", invalidReason=" + invalidReason +
                ", updateTime=" + updateTime +
                ", bindTime=" + bindTime +
                ", invalidTime=" + invalidTime +
                ", nickName='" + nickName + '\'' +
                ", userMobile='" + userMobile + '\'' +
                ", pic='" + pic + '\'' +
                '}';
    }
}
