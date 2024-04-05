package com.mall4j.cloud.common.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/12/18
 */
@Data
public class SubmitOrderDTO {

    @ApiModelProperty(value = "每个店铺提交的订单信息",required=true)
    private List<OrderShopDTO> orderShopParam;

    @ApiModelProperty(value = "订单发票")
    private List<OrderInvoiceDTO> orderInvoiceList;

    @ApiModelProperty(value = "自提信息Dto")
    private OrderSelfStationDTO orderSelfStationDto;

    @ApiModelProperty("代客下单用户ID")
    private Long userId;
    @ApiModelProperty(value = "门店id")
    private Long storeId;

    @ApiModelProperty(value = "触点号")
    private String tentacleNo;

    @ApiModelProperty(value = "支付方式 (0积分支付 1:微信小程序支付 2:支付宝 3微信扫码支付 4 微信h5支付 5微信公众号支付 6支付宝H5支付 7支付宝APP支付 8微信APP支付 9余额支付)", required = true)
    private Integer payType;

    public List<OrderShopDTO> getOrderShopParam() {
        return orderShopParam;
    }

    public void setOrderShopParam(List<OrderShopDTO> orderShopParam) {
        this.orderShopParam = orderShopParam;
    }

}
