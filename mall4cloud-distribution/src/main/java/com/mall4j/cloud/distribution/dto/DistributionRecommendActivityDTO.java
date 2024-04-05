package com.mall4j.cloud.distribution.dto;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 主推活动DTO
 *
 * @author EricJeppesen
 * @date 2022/10/18 13:56
 */
public class DistributionRecommendActivityDTO {

    /**
     * 主键标识
     */
    @ApiModelProperty("主键标识")
    private Long id;
    /**
     * 活动名称
     */
    @ApiModelProperty("活动名称")
    @NotBlank(message = "活动名称不能为空")
    private String activityName;
    /**
     * 活动有效期（起始时间）
     */
    @ApiModelProperty("活动有效期（起始时间）")
    @NotBlank(message = "活动有效期（起始时间）不能为空")
    private java.util.Date activityBeginTime;
    /**
     * 活动有效期（结束时间）
     */
    @ApiModelProperty("活动有效期（结束时间）")
    @NotBlank(message = "活动有效期（结束时间）不能为空")
    private java.util.Date activityEndTime;
    /**
     * 适用所有的门店
     */
    @ApiModelProperty("是否适用所有的门店-1是,0否-当选择0时，请确保shopIds数组大于零")
    @NotBlank(message = "是否适用所有的门店不能为空")
    private Integer applicableForAllStores;
    /**
     * 适用所有的产品
     */
    @ApiModelProperty("是否适用所有的产品-1是,0否-当选择0时，请确保spuIds数组大于零")
    @NotBlank(message = "是否适用所有的产品不能为空")
    private Integer applicableForAllGoods;
    /**
     * 数据排序（通常情况ASC）
     */
    @ApiModelProperty("数据排序")
    private Integer dataSort;
    /**
     * 参与活动的产品标识
     */
    @ApiModelProperty("参与活动的产品标识")
    private List<Long> spuIds;
    /**
     * 使用门店的标识
     */
    @ApiModelProperty("使用门店的标识")
    private List<Long> shopIds;

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

    public Integer getDataSort() {
        return dataSort;
    }

    public void setDataSort(Integer dataSort) {
        this.dataSort = dataSort;
    }

    public List<Long> getSpuIds() {
        return spuIds;
    }

    public void setSpuIds(List<Long> spuIds) {
        this.spuIds = spuIds;
    }

    public List<Long> getShopIds() {
        return shopIds;
    }

    public void setShopIds(List<Long> shopIds) {
        this.shopIds = shopIds;
    }

    @Override
    public String toString() {
        return "DistributionRecommendActivityDTO{" +
                "id=" + id +
                ", activityName='" + activityName + '\'' +
                ", activityBeginTime=" + activityBeginTime +
                ", activityEndTime=" + activityEndTime +
                ", applicableForAllStores=" + applicableForAllStores +
                ", applicableForAllGoods=" + applicableForAllGoods +
                ", dataSort=" + dataSort +
                ", spuIds=" + spuIds +
                ", shopIds=" + shopIds +
                '}';
    }
}
