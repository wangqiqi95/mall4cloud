package com.mall4j.cloud.api.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CrmUserSyncDTO {

    @ApiModelProperty("手机号码")
    @NotBlank(message="手机号码不能为空")
    private String phone;
    @ApiModelProperty("unionid")
    @NotBlank(message="用户unionId不能为空")
    private String unionId;
    @ApiModelProperty("用户编码")
    @NotBlank(message="用户编码不能为空")
    private String vipcode;
    @ApiModelProperty("门店编码")
    @NotBlank(message="门店编码不能为空")
    private String storeCode;

    @ApiModelProperty(value = "顾客姓名")
    private String customerName;
    @ApiModelProperty(value = "昵称")
    private String nickName;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "生日")
    private String birthDate;
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
}
