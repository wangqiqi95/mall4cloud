package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddPopUpAdAttachmentDTO {

//    @ApiModelProperty(value = "开屏广告主键")
//    private Long popUpAdId;

    @ApiModelProperty(value = "素材链接")
    private String mediaUrl;

    @ApiModelProperty(value = "页面链接")
    private String link;

    @ApiModelProperty(value = "业务ID，与广告设置的业务类型关联")
    private Long businessId;

    @ApiModelProperty(value = "页面链接类型，H5，MINI_PROGRAM")
    private String linkType;

}
