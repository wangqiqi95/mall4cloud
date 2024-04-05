package com.mall4j.cloud.biz.dto.channels.league;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description 新增达人
 * @Author axin
 * @Date 2023-02-14 17:20
 **/
@Data
public class PromoterAddReqDto {

    @ApiModelProperty(value = "视频号id",required = true)
    @NotBlank(message = "视频号id不能为空")
    private String finderId;

    @ApiModelProperty(value = "视频号名称",required = true)
    @NotBlank(message = "视频号名称不能为空")
    private String finderName;

    @ApiModelProperty(value = "关联门店",required = true)
    @NotNull(message = "关联门店不能为空")
    private Long storeId;
}
