package com.mall4j.cloud.api.biz.dto.cp.media;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UploadUrlMediaDTO {

    @ApiModelProperty(value = "video(视频)，image(图片)，link(图文链接)")
    private String mediaType;
    @ApiModelProperty(value = "资源url")
    private String mediaUrl;
    @ApiModelProperty(value = "页面链接")
    private String page;
    @ApiModelProperty(value = "水印")
    private String remark;
    @ApiModelProperty(value = "false:上传后获取对应的mediaId;ture:上传后获取对应的url")
    private Boolean urlFlag;

}
