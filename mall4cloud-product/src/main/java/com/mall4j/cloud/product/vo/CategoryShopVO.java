package com.mall4j.cloud.product.vo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author lth
 * @Date 2021/4/26 9:28
 */
public class CategoryShopVO {

    @ApiModelProperty("主键id")
    private Long categoryShopId;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("上级分类id")
    private Long parentId;

    @ApiModelProperty("上级分类名称")
    private String parentName;

    @ApiModelProperty("平台扣率")
    private Double platformRate;

    @ApiModelProperty("自定义扣率，为空代表采用平台扣率")
    private Double customizeRate;

    @ApiModelProperty("经营资质")
    private String qualifications;

    @ApiModelProperty("分类状态 1:上架 0:下架 -1:已删除")
    private Integer categoryStatus;

    public Integer getCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(Integer categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public Long getCategoryShopId() {
        return categoryShopId;
    }

    public void setCategoryShopId(Long categoryShopId) {
        this.categoryShopId = categoryShopId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Double getPlatformRate() {
        return platformRate;
    }

    public void setPlatformRate(Double platformRate) {
        this.platformRate = platformRate;
    }

    public Double getCustomizeRate() {
        return customizeRate;
    }

    public void setCustomizeRate(Double customizeRate) {
        this.customizeRate = customizeRate;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    @Override
    public String toString() {
        return "CategoryShopVO{" +
                "categoryShopId=" + categoryShopId +
                ", categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", parentName='" + parentName + '\'' +
                ", platformRate=" + platformRate +
                ", customizeRate=" + customizeRate +
                ", qualifications='" + qualifications + '\'' +
                ", categoryStatus=" + categoryStatus +
                '}';
    }
}
