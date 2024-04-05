package com.mall4j.cloud.distribution.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author cl
 * @date 2021-08-16 15:35:23
 */
public class DistributionUserInfoVO {

    @ApiModelProperty(value = "分销员id")
    private Long distributionUserId;

    @ApiModelProperty(value = "分销员昵称")
    private String  nickName;

    @ApiModelProperty(value = "分销员手机号")
    private String userMobile;

    @ApiModelProperty(value = "头像")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty(value = "分销员状态 (-1永久封禁 0 待审核 1 正常 2 暂时封禁 3审核失败)")
    private Integer state;

    @ApiModelProperty(value = "推广状态（0下线 1上线）")
    private Integer recruitState;

    public Long getDistributionUserId() {
        return distributionUserId;
    }

    public void setDistributionUserId(Long distributionUserId) {
        this.distributionUserId = distributionUserId;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getRecruitState() {
        return recruitState;
    }

    public void setRecruitState(Integer recruitState) {
        this.recruitState = recruitState;
    }

    @Override
    public String toString() {
        return "DistributionUserInfoVO{" +
                "distributionUserId=" + distributionUserId +
                ", nickName='" + nickName + '\'' +
                ", userMobile='" + userMobile + '\'' +
                ", pic='" + pic + '\'' +
                ", state=" + state +
                ", recruitState=" + recruitState +
                '}';
    }
}
