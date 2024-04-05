package com.mall4j.cloud.transfer.model;

import com.mall4j.cloud.common.model.BaseModel;

import java.io.Serializable;
import java.util.Date;
/**
 * 订单信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 23:32:08
 */
public class Order extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long orderId;

    private String orderNumber;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 下单导购ID
     */
    private Long buyStaffId;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 用户订单地址Id
     */
    private Long orderAddrId;

    /**
     * 配送类型 1:快递 2:自提 3：无需快递 4同城配送
     */
    private Integer deliveryType;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 总值
     */
    private Long total;

    /**
     * 实际总值
     */
    private Long actualTotal;

    /**
     * 订单运费
     */
    private Long freightAmount;

    /**
     * 订单使用积分
     */
    private Long orderScore;

    /**
     * 订单状态 1:待付款 2:待发货(待自提) 3:待收货(已发货) 5:成功 6:失败 7:待成团
     */
    private Integer status;

    /**
     * 支付方式 请参考枚举PayType
     */
    private Integer payType;

    /**
     * 订单类型1团购订单 2秒杀订单 3积分订单
     */
    private Integer orderType;

    /**
     * 订单商品总数
     */
    private Integer allCount;

    /**
     * 优惠总额
     */
    private Long reduceAmount;

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
     * 平台运费减免金额
     */
    private Long platformFreeFreightAmount;

    /**
     * 商家运费减免金额
     */
    private Long freeFreightAmount;

    /**
     * 店铺改价优惠金额
     */
    private Long shopChangeFreeAmount;

    /**
     * 触点号
     */
    private String tentacleNo;

    /**
     * 分销关系 1分销关系 2服务关系 3自主下单 4代客下单
     */
    private Integer distributionRelation;

    /**
     * 分销佣金
     */
    private Long distributionAmount;

    /**
     * 分销佣金状态 0-待结算 1-已结算-待提现 2-已结算-已提现
     */
    private Integer distributionStatus;

    /**
     * 分销用户类型 1-导购 2-微客
     */
    private Integer distributionUserType;

    /**
     * 分销用户ID (导购或微客)
     */
    private Long distributionUserId;

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
     * 发展佣金状态 0-待结算 1-已结算-待提现 2-已结算-已提现
     */
    private Integer developingStatus;

    /**
     * 发展用户ID (导购)
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

    /**
     * 平台优惠金额
     */
    private Long platformAmount;

    /**
     * 平台佣金
     */
    private Long platformCommission;

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
     * 结算时间
     */
    private Date settledTime;

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
     * 订单关闭原因 1-超时未支付 2-退款关闭 4-买家取消 15-已通过货到付款交易
     */
    private Integer closeType;

    /**
     * 订单退款状态（1:申请退款 2:退款成功 3:部分退款成功 4:退款失败）
     */
    private Integer refundStatus;

    /**
     * 卖家备注
     */
    private String shopRemarks;

    /**
     * 买家备注
     */
    private String remarks;

    /**
     * 用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除
     */
    private Integer deleteStatus;

    /**
     * 订单版本号，每处理一次订单，版本号+1
     */
    private Integer version;

    /**
     * 是否已经进行结算
     */
    private Integer isSettled;

    /**
     * 平台备注
     */
    private String platformRemarks;

    /**
     * 订单来源
     */
    private Integer orderSource;

    /**
     * 来源id
     */
    private String sourceId;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBuyStaffId() {
		return buyStaffId;
	}

	public void setBuyStaffId(Long buyStaffId) {
		this.buyStaffId = buyStaffId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getOrderAddrId() {
		return orderAddrId;
	}

	public void setOrderAddrId(Long orderAddrId) {
		this.orderAddrId = orderAddrId;
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

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getActualTotal() {
		return actualTotal;
	}

	public void setActualTotal(Long actualTotal) {
		this.actualTotal = actualTotal;
	}

	public Long getFreightAmount() {
		return freightAmount;
	}

	public void setFreightAmount(Long freightAmount) {
		this.freightAmount = freightAmount;
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

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getAllCount() {
		return allCount;
	}

	public void setAllCount(Integer allCount) {
		this.allCount = allCount;
	}

	public Long getReduceAmount() {
		return reduceAmount;
	}

	public void setReduceAmount(Long reduceAmount) {
		this.reduceAmount = reduceAmount;
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

	public Long getPlatformFreeFreightAmount() {
		return platformFreeFreightAmount;
	}

	public void setPlatformFreeFreightAmount(Long platformFreeFreightAmount) {
		this.platformFreeFreightAmount = platformFreeFreightAmount;
	}

	public Long getFreeFreightAmount() {
		return freeFreightAmount;
	}

	public void setFreeFreightAmount(Long freeFreightAmount) {
		this.freeFreightAmount = freeFreightAmount;
	}

	public Long getShopChangeFreeAmount() {
		return shopChangeFreeAmount;
	}

	public void setShopChangeFreeAmount(Long shopChangeFreeAmount) {
		this.shopChangeFreeAmount = shopChangeFreeAmount;
	}

	public String getTentacleNo() {
		return tentacleNo;
	}

	public void setTentacleNo(String tentacleNo) {
		this.tentacleNo = tentacleNo;
	}

	public Integer getDistributionRelation() {
		return distributionRelation;
	}

	public void setDistributionRelation(Integer distributionRelation) {
		this.distributionRelation = distributionRelation;
	}

	public Long getDistributionAmount() {
		return distributionAmount;
	}

	public void setDistributionAmount(Long distributionAmount) {
		this.distributionAmount = distributionAmount;
	}

	public Integer getDistributionStatus() {
		return distributionStatus;
	}

	public void setDistributionStatus(Integer distributionStatus) {
		this.distributionStatus = distributionStatus;
	}

	public Integer getDistributionUserType() {
		return distributionUserType;
	}

	public void setDistributionUserType(Integer distributionUserType) {
		this.distributionUserType = distributionUserType;
	}

	public Long getDistributionUserId() {
		return distributionUserId;
	}

	public void setDistributionUserId(Long distributionUserId) {
		this.distributionUserId = distributionUserId;
	}

	public Long getDistributionStoreId() {
		return distributionStoreId;
	}

	public void setDistributionStoreId(Long distributionStoreId) {
		this.distributionStoreId = distributionStoreId;
	}

	public Date getDistributionSettleTime() {
		return distributionSettleTime;
	}

	public void setDistributionSettleTime(Date distributionSettleTime) {
		this.distributionSettleTime = distributionSettleTime;
	}

	public Date getDistributionWithdrawTime() {
		return distributionWithdrawTime;
	}

	public void setDistributionWithdrawTime(Date distributionWithdrawTime) {
		this.distributionWithdrawTime = distributionWithdrawTime;
	}

	public Long getDevelopingAmount() {
		return developingAmount;
	}

	public void setDevelopingAmount(Long developingAmount) {
		this.developingAmount = developingAmount;
	}

	public Integer getDevelopingStatus() {
		return developingStatus;
	}

	public void setDevelopingStatus(Integer developingStatus) {
		this.developingStatus = developingStatus;
	}

	public Long getDevelopingUserId() {
		return developingUserId;
	}

	public void setDevelopingUserId(Long developingUserId) {
		this.developingUserId = developingUserId;
	}

	public Long getDevelopingStoreId() {
		return developingStoreId;
	}

	public void setDevelopingStoreId(Long developingStoreId) {
		this.developingStoreId = developingStoreId;
	}

	public Date getDevelopingSettleTime() {
		return developingSettleTime;
	}

	public void setDevelopingSettleTime(Date developingSettleTime) {
		this.developingSettleTime = developingSettleTime;
	}

	public Date getDevelopingWithdrawTime() {
		return developingWithdrawTime;
	}

	public void setDevelopingWithdrawTime(Date developingWithdrawTime) {
		this.developingWithdrawTime = developingWithdrawTime;
	}

	public Long getPlatformAmount() {
		return platformAmount;
	}

	public void setPlatformAmount(Long platformAmount) {
		this.platformAmount = platformAmount;
	}

	public Long getPlatformCommission() {
		return platformCommission;
	}

	public void setPlatformCommission(Long platformCommission) {
		this.platformCommission = platformCommission;
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

	public Date getSettledTime() {
		return settledTime;
	}

	public void setSettledTime(Date settledTime) {
		this.settledTime = settledTime;
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

	public Integer getCloseType() {
		return closeType;
	}

	public void setCloseType(Integer closeType) {
		this.closeType = closeType;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getShopRemarks() {
		return shopRemarks;
	}

	public void setShopRemarks(String shopRemarks) {
		this.shopRemarks = shopRemarks;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Integer deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getIsSettled() {
		return isSettled;
	}

	public void setIsSettled(Integer isSettled) {
		this.isSettled = isSettled;
	}

	public String getPlatformRemarks() {
		return platformRemarks;
	}

	public void setPlatformRemarks(String platformRemarks) {
		this.platformRemarks = platformRemarks;
	}

	public Integer getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(Integer orderSource) {
		this.orderSource = orderSource;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@Override
	public String toString() {
		return "Order{" +
				"orderId=" + orderId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",shopId=" + shopId +
				",userId=" + userId +
				",buyStaffId=" + buyStaffId +
				",storeId=" + storeId +
				",orderAddrId=" + orderAddrId +
				",deliveryType=" + deliveryType +
				",shopName=" + shopName +
				",total=" + total +
				",actualTotal=" + actualTotal +
				",freightAmount=" + freightAmount +
				",orderScore=" + orderScore +
				",status=" + status +
				",payType=" + payType +
				",orderType=" + orderType +
				",allCount=" + allCount +
				",reduceAmount=" + reduceAmount +
				",scoreAmount=" + scoreAmount +
				",memberAmount=" + memberAmount +
				",platformCouponAmount=" + platformCouponAmount +
				",shopCouponAmount=" + shopCouponAmount +
				",discountAmount=" + discountAmount +
				",platformFreeFreightAmount=" + platformFreeFreightAmount +
				",freeFreightAmount=" + freeFreightAmount +
				",shopChangeFreeAmount=" + shopChangeFreeAmount +
				",tentacleNo=" + tentacleNo +
				",distributionRelation=" + distributionRelation +
				",distributionAmount=" + distributionAmount +
				",distributionStatus=" + distributionStatus +
				",distributionUserType=" + distributionUserType +
				",distributionUserId=" + distributionUserId +
				",distributionStoreId=" + distributionStoreId +
				",distributionSettleTime=" + distributionSettleTime +
				",distributionWithdrawTime=" + distributionWithdrawTime +
				",developingAmount=" + developingAmount +
				",developingStatus=" + developingStatus +
				",developingUserId=" + developingUserId +
				",developingStoreId=" + developingStoreId +
				",developingSettleTime=" + developingSettleTime +
				",developingWithdrawTime=" + developingWithdrawTime +
				",platformAmount=" + platformAmount +
				",platformCommission=" + platformCommission +
				",payTime=" + payTime +
				",deliveryTime=" + deliveryTime +
				",finallyTime=" + finallyTime +
				",settledTime=" + settledTime +
				",cancelTime=" + cancelTime +
				",bookTime=" + bookTime +
				",isPayed=" + isPayed +
				",closeType=" + closeType +
				",refundStatus=" + refundStatus +
				",shopRemarks=" + shopRemarks +
				",remarks=" + remarks +
				",deleteStatus=" + deleteStatus +
				",version=" + version +
				",isSettled=" + isSettled +
				",platformRemarks=" + platformRemarks +
				",orderSource=" + orderSource +
				",sourceId=" + sourceId +
				'}';
	}
}
