package com.mall4j.cloud.api.platform.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StaffBindQiWeiDTO {

    @ApiModelProperty("导购id")
    private Long staffId;
    @ApiModelProperty("企业微信id")
    private String qiWeiUserId;
    @ApiModelProperty("企业微信状态")
    private Integer qiWeiUserStatus;

    /**
     * 如下敏感信息获取：
     * 网页授权登录：https://developer.work.weixin.qq.com/document/path/91022
     * scope为snsapi_privateinfo，且用户在应用可见范围之内时返回此参数。
     */
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("性别 0表示未定义，1表示男性，2表示女性")
    private String gender;
    @ApiModelProperty("员工个人二维码")
    private String qrCode;

}
