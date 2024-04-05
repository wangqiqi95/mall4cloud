package com.mall4j.cloud.api.order.vo;

import com.mall4j.cloud.api.order.vo.OrderAddrVO;
import com.mall4j.cloud.common.order.vo.OrderItemVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单下的每个店铺
 *
 * @author FrozenWatermelon
 */
@Data
public class OrderShopVO implements Serializable {

    @ApiModelProperty("订单号")
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("是否当前导购订单")
    private Boolean currentStaffOrder;

    @ApiModelProperty(value = "门店id", required = true)
    private Long storeId;

    /**
     * 店铺ID
     **/
    @ApiModelProperty(value = "店铺id", required = true)
    private Long shopId;

    /**
     * 店铺名称
     **/
    @ApiModelProperty(value = "店铺名称", required = true)
    private String shopName;

    @ApiModelProperty(value = "实际总值", required = true)
    private Long actualTotal;

    @ApiModelProperty(value = "商品总值", required = true)
    private Long total;

    @ApiModelProperty(value = "商品总数", required = true)
    private Integer totalNum;

    @ApiModelProperty(value = "地址Dto", required = true)
    private OrderAddrVO orderAddr;

    @ApiModelProperty(value = "支付方式",required=true)
    private Integer payType;

    @ApiModelProperty(value = "产品信息", required = true)
    private List<OrderItemVO> orderItems;

    @ApiModelProperty(value = "运费", required = true)
    private Long transfee;

    @ApiModelProperty(value = "优惠总额", required = true)
    private Long reduceAmount;

    @ApiModelProperty(value = "促销活动优惠金额", required = true)
    private Long discountMoney;

    @ApiModelProperty(value = "店铺优惠券优惠金额", required = true)
    private Long shopCouponMoney;

    @ApiModelProperty(value = "订单创建时间", required = true)
    private Date createTime;

    @ApiModelProperty(value = "订单过期时间")
    private Date endTime;

    @ApiModelProperty(value = "订单付款时间")
    private Date payTime;

    @ApiModelProperty(value = "订单发货时间")
    private Date deliveryTime;

    @ApiModelProperty(value = "订单完成时间")
    private Date finallyTime;

    @ApiModelProperty(value = "订单取消时间")
    private Date cancelTime;

