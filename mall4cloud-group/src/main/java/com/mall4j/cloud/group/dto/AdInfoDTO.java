package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "app-开屏广告信息")
public class AdInfoDTO implements Serializable {
    @ApiModelProperty(value = "用户id",hidden = true)
    private Long userId;
    private Long shopId;
}
