package com.mall4j.cloud.biz.dto.cp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EnableMatDTO {
    @ApiModelProperty("素材id")
    @NotNull(message = "id 不能为空")
    private Long id;

    @ApiModelProperty("状态 0 禁用 1 启用")
    @NotNull(message = "状态 不能为空")
    private Integer status;
}
