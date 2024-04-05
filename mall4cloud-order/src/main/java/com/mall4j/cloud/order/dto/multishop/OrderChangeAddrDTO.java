package com.mall4j.cloud.order.dto.multishop;

import io.swagger.annotations.ApiModelProperty;

/**
 * 订单修改地址获取金额传参
 * @author Citrus
 * @date 2021/11/26 13:08
 */
public class OrderChangeAddrDTO {

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty("区域ID")
    private Long areaId;

    @ApiModelProperty("经度")
    private Double lng;

    @ApiModelProperty("纬度")
    private Double lat;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "OrderChangeAddrDTO{" +
                "orderId=" + orderId +
                ", areaId=" + areaId +
                ", lng=" + lng +
                ", lat=" + lat +
                '}';
    }
}
