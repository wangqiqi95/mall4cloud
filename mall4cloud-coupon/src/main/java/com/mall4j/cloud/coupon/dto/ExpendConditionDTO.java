package com.mall4j.cloud.coupon.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author ty
 * @ClassName ExpendConditionDTO
 * @description
 * @date 2022/11/18 14:00
 */
@Data
public class ExpendConditionDTO {

    @ApiModelProperty("订单消费限制开关")
    private Integer orderSwitch;

    @ApiModelProperty("订单消费开始时间")
    private Date orderStartTime;

    @ApiModelProperty("订单消费结束时间")
    private Date orderEndTime;

    @ApiModelProperty("订单消费类型【0:表示累计消费;1:表示单笔消费】")
    private Integer orderExpendType;

    @ApiModelProperty("订单消费金额限制")
    private Long orderNum;

    @ApiModelProperty("订单类型限制")
    private String orderType;

    @ApiModelProperty("订单金额不足提示")
    private String orderTips;

    @ApiModelProperty("粉丝等级集合")
    private String fanLevels;

    @ApiModelProperty("粉丝等级不足提示")
    private String fanTips;

    @ApiModelProperty("指定消费门店")
    private String appointShop;

}
