package com.mall4j.cloud.api.product.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * 分类签约信息
 *
 * @author lth
 */
public class CategoryShopDTO {

    @ApiModelProperty("主键Id")
    private Long categoryShopId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("分类id")
    @NotNull(message = "分类id不能为空")
    private Long categoryId;

    @ApiModelProperty("自定义扣率，为空代表采用平台扣率")
    private Double rate;

    @ApiModelProperty("经营资质")
    private String qualifications;

    public Long getCategoryShopId() {
        return categoryShopId;
    }

    public void setCategoryShopId(Long categoryShopId) {
        this.categoryShopId = categoryShopId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    @Override
    public String toString() {
        return "CategoryShopDTO{" +
                "categoryShopId=" + categoryShopId +
                ", shopId=" + shopId +
                ", categoryId=" + categoryId +
                ", rate=" + rate +
                ", qualifications='" + qualifications + '\'' +
                '}';
    }
}
