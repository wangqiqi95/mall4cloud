package com.mall4j.cloud.api.delivery.dto;

import com.mall4j.cloud.common.order.vo.ShopCartItemVO;
import com.mall4j.cloud.common.order.vo.UserAddrVO;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author Citrus
 * @date 2021/11/26 10:47
 */
public class ChangeOrderAddrDTO {

    @ApiModelProperty(value = "购物项")
    private List<ShopCartItemVO> shopCartItems;

    @ApiModelProperty(value = "原本订单的运费")
    private Long freightAmount;

    @ApiModelProperty(value = "修改后的用户订单地址")
    private UserAddrVO userAddrVO;

    @ApiModelProperty(value = "配送类型 1:快递 2:自提 3：无需快递 4:同城配送")
    private Integer dvyType;

    public ChangeOrderAddrDTO(){}

    public ChangeOrderAddrDTO(List<ShopCartItemVO> shopCartItems, Long freightAmount, UserAddrVO userAddrVO, Integer dvyType) {
        this.shopCartItems = shopCartItems;
        this.freightAmount = freightAmount;
        this.userAddrVO = userAddrVO;
        this.dvyType = dvyType;
    }

    public List<ShopCartItemVO> getShopCartItems() {
        return shopCartItems;
    }

    public void setShopCartItems(List<ShopCartItemVO> shopCartItems) {
        this.shopCartItems = shopCartItems;
    }

    public Long getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(Long freightAmount) {
        this.freightAmount = freightAmount;
    }

    public UserAddrVO getUserAddrVO() {
        return userAddrVO;
    }

    public void setUserAddrVO(UserAddrVO userAddrVO) {
        this.userAddrVO = userAddrVO;
    }

    public Integer getDvyType() {
        return dvyType;
    }

    public void setDvyType(Integer dvyType) {
        this.dvyType = dvyType;
    }

    @Override
    public String toString() {
        return "ChangeOrderAddrDTO{" +
                "shopCartItems=" + shopCartItems +
                ", freightAmount=" + freightAmount +
                ", userAddrVO=" + userAddrVO +
                ", dvyType=" + dvyType +
                '}';
    }
}
