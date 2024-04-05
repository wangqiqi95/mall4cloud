package com.mall4j.cloud.api.payment.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class GetPayInfoByOrderIdsAndPayTypeVO {

    @ApiModelProperty("支付单号")
    private Long payId;

    @ApiModelProperty("外部订单流水号")
    private String bizPayNo;

    @ApiModelProperty("关联订单号")
    private String orderIds;

    @ApiModelProperty("关联订单号编码")
    private String orderNumber;

    /**
     * 支付方式 [0积分支付	1微信小程序支付	2支付宝支付	3微信扫码支付
     * 4微信H5支付	5微信公众号支付	6支付宝H5支付	7支付宝APP支付	8微信APP支付    9余额支付    10轻POS收钱吧支付]
     */
    @ApiModelProperty("支付方式")
    private Integer payType;

    @ApiModelProperty("支付状态[-1退款 0未支付 1已支付]")
    private Integer payStatus;

    @ApiModelProperty("支付积分")
    private Long payScore;

    @ApiModelProperty("支付金额")
    private Long payAmount;

    @ApiModelProperty("回调时间")
    private Date callbackTime;

}
