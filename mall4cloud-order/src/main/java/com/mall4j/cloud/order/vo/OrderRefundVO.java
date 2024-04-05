package com.mall4j.cloud.order.vo;

import com.mall4j.cloud.api.biz.dto.channels.response.EcGetaftersaleorderResponse;
import com.mall4j.cloud.common.order.vo.RefundOrderItemVO;
import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 订单退款记录信息VO
 *
 * @author FrozenWatermelon
 * @date 2020-12-05 14:13:50
 */
@Data
public class OrderRefundVO extends BaseVO{

    @ApiModelProperty("记录ID")
    private Long refundId;

	@ApiModelProperty("退单编号")
	private String refundNumber;

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("买家ID")
    private Long userId;

    @ApiModelProperty("订单号")
    private Long orderId;

	@ApiModelProperty("订单编号")
	private String orderNumber;

	@ApiModelProperty("微信测订单编号")
	private String wechatOrderId;

    @ApiModelProperty("订单项ID(0:为全部订单项)")
    private Long orderItemId;

    @ApiModelProperty("退货数量(0:为全部订单项)")
    private Integer refundCount;

	@ApiModelProperty("退还积分")
	private Long refundScore;

	@ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("平台退款金额（退款时将这部分钱退回给平台，所以商家要扣除从平台这里获取的金额）")
    private Long platformRefundAmount;

    @ApiModelProperty("平台佣金退款金额")
    private Long platformRefundCommission;

    @ApiModelProperty("退款单总分销金额")
    private Long distributionTotalAmount;

    @ApiModelProperty("退款单类型（1:整单退款,2:单个物品退款）")
    private Integer refundType;

    @ApiModelProperty("申请类型:1,仅退款,2退款退货")
    private Integer applyType;

    @ApiModelProperty("申请来源 0-小程序 1-管理后台")
	private Integer applySource;

    @ApiModelProperty("是否接收到商品(1:已收到,0:未收到)")
    private Integer isReceived;

    @ApiModelProperty("退款关闭原因(1.买家撤销退款 2.卖家拒绝退款 3.退款申请超时被系统关闭)")
    private Integer closeType;

    @ApiModelProperty("处理退款状态:(1.买家申请 2.卖家接受 3.买家发货 4.卖家收货 5.退款成功  -1.退款关闭)详情见ReturnMoneyStsType")
    private Integer returnMoneySts;

    @ApiModelProperty("申请原因")
    private Integer buyerReason;

	@ApiModelProperty("申请原因 中文描述")
	private String buyerReasonValue;

    @ApiModelProperty("申请说明")
    private String buyerDesc;

    @ApiModelProperty("联系方式（退款时留下的手机号码）")
    private String buyerMobile;

    @ApiModelProperty("文件凭证(逗号隔开)")
    private String imgUrls;

    @ApiModelProperty("超时时间（超过该时间不处理，系统将自动处理）（保留字段）")
    private Date overTime;

    @ApiModelProperty("拒绝原因")
    private String rejectMessage;

    @ApiModelProperty("卖家备注")
    private String sellerMsg;

    @ApiModelProperty("受理时间")
    private Date handelTime;

    @ApiModelProperty("发货时间")
    private Date deliveryTime;

    @ApiModelProperty("收货时间")
    private Date receiveTime;

    @ApiModelProperty("关闭时间")
    private Date closeTime;

    @ApiModelProperty("确定时间(确定退款时间)")
    private Date decisionTime;

    @ApiModelProperty("退款时间")
    private Date refundTime;

	@ApiModelProperty("付款时间")
	private Date payTime;

	@ApiModelProperty("订单实付金额")
	private Long actualTotal;

	@ApiModelProperty("订单实付积分")
	private Long orderScore;

//	@ApiModelProperty("订单来源0普通订单 1小程序直播 2视频号3.0")
//	private Long orderSource;

	@ApiModelProperty("订单状态")
	private Integer orderStatus;

	@ApiModelProperty("退款状态")
	private Integer refundStatus;

	@ApiModelProperty(value = "店铺名称")
	private String shopName;
	@ApiModelProperty(value = "店铺code")
	private String shopCode;

	@ApiModelProperty(value = "支付的时候，支付id")
	private Long payId;

	@ApiModelProperty(value = "支付方式")
	private Integer payType;

	@ApiModelProperty("订单项")
	private List<RefundOrderItemVO> orderItems;

	@ApiModelProperty("退款地址信息")
	private OrderRefundAddrVO orderRefundAddr;

	@ApiModelProperty(value = "订单平台备注")
	private String orderPlatformRemarks;

	@ApiModelProperty(value = "平台备注")
	private String platformRemarks;
	@ApiModelProperty("物流单号")
	private String deliveryNo;
	@ApiModelProperty("物流单号填写时间")
	private Date deliveryCreateTime;
	private Long aftersaleId;
	@ApiModelProperty("运费退款金额")
	private Long refundFreightAmount;
	@ApiModelProperty("订单运费金额")
	private Long freightAmount;

	@ApiModelProperty("代客下单人id")
	private Long buyStaffId;

	@ApiModelProperty("视频号纠纷单id")
	private Long complaintOrderId;
	@ApiModelProperty("纠纷单状态用户是否已读")
	private Long complaintUserIsRead;
	@ApiModelProperty("纠纷单状态平台是否已读")
	private Long complaintPlatformIsRead;

	@ApiModelProperty("是否线下退款（线上退款失败） 0否1是")
	private Integer offlineRefund;
	@ApiModelProperty("线下退款状态0待上传退款凭证 1，用户确认 2 退款成功")
	private Integer offlineRefundStatus;

