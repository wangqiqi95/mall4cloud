package com.mall4j.cloud.api.delivery.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 店铺运费信息
 * @author FrozenWatermelon
 * @date 2020/12/16
 */
public class ShopTransportVO {

    @ApiModelProperty(value = "运费模板id")
    private Long transportId;

    @ApiModelProperty(value = "运费模板名称")
    private String transName;

    public Long getTransportId() {
        return transportId;
    }

    public void setTransportId(Long transportId) {
        this.transportId = transportId;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }

    @Override
    public String toString() {
        return "ShopTransportVO{" +
                "transportId=" + transportId +
                ", transName='" + transName + '\'' +
                '}';
    }
}
