package com.mall4j.cloud.biz.dto.channels.sharer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description
 * @Author axin
 * @Date 2023-02-23 13:50
 **/
@Data
public class SharerBindReqDto {

    @ApiModelProperty(value = "员工id",required = true)
    @NotNull(message = "员工id不能为空")
    private Long staffId;

    @ApiModelProperty(value = "id")
    private Long id;
}
