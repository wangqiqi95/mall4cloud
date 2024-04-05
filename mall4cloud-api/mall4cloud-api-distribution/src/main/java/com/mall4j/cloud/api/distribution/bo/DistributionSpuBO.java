package com.mall4j.cloud.api.distribution.bo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author cl
 * @date 2021-08-18 15:00:14
 */
public class DistributionSpuBO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分销商品表")
    private Long distributionSpuId;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("商品id")
    private Long spuId;

    @ApiModelProperty("商品名称")
    private String spuName;

    @ApiModelProperty("店铺id")
    private  Long shopId;

    @ApiModelProperty("商品价格")
    private Long price;

    @ApiModelProperty("商品主图")
    private String MainImgUrl;

    @ApiModelProperty("奖励id")
    private Long awardId;

    @ApiModelProperty("状态(0:商家下架 1:商家上架 2:违规下架 3:平台审核)")
    private Integer state;

    @ApiModelProperty("奖励方式(0 按比例 1 按固定数值)")
    private Integer awardMode;

    @ApiModelProperty("上级奖励设置(0 关闭 1开启)")
    private Integer parentAwardSet;

    @ApiModelProperty("奖励数额(奖励方式为0时，表示百分比，为1时代表实际奖励金额）")
    private Long awardNumbers;

    @ApiModelProperty("上级奖励数额(奖励方式为0时，表示百分比，为1时代表实际奖励金额）")
    private Long parentAwardNumbers;

    public Long getDistributionSpuId() {
        return distributionSpuId;
    }

    public void setDistributionSpuId(Long distributionSpuId) {
        this.distributionSpuId = distributionSpuId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public String getSpuName() {
        return spuName;
    }

    public void setSpuName(String spuName) {
        this.spuName = spuName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getMainImgUrl() {
        return MainImgUrl;
    }

    public void setMainImgUrl(String mainImgUrl) {
        MainImgUrl = mainImgUrl;
    }

    public Long getAwardId() {
        return awardId;
    }

    public void setAwardId(Long awardId) {
        this.awardId = awardId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getAwardMode() {
        return awardMode;
    }

    public void setAwardMode(Integer awardMode) {
        this.awardMode = awardMode;
    }

    public Integer getParentAwardSet() {
        return parentAwardSet;
    }

    public void setParentAwardSet(Integer parentAwardSet) {
        this.parentAwardSet = parentAwardSet;
    }

    public Long getAwardNumbers() {
        return awardNumbers;
    }

    public void setAwardNumbers(Long awardNumbers) {
        this.awardNumbers = awardNumbers;
    }

    public Long getParentAwardNumbers() {
        return parentAwardNumbers;
    }

    public void setParentAwardNumbers(Long parentAwardNumbers) {
        this.parentAwardNumbers = parentAwardNumbers;
    }

    @Override
    public String toString() {
        return "DistributionSpuBO{" +
                "distributionSpuId=" + distributionSpuId +
                ", shopName='" + shopName + '\'' +
                ", spuId=" + spuId +
                ", spuName='" + spuName + '\'' +
                ", shopId=" + shopId +
                ", price=" + price +
                ", MainImgUrl='" + MainImgUrl + '\'' +
                ", awardId=" + awardId +
                ", state=" + state +
                ", awardMode=" + awardMode +
                ", parentAwardSet=" + parentAwardSet +
                ", awardNumbers=" + awardNumbers +
                ", parentAwardNumbers=" + parentAwardNumbers +
                '}';
    }
}
