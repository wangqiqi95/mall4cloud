package com.mall4j.cloud.group.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddPopUpAdAttachmentBO {

    @ApiModelProperty(value = "开屏广告主键")
    private Long popUpAdId;

    @ApiModelProperty(value = "素材链接")
    private String mediaUrl;

    @ApiModelProperty(value = "页面链接")
    private String link;

    @ApiModelProperty(value = "页面类型")
    private String linkType;

    @ApiModelProperty(value = "业务ID，与广告设置的业务类型关联")
    private Long businessId;

}
