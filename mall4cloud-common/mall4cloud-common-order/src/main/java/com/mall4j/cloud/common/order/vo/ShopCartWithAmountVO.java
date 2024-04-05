package com.mall4j.cloud.common.order.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2020/12/14
 */
public class ShopCartWithAmountVO {

    @ApiModelProperty("总额")
    private Long totalMoney;

    @ApiModelProperty("总计")
    private Long finalMoney;

    @ApiModelProperty("减额")
    private Long subtractMoney;

    @ApiModelProperty("商品数量")
    private Integer count;

    @ApiModelProperty(value = "运费",required=true)
    private Long freightAmount;

    @ApiModelProperty(value = "等级免运费金额", required = true)
    private Long freeTransfee;

    @ApiModelProperty("多个店铺的购物车信息")
    private List<ShopCartVO> shopCarts;

    @ApiModelProperty("运费信息")
    private UserDeliveryInfoVO userDeliveryInfo;

    public UserDeliveryInfoVO getUserDeliveryInfo() {
        return userDeliveryInfo;
    }

    public void setUserDeliveryInfo(UserDeliveryInfoVO userDeliveryInfo) {
        this.userDeliveryInfo = userDeliveryInfo;
    }

    public Long getFreeTransfee() {
        return freeTransfee;
    }

    public void setFreeTransfee(Long freeTransfee) {
        this.freeTransfee = freeTransfee;
    }

    public Long getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(Long freightAmount) {
        this.freightAmount = freightAmount;
    }

    public Long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Long getFinalMoney() {
        return finalMoney;
    }

    public void setFinalMoney(Long finalMoney) {
        this.finalMoney = finalMoney;
    }

    public Long getSubtractMoney() {
        return subtractMoney;
    }

    public void setSubtractMoney(Long subtractMoney) {
        this.subtractMoney = subtractMoney;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ShopCartVO> getShopCarts() {
        return shopCarts;
    }

    public void setShopCarts(List<ShopCartVO> shopCarts) {
        this.shopCarts = shopCarts;
    }

    @Override
    public String toString() {
        return "ShopCartWithAmountVO{" +
                "totalMoney=" + totalMoney +
                ", finalMoney=" + finalMoney +
                ", subtractMoney=" + subtractMoney +
                ", count=" + count +
                ", freightAmount=" + freightAmount +
                ", freeTransfee=" + freeTransfee +
                ", shopCarts=" + shopCarts +
                ", userDeliveryInfo=" + userDeliveryInfo +
                '}';
    }
}
