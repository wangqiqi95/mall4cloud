package com.mall4j.cloud.transfer.model;

import java.io.Serializable;
import java.util.Date;
import com.mall4j.cloud.common.model.BaseModel;
/**
 * 订单退款记录信息
 *
 * @author FrozenWatermelon
 * @date 2022-04-04 23:32:09
 */
public class OrderRefund extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    private Long refundId;

    private String refundNumber;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 买家ID
     */
    private Long userId;

    /**
     * 订单号
     */
    private Long orderId;

    /**
     * 订单项ID(0:为全部订单项)
     */
    private Long orderItemId;

    /**
     * 退货数量(0:为全部订单项)
     */
    private Integer refundCount;

    /**
     * 退还积分
     */
    private Long refundScore;

    /**
     * 退款金额
     */
    private Long refundAmount;

    /**
     * 平台佣金退款金额
     */
    private Long platformRefundCommission;

    /**
     * 平台退款金额（退款时将这部分钱退回给平台，所以商家要扣除从平台这里获取的金额）
     */
    private Long platformRefundAmount;

    /**
     * 退款单总分销金额
     */
    private Long distributionTotalAmount;

    /**
     * 退款单类型（1:整单退款,2:单个物品退款）
     */
    private Integer refundType;

    /**
     * 申请类型:1,仅退款,2退款退货
     */
    private Integer applyType;

    /**
     * 是否接收到商品(1:已收到,0:未收到)
     */
    private Integer isReceived;

    /**
     * 退款关闭原因(1.买家撤销退款 2.卖家拒绝退款 3.退款申请超时被系统关闭)
     */
    private Integer closeType;

    /**
     * 处理退款状态:(1.买家申请 2.卖家接受 3.买家发货 4.卖家收货 5.退款成功  -1.退款关闭)详情见ReturnMoneyStsType
     */
    private Integer returnMoneySts;

    /**
     * 申请原因(具体见BuyerReasonType)
     */
    private Integer buyerReason;

    /**
     * 申请说明
     */
    private String buyerDesc;

    /**
     * 联系方式（退款时留下的手机号码）
     */
    private String buyerMobile;

    /**
     * 文件凭证(逗号隔开)
     */
    private String imgUrls;

    /**
     * 超时时间（超过该时间不处理，系统将自动处理）（保留字段）
     */
    private Date overTime;

    /**
     * 拒绝原因
     */
    private String rejectMessage;

    /**
     * 卖家备注
     */
    private String sellerMsg;

    /**
     * 受理时间
     */
    private Date handelTime;

    /**
     * 发货时间
     */
    private Date deliveryTime;

    /**
     * 收货时间
     */
    private Date receiveTime;

    /**
     * 关闭时间
     */
    private Date closeTime;

    /**
     * 确定时间(确定退款时间)
     */
    private Date decisionTime;

    /**
     * 退款时间
     */
    private Date refundTime;

    public String getRefundNumber() {
        return refundNumber;
    }

    public void setRefundNumber(String refundNumber) {
        this.refundNumber = refundNumber;
    }

    public Long getRefundId() {
		return refundId;
	}

	public void setRefundId(Long refundId) {
		this.refundId = refundId;
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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Integer getRefundCount() {
		return refundCount;
	}

	public void setRefundCount(Integer refundCount) {
		this.refundCount = refundCount;
	}

	public Long getRefundScore() {
		return refundScore;
	}

	public void setRefundScore(Long refundScore) {
		this.refundScore = refundScore;
	}

	public Long getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Long refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Long getPlatformRefundCommission() {
		return platformRefundCommission;
	}

	public void setPlatformRefundCommission(Long platformRefundCommission) {
		this.platformRefundCommission = platformRefundCommission;
	}

	public Long getPlatformRefundAmount() {
		return platformRefundAmount;
	}

	public void setPlatformRefundAmount(Long platformRefundAmount) {
		this.platformRefundAmount = platformRefundAmount;
	}

	public Long getDistributionTotalAmount() {
		return distributionTotalAmount;
	}

	public void setDistributionTotalAmount(Long distributionTotalAmount) {
		this.distributionTotalAmount = distributionTotalAmount;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}

	public Integer getApplyType() {
		return applyType;
	}

	public void setApplyType(Integer applyType) {
		this.applyType = applyType;
	}

	public Integer getIsReceived() {
		return isReceived;
	}

	public void setIsReceived(Integer isReceived) {
		this.isReceived = isReceived;
	}

	public Integer getCloseType() {
		return closeType;
	}

	public void setCloseType(Integer closeType) {
		this.closeType = closeType;
	}

	public Integer getReturnMoneySts() {
		return returnMoneySts;
	}

	public void setReturnMoneySts(Integer returnMoneySts) {
		this.returnMoneySts = returnMoneySts;
	}

	public Integer getBuyerReason() {
		return buyerReason;
	}

	public void setBuyerReason(Integer buyerReason) {
		this.buyerReason = buyerReason;
	}

	public String getBuyerDesc() {
		return buyerDesc;
	}

	public void setBuyerDesc(String buyerDesc) {
		this.buyerDesc = buyerDesc;
	}

	public String getBuyerMobile() {
		return buyerMobile;
	}

	public void setBuyerMobile(String buyerMobile) {
		this.buyerMobile = buyerMobile;
	}

	public String getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(String imgUrls) {
		this.imgUrls = imgUrls;
	}

	public Date getOverTime() {
		return overTime;
	}

	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}

	public String getRejectMessage() {
		return rejectMessage;
	}

	public void setRejectMessage(String rejectMessage) {
		this.rejectMessage = rejectMessage;
	}

	public String getSellerMsg() {
		return sellerMsg;
	}

	public void setSellerMsg(String sellerMsg) {
		this.sellerMsg = sellerMsg;
	}

	public Date getHandelTime() {
		return handelTime;
	}

	public void setHandelTime(Date handelTime) {
		this.handelTime = handelTime;
	}

	public Date getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Date deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public Date getDecisionTime() {
		return decisionTime;
	}

	public void setDecisionTime(Date decisionTime) {
		this.decisionTime = decisionTime;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	@Override
	public String toString() {
		return "OrderRefund{" +
				"refundId=" + refundId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",shopId=" + shopId +
				",userId=" + userId +
				",orderId=" + orderId +
				",orderItemId=" + orderItemId +
				",refundCount=" + refundCount +
				",refundScore=" + refundScore +
				",refundAmount=" + refundAmount +
				",platformRefundCommission=" + platformRefundCommission +
				",platformRefundAmount=" + platformRefundAmount +
				",distributionTotalAmount=" + distributionTotalAmount +
				",refundType=" + refundType +
				",applyType=" + applyType +
				",isReceived=" + isReceived +
				",closeType=" + closeType +
				",returnMoneySts=" + returnMoneySts +
				",buyerReason=" + buyerReason +
				",buyerDesc=" + buyerDesc +
				",buyerMobile=" + buyerMobile +
				",imgUrls=" + imgUrls +
				",overTime=" + overTime +
				",rejectMessage=" + rejectMessage +
				",sellerMsg=" + sellerMsg +
				",handelTime=" + handelTime +
				",deliveryTime=" + deliveryTime +
				",receiveTime=" + receiveTime +
				",closeTime=" + closeTime +
				",decisionTime=" + decisionTime +
				",refundTime=" + refundTime +
				'}';
	}
}
