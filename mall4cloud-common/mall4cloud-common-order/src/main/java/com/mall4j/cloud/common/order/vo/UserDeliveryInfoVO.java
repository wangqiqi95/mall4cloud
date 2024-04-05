package com.mall4j.cloud.common.order.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

/**
 * @author FrozenWatermelon
 * @date 2020/12/15
 */
public class UserDeliveryInfoVO {

    @ApiModelProperty(value = "提货人信息")
    private UserConsigneeVO userConsignee;

    @ApiModelProperty(value = "用户地址信息")
    private UserAddrVO userAddr;

    @ApiModelProperty(value = "免运费金额")
    private Long totalFreeTransfee;

    @ApiModelProperty(value = "总运费", required = true)
    private Long totalTransfee;

    @ApiModelProperty(value = "同城配送可用状态 : 1 可用 -1 不在范围内 -2 商家没有配置同城配送信息 -3 起送费不够", required = true)
    private Integer shopCityStatus;

    private Map<Long, ShopTransFeeVO> shopIdWithShopTransFee;

    public UserConsigneeVO getUserConsignee() {
        return userConsignee;
    }

    public void setUserConsignee(UserConsigneeVO userConsignee) {
        this.userConsignee = userConsignee;
    }

    public UserAddrVO getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(UserAddrVO userAddr) {
        this.userAddr = userAddr;
    }

    public Long getTotalTransfee() {
        return totalTransfee;
    }

    public void setTotalTransfee(Long totalTransfee) {
        this.totalTransfee = totalTransfee;
    }

    public Integer getShopCityStatus() {
        return shopCityStatus;
    }

    public void setShopCityStatus(Integer shopCityStatus) {
        this.shopCityStatus = shopCityStatus;
    }

    public Long getTotalFreeTransfee() {
        return totalFreeTransfee;
    }

    public void setTotalFreeTransfee(Long totalFreeTransfee) {
        this.totalFreeTransfee = totalFreeTransfee;
    }

    public Map<Long, ShopTransFeeVO> getShopIdWithShopTransFee() {
        return shopIdWithShopTransFee;
    }

    public void setShopIdWithShopTransFee(Map<Long, ShopTransFeeVO> shopIdWithShopTransFee) {
        this.shopIdWithShopTransFee = shopIdWithShopTransFee;
    }

    @Override
    public String toString() {
        return "UserDeliveryInfoVO{" +
                "userConsignee=" + userConsignee +
                ", userAddr=" + userAddr +
                ", totalFreeTransfee=" + totalFreeTransfee +
                ", totalTransfee=" + totalTransfee +
                ", shopCityStatus=" + shopCityStatus +
                ", shopIdWithShopTransFee=" + shopIdWithShopTransFee +
                '}';
    }
}
