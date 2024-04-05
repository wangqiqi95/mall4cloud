package com.mall4j.cloud.common.order.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * @author FrozenWatermelon
 * @date 2021/2/5
 */
public class EsOrderVO {

    @ApiModelProperty("下单用户ID")
    private Long userId;

    @ApiModelProperty("用户名称")
    private String userName;

    @ApiModelProperty("用户手机号")
    private String userMobile;

    @ApiModelProperty(value = "订单项",required=true)
    private List<EsOrderItemVO> orderItems;

    @ApiModelProperty(value = "订单号",required=true)
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("微信端订单编号")
    private String wechatOrderId;

    @ApiModelProperty(value = "总价",required=true)
    private Long actualTotal;

    @ApiModelProperty(value = "使用积分",required=true)
    private Long orderScore;

    @ApiModelProperty(value = "订单状态",required=true)
    private Integer status;

    @ApiModelProperty(value = "订单类型(0普通订单 1团购订单 2秒杀订单)",required=true)
    private Integer orderType;

    @ApiModelProperty(value = "订单退款状态（1:申请退款 2:退款成功 3:部分退款成功 4:退款失败）",required=true)
    private Integer refundStatus;

    @ApiModelProperty(value = "配送类型 1:快递 2:自提 3：无需快递",required=true)
    private Integer deliveryType;

    @ApiModelProperty(value = "店铺名称",required=true)
    private String shopName;

    @ApiModelProperty(value = "店铺名称")
    private String tentacleNo;

    @ApiModelProperty(value = "店铺id",required=true)
    private Long shopId;

    @ApiModelProperty(value = "订单运费",required=true)
    private Long freightAmount;

    @ApiModelProperty(value = "订单创建时间",required=true)
    private Date createTime;

    @ApiModelProperty(value = "商品总数",required=true)
    private Integer allCount;

    @ApiModelProperty(value = "发票订单id")
    private Long orderInvoiceId;

    @ApiModelProperty(value = "用户备注信息")
    private String remarks;

    @ApiModelProperty(value = "收货人姓名")
    private String consignee;

    @ApiModelProperty(value = "收货人手机号")
    private String mobile;

    /**
     * 平台运费减免金额
     */
    private Long platformFreeFreightAmount;

    /**
     * 用户订单地址Id
     */
    private Long orderAddrId;

    /**
     * 总值
     */
    private Long total;

    /**
     * 卖家备注
     */
    private String shopRemarks;

    /**
     * 支付方式 请参考枚举PayType
     */
    private Integer payType;

    /**
     * 订单关闭原因 1-超时未支付 2-退款关闭 4-买家取消 15-已通过货到付款交易
     */
    private Integer closeType;

    /**
     * 付款时间
     */
    private Date payTime;

    /**
     * 发货时间
     */
    private Date deliveryTime;

    /**
     * 完成时间
     */
    private Date finallyTime;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 预售发货时间
     */
    private Date bookTime;

    /**
     * 是否已支付，1.已支付0.未支付
     */
    private Integer isPayed;

    /**
     * 用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除
     */
    private Integer deleteStatus;

    /**
     * 积分抵扣金额
     */
    private Long scoreAmount;

    /**
     * 会员折扣金额
     */
    private Long memberAmount;

    /**
     * 平台优惠券优惠金额
     */
    private Long platformCouponAmount;

    /**
     * 商家优惠券优惠金额
     */
    private Long shopCouponAmount;

    /**
     * 满减优惠金额
     */
    private Long discountAmount;

    /**
     * 平台优惠金额
     */
    private Long platformAmount;

    /**
     * 优惠总额
     */
    private Long reduceAmount;

    @ApiModelProperty(value = "平台备注")
    private String platformRemarks;

    private Integer orderSource;

    private Integer sourceId;

    private Long storeId;

    private String storeName;

    private String traceId;
    private String promoterId;
    private String finderNickname;
    private String sharerOpenid;

    /**
     * 能否修改或者查看物流 1.可以，2.不可以
     */
    private Integer updateOrViewDeliveryInfo;

    private Integer jointVentureCommissionStatus;

    private Integer jointVentureRefundStatus;

    private Boolean isStaffOrder;


    public String getWechatOrderId() {
        return wechatOrderId;
    }

    public void setWechatOrderId(String wechatOrderId) {
        this.wechatOrderId = wechatOrderId;
    }

    public String getTentacleNo() {
        return tentacleNo;
    }

    public void setTentacleNo(String tentacleNo) {
        this.tentacleNo = tentacleNo;
    }

    public String getPromoterId() {
        return promoterId;
    }

    public void setPromoterId(String promoterId) {
        this.promoterId = promoterId;
    }

    public String getFinderNickname() {
        return finderNickname;
    }

    public void setFinderNickname(String finderNickname) {
        this.finderNickname = finderNickname;
    }

    public String getSharerOpenid() {
        return sharerOpenid;
    }

    public void setSharerOpenid(String sharerOpenid) {
        this.sharerOpenid = sharerOpenid;
    }

    public Boolean getStaffOrder() {
        return isStaffOrder;
    }

