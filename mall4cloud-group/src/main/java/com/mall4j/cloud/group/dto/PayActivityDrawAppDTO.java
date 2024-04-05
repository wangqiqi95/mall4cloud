package com.mall4j.cloud.group.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("用户领取支付有礼奖励实体")
public class PayActivityDrawAppDTO implements Serializable {
    @ApiModelProperty("活动id")
    private Integer id;
    @ApiModelProperty(value = "用户id",hidden = true)
    private Long userId;
    @ApiModelProperty("订单id")
    private Long orderId;
    @ApiModelProperty("领取类型1 优惠券 2积分")
    private Integer drawType;
}
