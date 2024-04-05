package com.mall4j.cloud.biz.dto.channels.league;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @Description 删除达人
 * @Author axin
 * @Date 2023-02-14 17:42
 **/
@Data
public class PromoterDelReqDto {
    @ApiModelProperty(value = "id")
    @NotNull(message = "id不能为空")
    private Long id;
}
