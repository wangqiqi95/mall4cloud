package com.mall4j.cloud.common.order.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020-11-20 15:47:32
 */
public class ShopCartItemDiscountVO implements Serializable {

    @ApiModelProperty(value = "已选满减项", required = true)
    private ChooseDiscountItemVO chooseDiscountItem;

    @ApiModelProperty(value = "商品列表")
    private List<ShopCartItemVO> shopCartItems;

    public ChooseDiscountItemVO getChooseDiscountItem() {
        return chooseDiscountItem;
    }

    public void setChooseDiscountItem(ChooseDiscountItemVO chooseDiscountItem) {
        this.chooseDiscountItem = chooseDiscountItem;
    }

    public List<ShopCartItemVO> getShopCartItems() {
        return shopCartItems;
    }

    public void setShopCartItems(List<ShopCartItemVO> shopCartItems) {
        this.shopCartItems = shopCartItems;
    }

    @Override
    public String toString() {
        return "ShopCartItemDiscountVO{" +
                "chooseDiscountItem=" + chooseDiscountItem +
                ", shopCartItems=" + shopCartItems +
                '}';
    }
}
