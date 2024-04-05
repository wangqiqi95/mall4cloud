package com.mall4j.cloud.group.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PopUpAdFormVideoVO extends PopUpAdAttachmentExtractionVO{

    @ApiModelProperty(value = "视频链接")
    private String videoUrl;
}
