package com.mall4j.cloud.api.biz.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TextHerf {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("链接")
    private String href;

}
