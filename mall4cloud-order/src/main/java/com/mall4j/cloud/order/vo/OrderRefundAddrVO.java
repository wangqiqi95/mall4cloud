package com.mall4j.cloud.order.vo;

import com.mall4j.cloud.common.vo.BaseVO;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户退货物流地址VO
 *
 * @author FrozenWatermelon
 * @date 2021-03-09 13:44:31
 */
public class OrderRefundAddrVO extends BaseVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("物流ID")
    private Long refundAddrId;

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("退款号")
    private Long refundId;

    @ApiModelProperty("买家ID")
    private Long userId;

    @ApiModelProperty("物流公司ID")
    private Long deliveryCompanyId;

    @ApiModelProperty("物流公司名称")
    private String deliveryName;

    @ApiModelProperty("物流单号")
    private String deliveryNo;

    @ApiModelProperty("收件人姓名")
    private String consigneeName;

    @ApiModelProperty("收件人电话（顺丰快递需要）")
    private String consigneeMobile;

    @ApiModelProperty("收件人座机")
    private String consigneeTelephone;

    @ApiModelProperty("收件人邮政编码")
    private String consigneePostCode;

    @ApiModelProperty("收件人地址")
    private String consigneeAddr;

    @ApiModelProperty("发送人手机号码")
    private String senderMobile;

    @ApiModelProperty("买家备注")
    private String senderRemarks;

    @ApiModelProperty("图片凭证")
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
		return "OrderRefundAddrVO{" +
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
