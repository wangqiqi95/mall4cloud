package com.mall4j.cloud.api.user.dto;

import com.mall4j.cloud.api.user.constant.RegSourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class FriendlyWalkRegisterRequest {

    @ApiModelProperty("手机号码")
    private String phone;
    @ApiModelProperty("验证码")
    private String code;
    @ApiModelProperty("unionid")
    private String unionId;
    @ApiModelProperty("openId")
    private String openId;

    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "头像")
    private String img;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "生日")
    private String birthDate;
    /**
     * M(男) or F(女)
     */
    @ApiModelProperty(value = "M(男) or F(女)")
    private String sex;
    @ApiModelProperty(value = "宝宝性别,N无小孩 M男宝宝，F女宝宝")
    private String haveBaby;
    @ApiModelProperty(value = "宝宝生日,yyyy-MM-dd")
    private String babyBirthday;
    @ApiModelProperty(value = "二宝性别,M男宝宝，F女宝宝")
    private String secondBabySex;
    @ApiModelProperty(value = "二宝生日,yyyy-MM-dd")
    private String secondBabyBirth;
    @ApiModelProperty(value = "三宝性别,M男宝宝，F女宝宝")
    private String thirdBabySex;
    @ApiModelProperty(value = "三宝生日,yyyy-MM-dd")
    private String thirdBabyBirth;

    @ApiModelProperty(value = "省id")
    private Long provinceId;
    @ApiModelProperty(value = "省")
    private String province;
    @ApiModelProperty(value = "市Id")
    private Long cityId;
    @ApiModelProperty(value = "市")
    private String city;
    @ApiModelProperty(value = "区id")
    private Long areaId;
    @ApiModelProperty(value = "区")
    private String area;

    private Long storeId;

    /**
     * @see RegSourceEnum
     * 注册渠道
     */
    private Integer regSource;
}
