package com.mall4j.cloud.biz.dto.channels.league;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description 编辑达人
 * @Author axin
 * @Date 2023-02-13 17:31
 **/
@Data
public class PromoterUpdReqDto {
    @ApiModelProperty(value = "id")
    @NotNull(message = "ID不能为空")
    private String id;

    @ApiModelProperty(value = "视频号id")
    @NotBlank(message = "视频号id不能为空")
    private String finderId;

    @ApiModelProperty(value = "视频号名称")
    @NotBlank(message = "视频号名称不能为空")
    private String finderName;

    @ApiModelProperty(value = "关联门店")
    @NotNull(message = "关联门店不能为空")
    private Long storeId;
}
