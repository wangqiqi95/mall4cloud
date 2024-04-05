package com.mall4j.cloud.distribution.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * 主推商品活动-详情视图
 *
 * @author EricJeppesen
 * @date 2022/10/19 13:40
 */
public class DistributionRecommendActivityDetailVO {
    /**
     * 标识
     */
    @ApiModelProperty("标识")
    private Long id;

    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    private String activityName;

    /**
     * 活动有效期（起始时间）
     */
    @ApiModelProperty("活动有效期（起始时间）")
    private Date activityBeginTime;

    /**
     * 活动有效期（结束时间）
     */
    @ApiModelProperty("活动有效期（结束时间）")
    private Date activityEndTime;

    /**
     * 是否适用所有的门店-1是,0否
     */
    @ApiModelProperty("是否适用所有的门店-1是,0否")
    private Integer applicableForAllStores;

    /**
     * 是否适用所有的产品-1是,0否
     */
    @ApiModelProperty("是否适用所有的产品-1是,0否")
    private Integer applicableForAllGoods;

    /**
     * 作用域门店
     */
    @ApiModelProperty("作用域门店")
    private List<Long> shopIds;

    /**
     * 作用域商品SPU
     */
    @ApiModelProperty("作用月商品SPU")
    private List<Long> spuIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Date getActivityBeginTime() {
        return activityBeginTime;
    }

    public void setActivityBeginTime(Date activityBeginTime) {
        this.activityBeginTime = activityBeginTime;
    }

    public Date getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(Date activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public Integer getApplicableForAllStores() {
        return applicableForAllStores;
    }

    public void setApplicableForAllStores(Integer applicableForAllStores) {
        this.applicableForAllStores = applicableForAllStores;
    }

    public Integer getApplicableForAllGoods() {
        return applicableForAllGoods;
    }

    public void setApplicableForAllGoods(Integer applicableForAllGoods) {
        this.applicableForAllGoods = applicableForAllGoods;
    }

    public List<Long> getShopIds() {
        return shopIds;
    }

    public void setShopIds(List<Long> shopIds) {
        this.shopIds = shopIds;
    }

    public List<Long> getSpuIds() {
        return spuIds;
    }

    public void setSpuIds(List<Long> spuIds) {
        this.spuIds = spuIds;
    }

    @Override
    public String toString() {
        return "DistributionRecommendActivityDetailVO{" +
                "id=" + id +
                ", activityName='" + activityName + '\'' +
                ", activityBeginTime=" + activityBeginTime +
                ", activityEndTime=" + activityEndTime +
                ", applicableForAllStores=" + applicableForAllStores +
                ", applicableForAllGoods=" + applicableForAllGoods +
                ", shopIds=" + shopIds +
                ", spuIds=" + spuIds +
                '}';
    }
}
