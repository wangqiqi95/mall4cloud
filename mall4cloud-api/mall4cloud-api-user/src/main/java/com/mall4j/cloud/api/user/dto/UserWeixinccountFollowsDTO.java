package com.mall4j.cloud.api.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @luzhengxiang
 * @create 2022-04-01 4:07 PM
 **/
@Data
public class UserWeixinccountFollowsDTO {

    @NotNull(message = "公众号原始id not null")
    @ApiModelProperty(value = "公众号原始id",required = true)
    private String appId;

    private List<UserWeixinccountFollowDTO> followDTOList;
}
