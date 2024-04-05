package com.mall4j.cloud.product.dto.shopcart;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author lhd
 * @date 2021-07-01 10:47:32
 */
public class AddShopCartItemsDTO {

    @ApiModelProperty(value = "需要添加的购物车信息集合", required = true)
    private List<ChangeShopCartItemDTO> shopCartItemList;

    public List<ChangeShopCartItemDTO> getShopCartItemList() {
        return shopCartItemList;
    }

    public void setShopCartItemList(List<ChangeShopCartItemDTO> shopCartItemList) {
        this.shopCartItemList = shopCartItemList;
    }

    @Override
    public String toString() {
        return "BatchAddShopCartItemDTO{" +
                "shopCartItemList=" + shopCartItemList +
                '}';
    }
}