    @ApiModelProperty(value = "订单更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "订单备注信息", required = true)
    private String remarks;

    @ApiModelProperty(value = "配送类型 1:快递 2:自提 3：无需快递", required = true)
    private Integer deliveryType;

    @ApiModelProperty(value = "订单类型（1团购订单 2秒杀订单）", required = true)
    private Integer orderType;

    @ApiModelProperty(value = "订单状态", required = true)
    private Integer status;

    @ApiModelProperty(value = "店铺优惠金额")
    private Long shopAmount;

    @ApiModelProperty(value = "积分抵扣金额")
    private Long scoreAmount;

    @ApiModelProperty(value = "会员折扣金额")
    private Long memberAmount;

    @ApiModelProperty(value = "平台优惠券优惠金额")
    private Long platformCouponAmount;

    @ApiModelProperty(value = "平台运费减免金额")
    private Long platformFreeFreightAmount;

    @ApiModelProperty(value = "订单积分")
    private Long orderScore;

    @ApiModelProperty(value = "能否退款", required = true)
    private Boolean canRefund;

    @ApiModelProperty(value = "能否整单退款", required = true)
    private Boolean canAllRefund;

    @ApiModelProperty("最终的退款id")
    private Long finalRefundId;

    @ApiModelProperty(value = "当前可退款金额")
    private Long canRefundAmount;

    @ApiModelProperty(value = "店铺改价优惠金额")
    private Long shopChangeFreeAmount;

    @ApiModelProperty(value = "能否修改或者查看物流 1.可以，2.不可以")
    private Integer updateOrViewDeliveryInfo;

    @ApiModelProperty(value = "订单发票id")
    private Long orderInvoiceId;

    @ApiModelProperty(value = "视频号，跟踪ID")
    private String traceId;


    /**
     * 分销关系 1分享关系 2服务关系 3自主下单 4代客下单
     */
    private Integer distributionRelation;

    /**
     * 分销佣金
     */
    private Long distributionAmount;

    /**
     * 分销佣金状态 0待结算 1已结算 2已提现 3提现中
     */
    private Integer distributionStatus;

    /**
     * 分销用户ID
     */
    private Long distributionUserId;

    /**
     * 分销用户类型 1-导购 2-微客
     */
    private Integer distributionUserType;

    /**
     * 分销用户门店ID
     */
    private Long distributionStoreId;

    /**
     * 分销佣金结算时间
     */
    private Date distributionSettleTime;

    /**
     * 分销佣金提现时间
     */
    private Date distributionWithdrawTime;

    /**
     * 发展佣金
     */
    private Long developingAmount;

    /**
     * 发展佣金状态 0待结算 1已结算 2已提现 3提现中
     */
    private Integer developingStatus;

    /**
     * 发展用户ID
     */
    private Long developingUserId;

    /**
     * 发展用户门店ID
     */
    private Long developingStoreId;

    /**
     * 发展佣金结算时间
     */
    private Date developingSettleTime;

    /**
     * 发展佣金提现时间
     */
    private Date developingWithdrawTime;

    @ApiModelProperty(value = "订单来源 0普通订单 1直播订单 2视频号3.0订单 3 视频号4.0订单")
    private Integer orderSource;


    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getCurrentStaffOrder() {
        return currentStaffOrder;
    }

    public void setCurrentStaffOrder(Boolean currentStaffOrder) {
        this.currentStaffOrder = currentStaffOrder;
    }

    public Long getShopChangeFreeAmount() {
        return shopChangeFreeAmount;
    }

    public void setShopChangeFreeAmount(Long shopChangeFreeAmount) {
        this.shopChangeFreeAmount = shopChangeFreeAmount;
    }

    public Integer getUpdateOrViewDeliveryInfo() {
        return updateOrViewDeliveryInfo;
    }

    public void setUpdateOrViewDeliveryInfo(Integer updateOrViewDeliveryInfo) {
        this.updateOrViewDeliveryInfo = updateOrViewDeliveryInfo;
    }

    public Long getScoreAmount() {
        return scoreAmount;
    }

    public void setScoreAmount(Long scoreAmount) {
        this.scoreAmount = scoreAmount;
    }

    public Long getMemberAmount() {
        return memberAmount;
    }

    public void setMemberAmount(Long memberAmount) {
        this.memberAmount = memberAmount;
    }

    public Long getPlatformCouponAmount() {
        return platformCouponAmount;
    }

    public void setPlatformCouponAmount(Long platformCouponAmount) {
        this.platformCouponAmount = platformCouponAmount;
    }

    public Long getPlatformFreeFreightAmount() {
        return platformFreeFreightAmount;
    }

    public void setPlatformFreeFreightAmount(Long platformFreeFreightAmount) {
        this.platformFreeFreightAmount = platformFreeFreightAmount;
    }

    public Long getShopAmount() {
        return shopAmount;
    }

    public void setShopAmount(Long shopAmount) {
        this.shopAmount = shopAmount;
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

    public Long getActualTotal() {
        return actualTotal;
    }

    public void setActualTotal(Long actualTotal) {
        this.actualTotal = actualTotal;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public OrderAddrVO getOrderAddr() {
        return orderAddr;
    }

    public void setOrderAddr(OrderAddrVO orderAddr) {
        this.orderAddr = orderAddr;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public List<OrderItemVO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemVO> orderItems) {
        this.orderItems = orderItems;
    }

    public Long getTransfee() {
        return transfee;
    }

    public void setTransfee(Long transfee) {
        this.transfee = transfee;
    }

    public Long getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(Long reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public Long getDiscountMoney() {
        return discountMoney;
    }

    public void setDiscountMoney(Long discountMoney) {
        this.discountMoney = discountMoney;
    }

    public Long getShopCouponMoney() {
        return shopCouponMoney;
    }

    public void setShopCouponMoney(Long shopCouponMoney) {
        this.shopCouponMoney = shopCouponMoney;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getFinallyTime() {
        return finallyTime;
    }

    public void setFinallyTime(Date finallyTime) {
        this.finallyTime = finallyTime;
    }

    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getCanRefund() {
        return canRefund;
    }

    public void setCanRefund(Boolean canRefund) {
        this.canRefund = canRefund;
    }

    public Boolean getCanAllRefund() {
        return canAllRefund;
    }

    public void setCanAllRefund(Boolean canAllRefund) {
        this.canAllRefund = canAllRefund;
    }

    public Long getOrderScore() {
        return orderScore;
    }

    public void setOrderScore(Long orderScore) {
        this.orderScore = orderScore;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Long getFinalRefundId() {
        return finalRefundId;
    }

    public void setFinalRefundId(Long finalRefundId) {
        this.finalRefundId = finalRefundId;
    }

    public Long getCanRefundAmount() {
        return canRefundAmount;
    }

    public void setCanRefundAmount(Long canRefundAmount) {
        this.canRefundAmount = canRefundAmount;
    }

    public Long getOrderInvoiceId() {
        return orderInvoiceId;
    }

    public void setOrderInvoiceId(Long orderInvoiceId) {
        this.orderInvoiceId = orderInvoiceId;
    }


    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "OrderShopVO{" +
                "userId=" + userId +
                ", currentStaffOrder=" + currentStaffOrder +
                ", shopId=" + shopId +
                ", shopName='" + shopName + '\'' +
                ", actualTotal=" + actualTotal +
                ", total=" + total +
                ", totalNum=" + totalNum +
                ", orderAddr=" + orderAddr +
                ", payType=" + payType +
                ", orderItems=" + orderItems +
                ", transfee=" + transfee +
                ", reduceAmount=" + reduceAmount +
                ", discountMoney=" + discountMoney +
                ", shopCouponMoney=" + shopCouponMoney +
                ", createTime=" + createTime +
                ", payTime=" + payTime +
                ", deliveryTime=" + deliveryTime +
                ", finallyTime=" + finallyTime +
                ", cancelTime=" + cancelTime +
                ", updateTime=" + updateTime +
                ", remarks='" + remarks + '\'' +
                ", deliveryType=" + deliveryType +
                ", orderType=" + orderType +
                ", status=" + status +
                ", shopAmount=" + shopAmount +
                ", scoreAmount=" + scoreAmount +
                ", memberAmount=" + memberAmount +
                ", platformCouponAmount=" + platformCouponAmount +
                ", platformFreeFreightAmount=" + platformFreeFreightAmount +
                ", orderScore=" + orderScore +
                ", canRefund=" + canRefund +
                ", canAllRefund=" + canAllRefund +
                ", finalRefundId=" + finalRefundId +
                ", canRefundAmount=" + canRefundAmount +
                ", shopChangeFreeAmount=" + shopChangeFreeAmount +
                ", updateOrViewDeliveryInfo=" + updateOrViewDeliveryInfo +
                ", orderInvoiceId=" + orderInvoiceId +
                '}';
    }
}
