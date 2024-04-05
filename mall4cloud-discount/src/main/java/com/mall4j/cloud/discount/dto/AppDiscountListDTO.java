package com.mall4j.cloud.discount.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * app查询满减满折活动列表参数对象
 *
 * @luzhengxiang
 * @create 2022-03-14 11:26 AM
 **/
@Data
public class AppDiscountListDTO {
    @ApiModelProperty("店铺id")
    private Long shopId;
}
