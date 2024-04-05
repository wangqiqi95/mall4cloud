package com.mall4j.cloud.group.vo.app;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * @author YXF
 * @date 2021/3/26
 */
public class AppGroupActivityVO {

    @ApiModelProperty(value = "拼团活动id")
    private Long groupActivityId;

    @ApiModelProperty(value = "拼团状态(-1:删除、0:未启用、1:启用、2:违规下架、3:等待审核 4:已失效 5:已结束)")
    private Integer status;

    @ApiModelProperty(value = "活动开始时间")
    private Date startTime;

    @ApiModelProperty(value = "活动结束时间")
    private Date endTime;

    @ApiModelProperty(value = "成团人数")
    private Integer groupNumber;

    @ApiModelProperty(value = "商品是否限购（1:限购、0:不限购）")
    private Integer hasMaxNum;

    @ApiModelProperty(value = "限购数量")
    private Integer maxNum;

    @ApiModelProperty(value = "已购买数量")
    private Integer prodCount;

    @ApiModelProperty(value = "当前用户参团的团队ID（null表示还没有参加该活动）")
    private Long joinGroupTeamId;

    @ApiModelProperty(value = "是否开启凑团模式（1:凑团、0:不凑团）", notes = "开启模拟成团后，拼团有效期内人数未满的团，系统将会模拟“匿名买家”凑满人数，使该团成团。 你只需要对已付款参团的真实买家发货。建议合理开启，以提高成团率。")
    private Integer hasGroupTip;

    @ApiModelProperty(value = "是否开启活动预热（1:预热、0:不预热）", notes = "开启后，商品详情页展示未开始的拼团活动，但活动开始前用户无法拼团购买")
    private Integer isPreheat;

    @ApiModelProperty(value = "活动状态（活动状态：1:未开始、2:进行中、3:已结束、4:已失效）")
    private Integer activityStatus;

    @ApiModelProperty(value = "商品活动价格（最低价）")
    private Long price;

    @ApiModelProperty(value = "商品id")
    private Long spuId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "在多少秒后过期")
    private Long expiresIn;

    @ApiModelProperty(value = "在多少秒后开始")
    private Long startIn;

    @ApiModelProperty(value = "sku列表")
    private List<AppGroupSkuVO> groupSkuList;

    @ApiModelProperty(value = "指定门店类型 0-所有门店 1-部分门店")
    private Integer limitStoreType;

    public Long getGroupActivityId() {
        return groupActivityId;
    }

    public void setGroupActivityId(Long groupActivityId) {
        this.groupActivityId = groupActivityId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


    public Integer getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(Integer groupNumber) {
        this.groupNumber = groupNumber;
    }

    public Integer getHasMaxNum() {
        return hasMaxNum;
    }

    public void setHasMaxNum(Integer hasMaxNum) {
        this.hasMaxNum = hasMaxNum;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public Integer getProdCount() {
        return prodCount;
    }

    public void setProdCount(Integer prodCount) {
        this.prodCount = prodCount;
    }

    public Long getJoinGroupTeamId() {
        return joinGroupTeamId;
    }

    public void setJoinGroupTeamId(Long joinGroupTeamId) {
        this.joinGroupTeamId = joinGroupTeamId;
    }

    public Integer getHasGroupTip() {
        return hasGroupTip;
    }

    public void setHasGroupTip(Integer hasGroupTip) {
        this.hasGroupTip = hasGroupTip;
    }

    public Integer getIsPreheat() {
        return isPreheat;
    }

    public void setIsPreheat(Integer isPreheat) {
        this.isPreheat = isPreheat;
    }

    public void setActivityStatus(Integer activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Integer getActivityStatus() {
        return activityStatus;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public List<AppGroupSkuVO> getGroupSkuList() {
        return groupSkuList;
    }

    public void setGroupSkuList(List<AppGroupSkuVO> groupSkuList) {
        this.groupSkuList = groupSkuList;
    }


    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Long getStartIn() {
        return startIn;
    }

    public void setStartIn(Long startIn) {
        this.startIn = startIn;
    }

    public Integer getLimitStoreType() {
        return limitStoreType;
    }

    public void setLimitStoreType(Integer limitStoreType) {
        this.limitStoreType = limitStoreType;
    }

    @Override
    public String toString() {
        return "AppGroupActivityVO{" +
                "groupActivityId=" + groupActivityId +
                ", status=" + status +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", groupNumber=" + groupNumber +
                ", hasMaxNum=" + hasMaxNum +
                ", maxNum=" + maxNum +
                ", prodCount=" + prodCount +
                ", joinGroupTeamId=" + joinGroupTeamId +
                ", hasGroupTip=" + hasGroupTip +
                ", isPreheat=" + isPreheat +
                ", activityStatus=" + activityStatus +
                ", price=" + price +
                ", spuId=" + spuId +
                ", shopId=" + shopId +
                ", expiresIn=" + expiresIn +
                ", startIn=" + startIn +
                ", groupSkuList=" + groupSkuList +
                '}';
    }
}