    public void setStaffOrder(Boolean staffOrder) {
        isStaffOrder = staffOrder;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Integer getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(Integer orderSource) {
        this.orderSource = orderSource;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getPlatformRemarks() {
        return platformRemarks;
    }

    public void setPlatformRemarks(String platformRemarks) {
        this.platformRemarks = platformRemarks;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public Integer getUpdateOrViewDeliveryInfo() {
        return updateOrViewDeliveryInfo;
    }

    public void setUpdateOrViewDeliveryInfo(Integer updateOrViewDeliveryInfo) {
        this.updateOrViewDeliveryInfo = updateOrViewDeliveryInfo;
    }

    public List<EsOrderItemVO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<EsOrderItemVO> orderItems) {
        this.orderItems = orderItems;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getActualTotal() {
        return actualTotal;
    }

    public void setActualTotal(Long actualTotal) {
        this.actualTotal = actualTotal;
    }

    public Long getOrderScore() {
        return orderScore;
    }

    public void setOrderScore(Long orderScore) {
        this.orderScore = orderScore;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(Long freightAmount) {
        this.freightAmount = freightAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getOrderInvoiceId() {
        return orderInvoiceId;
    }

    public void setOrderInvoiceId(Long orderInvoiceId) {
        this.orderInvoiceId = orderInvoiceId;
    }

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getOrderAddrId() {
        return orderAddrId;
    }

    public void setOrderAddrId(Long orderAddrId) {
        this.orderAddrId = orderAddrId;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getShopRemarks() {
        return shopRemarks;
    }

    public void setShopRemarks(String shopRemarks) {
        this.shopRemarks = shopRemarks;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getCloseType() {
        return closeType;
    }

    public void setCloseType(Integer closeType) {
        this.closeType = closeType;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
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

    public Date getBookTime() {
        return bookTime;
    }

    public void setBookTime(Date bookTime) {
        this.bookTime = bookTime;
    }

    public Integer getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(Integer isPayed) {
        this.isPayed = isPayed;
    }

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
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

    public Long getShopCouponAmount() {
        return shopCouponAmount;
    }

    public void setShopCouponAmount(Long shopCouponAmount) {
        this.shopCouponAmount = shopCouponAmount;
    }

    public Long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Long getPlatformAmount() {
        return platformAmount;
    }

    public void setPlatformAmount(Long platformAmount) {
        this.platformAmount = platformAmount;
    }

    public Long getReduceAmount() {
        return reduceAmount;
    }

    public void setReduceAmount(Long reduceAmount) {
        this.reduceAmount = reduceAmount;
    }

    public Long getPlatformFreeFreightAmount() {
        return platformFreeFreightAmount;
    }

    public void setPlatformFreeFreightAmount(Long platformFreeFreightAmount) {
        this.platformFreeFreightAmount = platformFreeFreightAmount;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getJointVentureCommissionStatus() {
        return jointVentureCommissionStatus;
    }

    public void setJointVentureCommissionStatus(Integer jointVentureCommissionStatus) {
        this.jointVentureCommissionStatus = jointVentureCommissionStatus;
    }

    public Integer getJointVentureRefundStatus() {
        return jointVentureRefundStatus;
    }

    public void setJointVentureRefundStatus(Integer jointVentureRefundStatus) {
        this.jointVentureRefundStatus = jointVentureRefundStatus;
    }

    @Override
    public String toString() {
        return "EsOrderVO{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userMobile='" + userMobile + '\'' +
                ", orderItems=" + orderItems +
                ", orderId=" + orderId +
                ", orderNumber='" + orderNumber + '\'' +
                ", actualTotal=" + actualTotal +
                ", orderScore=" + orderScore +
                ", status=" + status +
                ", orderType=" + orderType +
                ", refundStatus=" + refundStatus +
                ", deliveryType=" + deliveryType +
                ", shopName='" + shopName + '\'' +
                ", tentacleNo='" + tentacleNo + '\'' +
                ", shopId=" + shopId +
                ", freightAmount=" + freightAmount +
                ", createTime=" + createTime +
                ", allCount=" + allCount +
                ", orderInvoiceId=" + orderInvoiceId +
                ", remarks='" + remarks + '\'' +
                ", consignee='" + consignee + '\'' +
                ", mobile='" + mobile + '\'' +
                ", platformFreeFreightAmount=" + platformFreeFreightAmount +
                ", orderAddrId=" + orderAddrId +
                ", total=" + total +
                ", shopRemarks='" + shopRemarks + '\'' +
                ", payType=" + payType +
                ", closeType=" + closeType +
                ", payTime=" + payTime +
                ", deliveryTime=" + deliveryTime +
                ", finallyTime=" + finallyTime +
                ", cancelTime=" + cancelTime +
                ", bookTime=" + bookTime +
                ", isPayed=" + isPayed +
                ", deleteStatus=" + deleteStatus +
                ", scoreAmount=" + scoreAmount +
                ", memberAmount=" + memberAmount +
                ", platformCouponAmount=" + platformCouponAmount +
                ", shopCouponAmount=" + shopCouponAmount +
                ", discountAmount=" + discountAmount +
                ", platformAmount=" + platformAmount +
                ", reduceAmount=" + reduceAmount +
                ", platformRemarks='" + platformRemarks + '\'' +
                ", orderSource=" + orderSource +
                ", sourceId=" + sourceId +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", traceId='" + traceId + '\'' +
                ", updateOrViewDeliveryInfo=" + updateOrViewDeliveryInfo +
                ", jointVentureCommissionStatus=" + jointVentureCommissionStatus +
                ", jointVentureRefundStatus=" + jointVentureRefundStatus +
                '}';
    }
}
