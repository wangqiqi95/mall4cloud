package com.mall4j.cloud.biz.dto.channels.sharer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description 重新申请绑定分享员
 * @Author axin
 * @Date 2023-02-23 14:43
 **/
@Data
public class SharerReBindReqDto {
    @ApiModelProperty(value = "id",required = true)
    @NotNull(message = "id不能为空")
    private Long id;
}
