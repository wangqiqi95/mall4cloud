package com.mall4j.cloud.order.model;

import java.io.Serializable;

import com.mall4j.cloud.common.model.BaseModel;
/**
 * 用户退货物流地址
 *
 * @author FrozenWatermelon
 * @date 2021-03-09 13:44:31
 */
public class OrderRefundAddr extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 物流ID
     */
    private Long refundAddrId;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 退款号
     */
    private Long refundId;

    /**
     * 买家ID
     */
    private Long userId;

    /**
     * 物流公司ID
     */
    private Long deliveryCompanyId;

    /**
     * 物流公司名称
     */
    private String deliveryName;

    /**
     * 物流单号
     */
    private String deliveryNo;

    /**
     * 收件人姓名
     */
    private String consigneeName;

    /**
     * 收件人电话（顺丰快递需要）
     */
    private String consigneeMobile;

    /**
     * 收件人座机
     */
    private String consigneeTelephone;

    /**
     * 收件人邮政编码
     */
    private String consigneePostCode;

    /**
     * 收件人地址
     */
    private String consigneeAddr;

    /**
     * 发送人手机号码
     */
    private String senderMobile;

    /**
     * 买家备注
     */
    private String senderRemarks;

    /**
     * 图片凭证
     */
    private String imgs;

	public Long getRefundAddrId() {
		return refundAddrId;
	}

	public void setRefundAddrId(Long refundAddrId) {
		this.refundAddrId = refundAddrId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getRefundId() {
		return refundId;
	}

	public void setRefundId(Long refundId) {
		this.refundId = refundId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getDeliveryCompanyId() {
		return deliveryCompanyId;
	}

	public void setDeliveryCompanyId(Long deliveryCompanyId) {
		this.deliveryCompanyId = deliveryCompanyId;
	}

	public String getDeliveryName() {
		return deliveryName;
	}

	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}

	public String getDeliveryNo() {
		return deliveryNo;
	}

	public void setDeliveryNo(String deliveryNo) {
		this.deliveryNo = deliveryNo;
	}

	public String getConsigneeName() {
		return consigneeName;
	}

	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}

	public String getConsigneeMobile() {
		return consigneeMobile;
	}

	public void setConsigneeMobile(String consigneeMobile) {
		this.consigneeMobile = consigneeMobile;
	}

	public String getConsigneeTelephone() {
		return consigneeTelephone;
	}

	public void setConsigneeTelephone(String consigneeTelephone) {
		this.consigneeTelephone = consigneeTelephone;
	}

	public String getConsigneePostCode() {
		return consigneePostCode;
	}

	public void setConsigneePostCode(String consigneePostCode) {
		this.consigneePostCode = consigneePostCode;
	}

	public String getConsigneeAddr() {
		return consigneeAddr;
	}

	public void setConsigneeAddr(String consigneeAddr) {
		this.consigneeAddr = consigneeAddr;
	}

	public String getSenderMobile() {
		return senderMobile;
	}

	public void setSenderMobile(String senderMobile) {
		this.senderMobile = senderMobile;
	}

	public String getSenderRemarks() {
		return senderRemarks;
	}

	public void setSenderRemarks(String senderRemarks) {
		this.senderRemarks = senderRemarks;
	}

	public String getImgs() {
		return imgs;
	}

	public void setImgs(String imgs) {
		this.imgs = imgs;
	}

	@Override
	public String toString() {
		return "OrderRefundAddr{" +
				"refundAddrId=" + refundAddrId +
				",createTime=" + createTime +
				",updateTime=" + updateTime +
				",shopId=" + shopId +
				",refundId=" + refundId +
				",userId=" + userId +
				",deliveryCompanyId=" + deliveryCompanyId +
				",deliveryName=" + deliveryName +
				",deliveryNo=" + deliveryNo +
				",consigneeName=" + consigneeName +
				",consigneeMobile=" + consigneeMobile +
				",consigneeTelephone=" + consigneeTelephone +
				",consigneePostCode=" + consigneePostCode +
				",consigneeAddr=" + consigneeAddr +
				",senderMobile=" + senderMobile +
				",senderRemarks=" + senderRemarks +
				",imgs=" + imgs +
				'}';
	}
}
