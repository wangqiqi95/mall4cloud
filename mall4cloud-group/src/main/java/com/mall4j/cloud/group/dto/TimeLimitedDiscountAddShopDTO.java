package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 添加活动关联店铺
 *
 * @luzhengxiang
 * @create 2022-03-28 10:23 PM
 **/
@Data
public class TimeLimitedDiscountAddShopDTO {

    @ApiModelProperty("")
    private Integer activityId;

    @ApiModelProperty("")
    private List<Long> shopId;
}
