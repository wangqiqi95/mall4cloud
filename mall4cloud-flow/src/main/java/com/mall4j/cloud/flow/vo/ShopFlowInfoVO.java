package com.mall4j.cloud.flow.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

/**
 * @Author lth
 * @Date 2021/5/25 14:31
 */
public class ShopFlowInfoVO {

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("浏览量")
    private Long visitCount;

    @ApiModelProperty("访客数")
    private Long visitUserCount;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Long visitCount) {
        this.visitCount = visitCount;
    }

    public Long getVisitUserCount() {
        return visitUserCount;
    }

    public void setVisitUserCount(Long visitUserCount) {
        this.visitUserCount = visitUserCount;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    @Override
    public String toString() {
        return "ShopFlowInfoVO{" +
                "shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", visitCount=" + visitCount +
                ", visitUserCount=" + visitUserCount +
                '}';
    }
}
