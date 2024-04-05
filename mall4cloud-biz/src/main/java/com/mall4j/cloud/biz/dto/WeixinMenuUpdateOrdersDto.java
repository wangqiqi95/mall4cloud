package com.mall4j.cloud.biz.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 微信菜单表VO
 *
 * @author FrozenWatermelon
 * @date 2022-01-26 23:14:17
 */
@Data
public class WeixinMenuUpdateOrdersDto {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜单位置变更")
    private List<WeixinMenuUpdateOrderDto> menuOrders;

}
