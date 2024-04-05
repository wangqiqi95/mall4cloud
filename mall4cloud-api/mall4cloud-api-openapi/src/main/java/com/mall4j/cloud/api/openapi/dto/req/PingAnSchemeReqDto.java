package com.mall4j.cloud.api.openapi.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description 平安获取Scheme码
 * @Author axin
 * @Date 2023-05-10
 **/
@Data
public class PingAnSchemeReqDto {

    @NotNull(message = "类型不能为空")
    private Integer type;

    @ApiModelProperty("通过 scheme 码进入小程序时的 query，最大1024个字符，只支持数字，大小写英文以及部分特殊字符：`!#$&'()*+,/:;=?@-._~%``")
    private String query;
}
