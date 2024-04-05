package com.mall4j.cloud.api.multishop.bo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @Author lth
 * @Date 2021/8/11 10:05
 */
public class ShopSimpleBO {

    @ApiModelProperty("shopId")
    private Long shopId;

    @ApiModelProperty("店铺id列表")
    private List<Long> shopIds;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("店铺状态")
    private Integer shopStatus;

    @ApiModelProperty("店铺类型")
    private Integer type;

    @ApiModelProperty("排序: 1.shopId 正序 2.shopId 倒序")
    private Integer seq;

    public List<Long> getShopIds() {
        return shopIds;
    }

    public void setShopIds(List<Long> shopIds) {
        this.shopIds = shopIds;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(Integer shopStatus) {
        this.shopStatus = shopStatus;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "ShopSimpleBO{" +
                "shopId=" + shopId +
                ", shopIds=" + shopIds +
                ", shopName='" + shopName + '\'' +
                ", shopStatus=" + shopStatus +
                ", type=" + type +
                ", seq=" + seq +
                '}';
    }
}
