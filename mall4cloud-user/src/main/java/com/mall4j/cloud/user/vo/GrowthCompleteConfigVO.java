package com.mall4j.cloud.user.vo;

/**
 * 成长配置信息
 * @author lth
 */
public class GrowthCompleteConfigVO {
    /**
     * 成长值获取开关
     */
    private Boolean shopGrowthSwitch;

    private Double buyPrice;

    private Double buyOrder;

    public Boolean getShopGrowthSwitch() {
        return shopGrowthSwitch;
    }

    public void setShopGrowthSwitch(Boolean shopGrowthSwitch) {
        this.shopGrowthSwitch = shopGrowthSwitch;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public Double getBuyOrder() {
        return buyOrder;
    }

    public void setBuyOrder(Double buyOrder) {
        this.buyOrder = buyOrder;
    }

    @Override
    public String toString() {
        return "GrowthConfigVO{" +
                "shopGrowthSwitch=" + shopGrowthSwitch +
                ", buyPrice=" + buyPrice +
                ", buyOrder=" + buyOrder +
                '}';
    }
}
