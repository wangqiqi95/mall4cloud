package com.mall4j.cloud.common.order.bo;

import io.swagger.annotations.ApiModelProperty;

/**
 * 配送方式
 * @author FrozenWatermelon
 * @date 2020/12/8
 */
public class DeliveryModeBO {

    /**
     * 用户自提
     */
    @ApiModelProperty(value = "用户自提", required = true)
    private Boolean hasUserPickUp;

    /**
     * 店铺配送
     */
    @ApiModelProperty(value = "店铺配送", required = true)
    private Boolean hasShopDelivery;

    /**
     * 同城配送
     */
    @ApiModelProperty(value = "同城配送", required = true)
    private Boolean hasCityDelivery;

    public Boolean getHasUserPickUp() {
        return hasUserPickUp;
    }

    public void setHasUserPickUp(Boolean hasUserPickUp) {
        this.hasUserPickUp = hasUserPickUp;
    }

    public Boolean getHasShopDelivery() {
        return hasShopDelivery;
    }

    public void setHasShopDelivery(Boolean hasShopDelivery) {
        this.hasShopDelivery = hasShopDelivery;
    }

    public Boolean getHasCityDelivery() {
        return hasCityDelivery;
    }

    public void setHasCityDelivery(Boolean hasCityDelivery) {
        this.hasCityDelivery = hasCityDelivery;
    }

    @Override
    public String toString() {
        return "DeliveryModeBO{" +
                "hasUserPickUp=" + hasUserPickUp +
                ", hasShopDelivery=" + hasShopDelivery +
                ", hasCityDelivery=" + hasCityDelivery +
                '}';
    }
}
