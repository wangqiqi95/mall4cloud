package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description 用户好友助力
 * @Author axin
 * @Date 2022-10-17 14:45
 **/
@Data
public class UserFriendAssistanceDto {

    @ApiModelProperty("被助力的用户Id")
    @NotNull(message = "被助力的用户Id不能为空")
    private Long inviterUserId;

}
