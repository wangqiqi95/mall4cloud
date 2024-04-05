package com.mall4j.cloud.api.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserWeixinAccountFollowVO {
    private Long id;

    private String appId;

    @ApiModelProperty(value = "公众号类型 0成人公众号 1儿童公众号 2lifestyle公众号 3sport公众号")
    private Integer type;

    @ApiModelProperty(value = "状态 1:关注 2:取消关注")
    private Integer status;

    private String openId;

    @ApiModelProperty(value = "unionid")
    private String unionId;

    @ApiModelProperty(value = "公众号名称")
    private String wxmpname;
}
