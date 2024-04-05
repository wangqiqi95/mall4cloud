package com.mall4j.cloud.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author lhd
 */
@ApiModel("订单详细信息")
public class OrderDetailVO {


    @ApiModelProperty(value = "订单运费",required=true)
    private Long freightAmount;

    @ApiModelProperty(value = "店铺运费减免金额",required=true)
    private Long freeFreightAmount;

    @ApiModelProperty(value = "平台运费减免金额",required=true)
    private Long platformFreeFreightAmount;

    @ApiModelProperty(value = "订单项详细信息")
    private List<OrderItemDetailVO> orderItemDetailList;

    public Long getPlatformFreeFreightAmount() {
        return platformFreeFreightAmount;
    }

    public void setPlatformFreeFreightAmount(Long platformFreeFreightAmount) {
        this.platformFreeFreightAmount = platformFreeFreightAmount;
    }

    public Long getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(Long freightAmount) {
        this.freightAmount = freightAmount;
    }

    public List<OrderItemDetailVO> getOrderItemDetailList() {
        return orderItemDetailList;
    }

    public void setOrderItemDetailList(List<OrderItemDetailVO> orderItemDetailList) {
        this.orderItemDetailList = orderItemDetailList;
    }

    public Long getFreeFreightAmount() {
        return freeFreightAmount;
    }

    public void setFreeFreightAmount(Long freeFreightAmount) {
        this.freeFreightAmount = freeFreightAmount;
    }

    @Override
    public String toString() {
        return "OrderDetailVO{" +
                "freightAmount=" + freightAmount +
                ", freeFreightAmount=" + freeFreightAmount +
                ", platformFreeFreightAmount=" + platformFreeFreightAmount +
                ", orderItemDetailList=" + orderItemDetailList +
                '}';
    }
}
