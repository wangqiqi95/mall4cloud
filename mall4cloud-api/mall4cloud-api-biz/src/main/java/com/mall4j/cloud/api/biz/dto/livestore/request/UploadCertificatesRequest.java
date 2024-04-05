package com.mall4j.cloud.api.biz.dto.livestore.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Data
public class UploadCertificatesRequest {

    @ApiModelProperty("视频号售后单id")
    private Long aftersale_id;

    @ApiModelProperty("协商文本内容")
    private String refund_desc;

    @ApiModelProperty("凭证图片列表")
    private List<String> certificates;


}
