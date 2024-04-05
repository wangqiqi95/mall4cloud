package com.mall4j.cloud.group.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class PopUpAdAttachmentExtractionVO {

    @ApiModelProperty(value = "广告ID")
    private Long PopUpAdID;

    @ApiModelProperty(value = "推送内容类型")
    private String attachmentType;

    @ApiModelProperty(value = "自动关闭时间")
    private Integer autoOffSeconds;

}
