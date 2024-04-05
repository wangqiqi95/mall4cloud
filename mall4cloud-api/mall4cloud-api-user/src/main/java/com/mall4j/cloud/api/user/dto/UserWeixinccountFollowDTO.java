package com.mall4j.cloud.api.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @luzhengxiang
 * @create 2022-04-01 4:07 PM
 **/
@Data
public class  UserWeixinccountFollowDTO {

    @NotNull(message = "公众号原始id not null")
    @ApiModelProperty(value = "公众号原始id",required = true)
    private String appId;

    @NotNull(message = "公众号类型 not null")
    @ApiModelProperty(value ="公众号类型 0成人公众号 1儿童公众号 2lifestyle公众号 3sport公众号",required = false)
    private Integer type;

    @NotNull(message = "状态 not null")
    @ApiModelProperty(value ="状态 1:关注 2:取消关注",required = true)
    private Integer status;

    @NotNull(message = "openId not null")
    @ApiModelProperty(value ="openId",required = true)
    private String openId;

    @ApiModelProperty(value ="unionId",required = true)
    private String unionId;

    private String subscribeScene;

    private String ticket;

    private Date createTime;

    private Date updateTime;

    private Date unFollowTime;
}
