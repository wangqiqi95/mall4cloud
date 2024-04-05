package com.mall4j.cloud.distribution.model;


import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 主推活动主表
 * 可进行逻辑删除（逻辑删除字段：deleted_status）
 * <p>
 * 继承BaseModel create_time update_time 字段
 *
 * @author EricJeppesen
 * @date 2022-10-18 01:40 下午
 */
public class DistributionRecommendActivity extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1246891060829572518L;

    /**
     * 标识
     */
    private Long id;
    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 活动有效期（起始时间）
     */
    private java.util.Date activityBeginTime;
    /**
     * 活动有效期（结束时间）
     */
    private java.util.Date activityEndTime;
    /**
     * 适用所有的门店（0否,1是）
     */
    private Integer applicableForAllStores;
    /**
     * 适用门店数量（缓存）
     */
    private Integer applicableStoresAmount;
    /**
     * 适用所有的门店（0否,1是）
     */
    private Integer applicableForAllGoods;
    /**
     * 参与活动的商品（缓存）
     */
    private Integer activityGoodsAmount;
    /**
     * 数据是否删除
     */
    private Integer deletedStatus;
    /**
     * 活动状态
     */
    private Integer activityStatus;
    /**
     * 数据排序（通常情况ASC）
     */
    private Integer dataSort;
    /**
     * 创建人（标识）
     */
    private Long createUserId;
    /**
     * 创建人（姓名）
     */
    private String createUserName;
    /**
     * 最后一次修改人（标识）
     */
    private Long updateUserId;
    /**
     * 最后一次修改人（姓名）
     */
    private String updateUserName;

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

    public Integer getApplicableStoresAmount() {
        return applicableStoresAmount;
    }

    public void setApplicableStoresAmount(Integer applicableStoresAmount) {
        this.applicableStoresAmount = applicableStoresAmount;
    }

    public Integer getApplicableForAllGoods() {
        return applicableForAllGoods;
    }

    public void setApplicableForAllGoods(Integer applicableForAllGoods) {
        this.applicableForAllGoods = applicableForAllGoods;
    }

    public Integer getActivityGoodsAmount() {
        return activityGoodsAmount;
    }

    public void setActivityGoodsAmount(Integer activityGoodsAmount) {
        this.activityGoodsAmount = activityGoodsAmount;
    }

    public Integer getDeletedStatus() {
        return deletedStatus;
    }

    public void setDeletedStatus(Integer deletedStatus) {
        this.deletedStatus = deletedStatus;
    }

    public Integer getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Integer activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Integer getDataSort() {
        return dataSort;
    }

    public void setDataSort(Integer dataSort) {
        this.dataSort = dataSort;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    @Override
    public String toString() {
        return "DistributionRecommendActivity{" +
                "createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", id=" + id +
                ", activityName='" + activityName + '\'' +
                ", activityBeginTime=" + activityBeginTime +
                ", activityEndTime=" + activityEndTime +
                ", applicableForAllStores=" + applicableForAllStores +
                ", applicableStoresAmount=" + applicableStoresAmount +
                ", activityGoodsAmount=" + activityGoodsAmount +
                ", deletedStatus=" + deletedStatus +
                ", activityStatus=" + activityStatus +
                ", dataSort=" + dataSort +
                ", createUserId=" + createUserId +
                ", createUserName='" + createUserName + '\'' +
                ", updateUserId=" + updateUserId +
                ", updateUserName='" + updateUserName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DistributionRecommendActivity that = (DistributionRecommendActivity) o;
        return Objects.equals(id, that.id) && Objects.equals(activityName, that.activityName) && Objects.equals(activityBeginTime, that.activityBeginTime) && Objects.equals(activityEndTime, that.activityEndTime) && Objects.equals(applicableForAllStores, that.applicableForAllStores) && Objects.equals(applicableStoresAmount, that.applicableStoresAmount) && Objects.equals(activityGoodsAmount, that.activityGoodsAmount) && Objects.equals(deletedStatus, that.deletedStatus) && Objects.equals(activityStatus, that.activityStatus) && Objects.equals(dataSort, that.dataSort) && Objects.equals(createUserId, that.createUserId) && Objects.equals(createUserName, that.createUserName) && Objects.equals(updateUserId, that.updateUserId) && Objects.equals(updateUserName, that.updateUserName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, activityName, activityBeginTime, activityEndTime, applicableForAllStores, applicableStoresAmount, activityGoodsAmount, deletedStatus, activityStatus, dataSort, createUserId, createUserName, updateUserId, updateUserName);
    }
}