	@ApiModelProperty(value = "订单来源 0普通订单 1直播订单 2视频号3.0订单 3 视频号4.0订单", required = true)
	private Integer orderSource;
	@ApiModelProperty("视频号4.0售后单 微信接口返回的详情")
	private EcGetaftersaleorderResponse ecGetaftersaleorderResponse;



	public Long getRefundFreightAmount() {
		return refundFreightAmount;
	}

	public void setRefundFreightAmount(Long refundFreightAmount) {
		this.refundFreightAmount = refundFreightAmount;
	}

	public String getRefundNumber() {
		return refundNumber;
	}

	public void setRefundNumber(String refundNumber) {
		this.refundNumber = refundNumber;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getShopCode() {
		return shopCode;
	}

	public void setShopCode(String shopCode) {
		this.shopCode = shopCode;
	}

	public String getOrderPlatformRemarks() {
		return orderPlatformRemarks;
	}

	public void setOrderPlatformRemarks(String orderPlatformRemarks) {
		this.orderPlatformRemarks = orderPlatformRemarks;
	}

	public String getDeliveryNo() {
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public Long getAftersaleId() {
		return aftersaleId;
	}

	public void setAftersaleId(Long aftersaleId) {
		this.aftersaleId = aftersaleId;
	}

	public String getPlatformRemarks() {
		return platformRemarks;
	}

	public void setPlatformRemarks(String platformRemarks) {
		this.platformRemarks = platformRemarks;
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

	public Long getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(Long refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Long getPlatformRefundAmount() {
		return platformRefundAmount;
	}

	public void setPlatformRefundAmount(Long platformRefundAmount) {
		this.platformRefundAmount = platformRefundAmount;
	}

	public Long getPlatformRefundCommission() {
		return platformRefundCommission;
	}

	public void setPlatformRefundCommission(Long platformRefundCommission) {
		this.platformRefundCommission = platformRefundCommission;
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

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Long getActualTotal() {
		return actualTotal;
	}

	public void setActualTotal(Long actualTotal) {
		this.actualTotal = actualTotal;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public List<RefundOrderItemVO> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<RefundOrderItemVO> orderItems) {
		this.orderItems = orderItems;
	}

	public Long getRefundScore() {
		return refundScore;
	}

	public void setRefundScore(Long refundScore) {
		this.refundScore = refundScore;
	}

	public String getBuyerReasonValue() {
		return buyerReasonValue;
	}

	public void setBuyerReasonValue(String buyerReasonValue) {
		this.buyerReasonValue = buyerReasonValue;
	}

	public Long getOrderScore() {
		return orderScore;
	}

	public void setOrderScore(Long orderScore) {
		this.orderScore = orderScore;
	}

	public OrderRefundAddrVO getOrderRefundAddr() {
		return orderRefundAddr;
	}

	public void setOrderRefundAddr(OrderRefundAddrVO orderRefundAddr) {
		this.orderRefundAddr = orderRefundAddr;
	}

	public Long getPayId() {
		return payId;
	}

	public void setPayId(Long payId) {
		this.payId = payId;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Long getBuyStaffId() {
		return buyStaffId;
	}

	public void setBuyStaffId(Long buyStaffId) {
		this.buyStaffId = buyStaffId;
	}

	@Override
	public String toString() {
		return "OrderRefundVO{" +
				"refundId=" + refundId +
				", refundNumber='" + refundNumber + '\'' +
				", shopId=" + shopId +
				", userId=" + userId +
				", orderId=" + orderId +
				", orderNumber='" + orderNumber + '\'' +
				", orderItemId=" + orderItemId +
				", refundCount=" + refundCount +
				", refundScore=" + refundScore +
				", refundAmount=" + refundAmount +
				", platformRefundAmount=" + platformRefundAmount +
				", platformRefundCommission=" + platformRefundCommission +
				", distributionTotalAmount=" + distributionTotalAmount +
				", refundType=" + refundType +
				", applyType=" + applyType +
				", isReceived=" + isReceived +
				", closeType=" + closeType +
				", returnMoneySts=" + returnMoneySts +
				", buyerReason=" + buyerReason +
				", buyerReasonValue='" + buyerReasonValue + '\'' +
				", buyerDesc='" + buyerDesc + '\'' +
				", buyerMobile='" + buyerMobile + '\'' +
				", imgUrls='" + imgUrls + '\'' +
				", overTime=" + overTime +
				", rejectMessage='" + rejectMessage + '\'' +
				", sellerMsg='" + sellerMsg + '\'' +
				", handelTime=" + handelTime +
				", deliveryTime=" + deliveryTime +
				", receiveTime=" + receiveTime +
				", closeTime=" + closeTime +
				", decisionTime=" + decisionTime +
				", refundTime=" + refundTime +
				", payTime=" + payTime +
				", actualTotal=" + actualTotal +
				", orderScore=" + orderScore +
				", orderStatus=" + orderStatus +
				", refundStatus=" + refundStatus +
				", shopName='" + shopName + '\'' +
				", shopCode='" + shopCode + '\'' +
				", payId=" + payId +
				", payType=" + payType +
				", orderItems=" + orderItems +
				", orderRefundAddr=" + orderRefundAddr +
				", orderPlatformRemarks='" + orderPlatformRemarks + '\'' +
				", platformRemarks='" + platformRemarks + '\'' +
				", deliveryNo='" + deliveryNo + '\'' +
				", aftersaleId=" + aftersaleId +
				", refundFreightAmount=" + refundFreightAmount +
				", freightAmount=" + freightAmount +
				", buyStaffId=" + buyStaffId +
				'}';
	}
}
