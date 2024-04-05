package com.mall4j.cloud.group.bo;

/**
 * @author FrozenWatermelon
 * @date 2021/4/12
 */
public class GroupOrderBO {

    /**
     * 拼团活动id
     */
    private Long groupActivityId;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 拼团团队id
     */
    private Long groupTeamId;

    /**
     * user_id(当user_id为0时标识为机器人)
     */
    private Long userId;

    /**
     * 团长id
     */
    private Long shareUserId;

    /**
     * 活动商品金额
     */
    private Long activityProdPrice;

    /**
     * 支付金额
     */
    private Long payPrice;

    /**
     * 订单编号
     */
    private Long orderId;

    /**
     * 商品数量
     */
    private Integer count;

    private Long spuId;

    public Long getUserId() {
        return userId;
    }

    public Long getSpuId() {
        return spuId;
    }

    public void setSpuId(Long spuId) {
        this.spuId = spuId;
    }

    public Long getShareUserId() {
        return shareUserId;
    }

    public void setShareUserId(Long shareUserId) {
        this.shareUserId = shareUserId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupActivityId() {
        return groupActivityId;
    }

    public void setGroupActivityId(Long groupActivityId) {
        this.groupActivityId = groupActivityId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getGroupTeamId() {
        return groupTeamId;
    }

    public void setGroupTeamId(Long groupTeamId) {
        this.groupTeamId = groupTeamId;
    }

    public Long getActivityProdPrice() {
        return activityProdPrice;
    }

    public void setActivityProdPrice(Long activityProdPrice) {
        this.activityProdPrice = activityProdPrice;
    }

    public Long getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Long payPrice) {
        this.payPrice = payPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "GroupOrderBO{" +
                ", groupActivityId=" + groupActivityId +
                ", shopId=" + shopId +
                ", groupTeamId=" + groupTeamId +
                ", userId=" + userId +
                ", shareUserId=" + shareUserId +
                ", activityProdPrice=" + activityProdPrice +
                ", payPrice=" + payPrice +
                ", orderId=" + orderId +
                ", count=" + count +
                ", spuId=" + spuId +
                '}';
    }
}
