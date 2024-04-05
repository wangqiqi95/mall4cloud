package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author cl
 * @date 2021-08-16 10:04:14
 */
public class DistributionUserRegisterDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "手机号码",required=true)
    @NotNull(message = "手机号不能为空")
    private String userMobile;

    @ApiModelProperty(value = "真实姓名")
    @Size(max = 50, message = "真实姓名长度应该小于{max}")
    private String realName;

    @ApiModelProperty(value = "身份证号")
    private String  identityCardNumber;

    @ApiModelProperty(value = "身份证正面")
    private String identityCardPicFront;

    @ApiModelProperty(value = "身份证背面")
    private String identityCardPicBack;

    @ApiModelProperty(value = "手持身份证照片")
    private String identityCardPicHold;

    @ApiModelProperty(value = "分享者")
    private String sharer;

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdentityCardNumber() {
        return identityCardNumber;
    }

    public void setIdentityCardNumber(String identityCardNumber) {
        this.identityCardNumber = identityCardNumber;
    }

    public String getIdentityCardPicFront() {
        return identityCardPicFront;
    }

    public void setIdentityCardPicFront(String identityCardPicFront) {
        this.identityCardPicFront = identityCardPicFront;
    }

    public String getIdentityCardPicBack() {
        return identityCardPicBack;
    }

    public void setIdentityCardPicBack(String identityCardPicBack) {
        this.identityCardPicBack = identityCardPicBack;
    }

    public String getIdentityCardPicHold() {
        return identityCardPicHold;
    }

    public void setIdentityCardPicHold(String identityCardPicHold) {
        this.identityCardPicHold = identityCardPicHold;
    }

    public String getSharer() {
        return sharer;
    }

    public void setSharer(String sharer) {
        this.sharer = sharer;
    }

    @Override
    public String toString() {
        return "DistributionUserRegisterDTO{" +
                "userMobile='" + userMobile + '\'' +
                ", realName='" + realName + '\'' +
                ", identityCardNumber='" + identityCardNumber + '\'' +
                ", identityCardPicFront='" + identityCardPicFront + '\'' +
                ", identityCardPicBack='" + identityCardPicBack + '\'' +
                ", identityCardPicHold='" + identityCardPicHold + '\'' +
                ", sharer='" + sharer + '\'' +
                '}';
    }
}
