package com.mall4j.cloud.distribution.bo;

import com.mall4j.cloud.api.multishop.bo.ShopWalletBO;
import com.mall4j.cloud.api.order.bo.EsOrderItemBO;

import java.util.List;

/**
 * @author cl
 * @date 2021-08-19 11:30:50
 */
public class DistributionNotifyOrderAndShopBO {
    /**
     * 店铺钱包
     */
    private List<ShopWalletBO> shopWalletBOList;
    /**
     * 订单项
     */
    private List<EsOrderItemBO> distributionOrderItems;

    public List<ShopWalletBO> getShopWalletBOList() {
        return shopWalletBOList;
    }

    public void setShopWalletBOList(List<ShopWalletBO> shopWalletBOList) {
        this.shopWalletBOList = shopWalletBOList;
    }

    public List<EsOrderItemBO> getDistributionOrderItems() {
        return distributionOrderItems;
    }

    public void setDistributionOrderItems(List<EsOrderItemBO> distributionOrderItems) {
        this.distributionOrderItems = distributionOrderItems;
    }

    @Override
    public String toString() {
        return "DistributionNotifyOrderAndShopBO{" +
                "shopWalletBOList=" + shopWalletBOList +
                ", distributionOrderItems=" + distributionOrderItems +
                '}';
    }
}
