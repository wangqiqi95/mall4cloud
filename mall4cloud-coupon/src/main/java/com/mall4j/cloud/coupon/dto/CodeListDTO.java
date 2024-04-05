package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 券码列表搜索条件 *
 * @author shijing
 * @date 2022-01-05
 */
@Data
@ApiModel(description = "券码列表搜索条件")
public class CodeListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页码",required = true)
    private Integer pageNo = 1;
    @ApiModelProperty(value = "每页长度",required = true)
    private Integer pageSize = 10;

    @ApiModelProperty(value = "优惠券id",required = true)
    private Long couponId;

    @ApiModelProperty(value = "券码")
    private String couponCode;

    @ApiModelProperty(value = "券码状态（0：有效/1：失效）")
    private Short status;
}
