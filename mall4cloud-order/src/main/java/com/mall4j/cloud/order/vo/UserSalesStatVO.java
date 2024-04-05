package com.mall4j.cloud.order.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author ZengFanChang
 * @Date 2022/02/11
 */
@Data
public class UserSalesStatVO {


    @ApiModelProperty(value = "交易总额")
    private Long totalSales;

    @ApiModelProperty("订单数")
    private Integer orderNum;

    @ApiModelProperty("退单成功数量")
    private Integer refundNum;

    @ApiModelProperty("客单价")
    private Long customerOrderPrice;

    @ApiModelProperty("客单量")
    private Integer customerOrderNum;

    @ApiModelProperty("转化率")
    private String convertRate;

    @ApiModelProperty("有消费会员")
    private Integer buyUserNum;

    @ApiModelProperty("无消费会员")
    private Integer notBuyUserNum;

    @ApiModelProperty("有消占比")
    private String buyRate;

}
