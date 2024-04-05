package com.mall4j.cloud.distribution.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mall4j.cloud.common.serializer.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 精简版分销员数据
 * @author cl
 * @date 2021-08-16 15:42:26
 */
public class DistributionUserSimpleInfoVO {

    @ApiModelProperty(value = "分销员id")
    private Long distributionUserId;

    @ApiModelProperty(value = "分销员昵称")
    private String nickName;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "头像")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty(value = "绑定时间")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date bindTime;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Date getBindTime() {
        return bindTime;
    }

    public void setBindTime(Date bindTime) {
        this.bindTime = bindTime;
    }

    @Override
    public String toString() {
        return "DistributionUserSimpleInfoVO{" +
                "distributionUserId=" + distributionUserId +
                ", nickName='" + nickName + '\'' +
                ", realName='" + realName + '\'' +
                ", pic='" + pic + '\'' +
                ", bindTime=" + bindTime +
                '}';
    }
}
