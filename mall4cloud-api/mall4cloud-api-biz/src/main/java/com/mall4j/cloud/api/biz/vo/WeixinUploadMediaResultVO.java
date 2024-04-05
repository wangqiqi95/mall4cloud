package com.mall4j.cloud.api.biz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WeixinUploadMediaResultVO {

    @ApiModelProperty(value = "素材ID")
    private String mediaId;

    @ApiModelProperty(value = "素材URL")
    private String mediaUrl;

}
