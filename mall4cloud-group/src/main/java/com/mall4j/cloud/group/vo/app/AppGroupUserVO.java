package com.mall4j.cloud.group.vo.app;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 拼团活动用户信息
 *
 * @author YXF
 * @date 2021/3/30
 */
public class AppGroupUserVO {

    @ApiModelProperty("用户ID（当ID为0时标识为机器人）")
    private Long userId;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("用户头像")
    private String pic;

    @ApiModelProperty("用户类型(0:成员  1:团长)")
    private Integer identityType;

    @ApiModelProperty("创建时间")
    private Date createTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Integer getIdentityType() {
        return identityType;
    }

    public void setIdentityType(Integer identityType) {
        this.identityType = identityType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "AppGroupUserVO{" +
                "userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", pic='" + pic + '\'' +
                ", identityType=" + identityType +
                ", createTime=" + createTime +
                '}';
    }
}
