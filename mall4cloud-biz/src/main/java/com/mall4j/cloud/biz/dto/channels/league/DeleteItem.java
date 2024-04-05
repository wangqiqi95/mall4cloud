package com.mall4j.cloud.biz.dto.channels.league;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Description 删除商品
 * @Author axin
 * @Date 2023-04-23 11:15
 **/
@Getter
@Setter
public class DeleteItem {
    @ApiModelProperty(value = "联盟商品id")
    @NotNull(message = "联盟商品id不能为空")
    private Long id;
}
