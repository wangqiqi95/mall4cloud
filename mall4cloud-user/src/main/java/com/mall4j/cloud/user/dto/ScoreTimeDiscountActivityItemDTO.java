package com.mall4j.cloud.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 积分限时折扣兑换券DTO
 *
 * @author gmq
 * @date 2022-07-11 15:12:37
 */
@Data
public class ScoreTimeDiscountActivityItemDTO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("积分活动ID")
    private Long convertId;

    @ApiModelProperty("折扣百分比")
    private Integer discount;

}
