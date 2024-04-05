package com.mall4j.cloud.common.order.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author FrozenWatermelon
 * @date 2021/5/10
 */
public class ShopInfoInOrderVO {

    @ApiModelProperty("店铺类型1自营店 2普通店")
    private Integer type;

    @ApiModelProperty("店铺名称")
    private String shopName;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    @Override
    public String toString() {
        return "ShopInfoInOrderVO{" +
                "type=" + type +
                ", shopName='" + shopName + '\'' +
                '}';
    }
}
