package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(description = "核销优惠券优惠券")
public class WriteOffCouponDTO implements Serializable {

    @NotNull(message = "id不能为空")
    @ApiModelProperty(value = "用户优惠券id")
    private Long id;

    @ApiModelProperty(value = "核销人id")
    private Long writeOffUserId;

    @ApiModelProperty(value = "核销人名称")
    private String writeOffUserName;

    @ApiModelProperty(value = "核销人工号")
    private String writeOffUserCode;

    @ApiModelProperty(value = "核销人手机号")
    private String writeOffUserPhone;

    @ApiModelProperty(value = "核销门店id")
    private Long writeOffShopId;

    @ApiModelProperty(value = "核销门店名称")
    private String writeOffShopName;
}
