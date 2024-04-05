package com.mall4j.cloud.api.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 积分活动、抽奖游戏活动限制条件集合类
 */
@Data
public class CheckOrderDTO {

    @ApiModelProperty("会员用户ID")
    private Long userId;

    @ApiModelProperty("用户手机号")
    private String phone;

    @ApiModelProperty("订单开始时间")
    private String startTime;

    @ApiModelProperty("订单结束时间")
    private String endTime;

    @ApiModelProperty("订单消费金额限制")
    private Long orderNum;

    @ApiModelProperty("订单类型限制")
    private String orderType;

    @ApiModelProperty("指定消费门店ID")
    private List<Long> storeIdList;

    @ApiModelProperty("指定消费门店编码")
    private List<String> storeCodeList;

}
