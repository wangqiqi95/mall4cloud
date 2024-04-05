package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lhd
 * @date 2020/12/30
 */
@ApiModel(value = "设置用户信息")
@Data
public class UserRegisterDTO {

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "验证码")
    private String validCode;

    @ApiModelProperty(value = "头像")
    private String img;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "校验登陆注册验证码成功的标识")
    private String checkRegisterSmsFlag;

    @ApiModelProperty(value = "当账户未绑定时，临时的uid")
    private String tempUid;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "通过哪种方式校验 1手机号 2.邮箱")
    private Integer verifyType;

    @ApiModelProperty(value = "生日")
    private String birthDate;

    /**
     * M(男) or F(女)
     */
    @ApiModelProperty(value = "M(男) or F(女)")
    private String sex;
    @ApiModelProperty(value = "宝宝性别,M男宝宝，F女宝宝")
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


    /**
     * 触点Id
     */
    @ApiModelProperty(value = "触点号")
    private String tentacleNo;

    @ApiModelProperty(value = "第三方编码")
    private String vipCode;

    private Long storeId;
    private Long serviceStoreId;

    @ApiModelProperty(value = "业务用户Id")
    private Long bizUserId;

    @ApiModelProperty(value = "业务类型:1积分清零邀请用户")
    private Integer bizType;

}
