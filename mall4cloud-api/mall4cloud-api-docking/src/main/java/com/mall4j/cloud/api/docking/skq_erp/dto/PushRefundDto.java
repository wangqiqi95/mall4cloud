package com.mall4j.cloud.api.docking.skq_erp.dto;

import com.mall4j.cloud.api.docking.skq_erp.config.IStdDataCheck;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 类描述：推退单到中台
 *
 * @date 2022/1/7 19:25：21
 */
public class PushRefundDto implements Serializable, IStdDataCheck {

	@ApiModelProperty(value = "退款单号", required = true)
	private String return_no;

	@ApiModelProperty(value = "退款状态，枚举申请退款 等待卖家同意（1）卖家同意退款/退货(2)买家已发货 等待卖家收货(3)退款完成（4）已拒绝（5）关闭（6）", required = true)
	private Integer return_status;

	@ApiModelProperty(value = "平台单号(对应本系统order_number)", required = true)
	private String order_no;

	@ApiModelProperty(value = "订单状态，枚举买家已付款 等待买家发货(WAIT_SELLER_SEND_GOODS)卖家已发货 等待买家收货(WAIT_BUYER_CONFIRM_GOODS)交易关闭（TRADE_CLOSED）")
	private String order_status;

	@ApiModelProperty(value = "子订单编号")
	private String sub_order_id;

	@ApiModelProperty(value = "退货类型，枚举仅退款(1）退货退款（2）换货（3）", required = true)
	private Integer refund_type;

	@ApiModelProperty(value = "买家昵称", required = true)
	private String buyer_nick;

	@ApiModelProperty(value = "买家备注")
	private String buyer_remark;

	@ApiModelProperty(value = "买家手机号", required = true)
	private String buyer_mobile;

	@ApiModelProperty(value = "退款原因", required = true)
	private String return_reason;

	@ApiModelProperty(value = "退款金额")
	private BigDecimal refund_amount;

	@ApiModelProperty(value = "退还邮费")
	private BigDecimal return_shipamount;

	@ApiModelProperty(value = "是否返还优惠券")
	private Integer is_return_coupon;

	@ApiModelProperty(value = "订单积分")
	private Integer order_credit;

	@ApiModelProperty(value = "退还积分")
	private Integer return_credit;

	@ApiModelProperty(value = "平台修改时间")
	private String modified;

	@ApiModelProperty(value = "平台申请时间", required = true)
	private String created;

	@ApiModelProperty(value = "退回快递")
	private String company_name;

	@ApiModelProperty(value = "退回物流单号")
	private String logistics_no;

	@ApiModelProperty(value = "店铺名称")
	private String cp_c_shop_title;

	@ApiModelProperty(value = "店铺id")
	private Integer cp_c_shop_id;

	@ApiModelProperty(value = "平台类型")
	private Integer cp_c_platform_id;

	@ApiModelProperty(value = "退单子表")
	private List<Item> items;

	public String getReturn_no() {
		return return_no;
	}

	public void setReturn_no(String return_no) {
		this.return_no = return_no;
	}

	public Integer getReturn_status() {
		return return_status;
	}

	public void setReturn_status(Integer return_status) {
		this.return_status = return_status;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getSub_order_id() {
		return sub_order_id;
	}

	public void setSub_order_id(String sub_order_id) {
		this.sub_order_id = sub_order_id;
	}

	public Integer getRefund_type() {
		return refund_type;
	}

	public void setRefund_type(Integer refund_type) {
		this.refund_type = refund_type;
	}

	public String getBuyer_nick() {
		return buyer_nick;
	}

	public void setBuyer_nick(String buyer_nick) {
		this.buyer_nick = buyer_nick;
	}

	public String getBuyer_remark() {
		return buyer_remark;
	}

	public void setBuyer_remark(String buyer_remark) {
		this.buyer_remark = buyer_remark;
	}

	public String getBuyer_mobile() {
		return buyer_mobile;
	}

	public void setBuyer_mobile(String buyer_mobile) {
		this.buyer_mobile = buyer_mobile;
	}

	public String getReturn_reason() {
		return return_reason;
	}

	public void setReturn_reason(String return_reason) {
		this.return_reason = return_reason;
	}

	public BigDecimal getRefund_amount() {
		return refund_amount;
	}

	public void setRefund_amount(BigDecimal refund_amount) {
		this.refund_amount = refund_amount;
	}

	public BigDecimal getReturn_shipamount() {
		return return_shipamount;
	}

	public void setReturn_shipamount(BigDecimal return_shipamount) {
		this.return_shipamount = return_shipamount;
	}

	public Integer getIs_return_coupon() {
		return is_return_coupon;
	}

	public void setIs_return_coupon(Integer is_return_coupon) {
		this.is_return_coupon = is_return_coupon;
	}

	public Integer getOrder_credit() {
		return order_credit;
	}

	public void setOrder_credit(Integer order_credit) {
		this.order_credit = order_credit;
	}

	public Integer getReturn_credit() {
		return return_credit;
	}

	public void setReturn_credit(Integer return_credit) {
		this.return_credit = return_credit;
	}

	public String getModified() {
		return modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getLogistics_no() {
		return logistics_no;
	}

	public void setLogistics_no(String logistics_no) {
		this.logistics_no = logistics_no;
	}

	public String getCp_c_shop_title() {
		return cp_c_shop_title;
	}

	public void setCp_c_shop_title(String cp_c_shop_title) {
		this.cp_c_shop_title = cp_c_shop_title;
	}

	public Integer getCp_c_shop_id() {
		return cp_c_shop_id;
	}

	public void setCp_c_shop_id(Integer cp_c_shop_id) {
		this.cp_c_shop_id = cp_c_shop_id;
	}

	public Integer getCp_c_platform_id() {
		return cp_c_platform_id;
	}

	public void setCp_c_platform_id(Integer cp_c_platform_id) {
		this.cp_c_platform_id = cp_c_platform_id;
	}


	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	@Override
	public String toString() {
		return "PushRefundDto{" +
				"return_no='" + return_no + '\'' +
				", return_status=" + return_status +
				", order_no='" + order_no + '\'' +
				", order_status='" + order_status + '\'' +
				", sub_order_id='" + sub_order_id + '\'' +
				", refund_type=" + refund_type +
				", buyer_nick='" + buyer_nick + '\'' +
				", buyer_remark='" + buyer_remark + '\'' +
				", buyer_mobile='" + buyer_mobile + '\'' +
				", return_reason='" + return_reason + '\'' +
				", refund_amount=" + refund_amount +
				", return_shipamount=" + return_shipamount +
				", is_return_coupon=" + is_return_coupon +
				", order_credit=" + order_credit +
				", return_credit=" + return_credit +
				", modified='" + modified + '\'' +
				", created='" + created + '\'' +
				", company_name='" + company_name + '\'' +
				", logistics_no='" + logistics_no + '\'' +
				", cp_c_shop_title='" + cp_c_shop_title + '\'' +
				", cp_c_shop_id=" + cp_c_shop_id +
				", cp_c_platform_id=" + cp_c_platform_id +
				", items=" + items +
				'}';
	}

	public static class Item {

		@ApiModelProperty(value = "退款单编号", required = true)
		private String return_no;

		@ApiModelProperty(value = "平台商品编号")
		private String productcode;

		@ApiModelProperty(value = "平台sku编号", required = true)
		private String sku_id;

		@ApiModelProperty(value = "商品标题")
		private String title;

		@ApiModelProperty(value = "商家商品编码")
		private String outer_goods_id;

		@ApiModelProperty(value = "商家商品条码")
		private String sku;

		@ApiModelProperty(value = "购买数量")
		private Integer quantity;

		@ApiModelProperty(value = "退货数量", required = true)
		private Integer return_quantity;

		@ApiModelProperty(value = "价格")
		private BigDecimal price;

		@ApiModelProperty(value = "是否赠品（0 不是赠品；1 是赠品）")
		private Integer is_gift;

		@ApiModelProperty(value = "子订单编号")
		private String sub_order_id;

		@ApiModelProperty(value = "退款金额")
		private BigDecimal refund_fee;

		@ApiModelProperty(value = "应付金额")
		private BigDecimal total_fee;

		@ApiModelProperty(value = "实付金额")
		private BigDecimal payment;

		@ApiModelProperty(value = "商品优惠")
		private BigDecimal discount_fee;

		public String getReturn_no() {
			return return_no;
		}

		public void setReturn_no(String return_no) {
			this.return_no = return_no;
		}

		public String getSub_order_id() {
			return sub_order_id;
		}

		public void setSub_order_id(String sub_order_id) {
			this.sub_order_id = sub_order_id;
		}

		public String getProductcode() {
			return productcode;
		}

		public void setProductcode(String productcode) {
			this.productcode = productcode;
		}

		public String getSku_id() {
			return sku_id;
		}

		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getOuter_goods_id() {
			return outer_goods_id;
		}

		public void setOuter_goods_id(String outer_goods_id) {
			this.outer_goods_id = outer_goods_id;
		}

		public String getSku() {
			return sku;
		}

		public void setSku(String sku) {
			this.sku = sku;
		}

		public Integer getQuantity() {
			return quantity;
		}

		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}

		public Integer getReturn_quantity() {
			return return_quantity;
		}

		public void setReturn_quantity(Integer return_quantity) {
			this.return_quantity = return_quantity;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
		}

		public Integer getIs_gift() {
			return is_gift;
		}

		public void setIs_gift(Integer is_gift) {
			this.is_gift = is_gift;
		}

		public BigDecimal getRefund_fee() {
			return refund_fee;
		}

		public void setRefund_fee(BigDecimal refund_fee) {
			this.refund_fee = refund_fee;
		}

		public BigDecimal getTotal_fee() {
			return total_fee;
		}

		public void setTotal_fee(BigDecimal total_fee) {
			this.total_fee = total_fee;
		}

		public BigDecimal getPayment() {
			return payment;
		}

		public void setPayment(BigDecimal payment) {
			this.payment = payment;
		}

		public BigDecimal getDiscount_fee() {
			return discount_fee;
		}

		public void setDiscount_fee(BigDecimal discount_fee) {
			this.discount_fee = discount_fee;
		}

		@Override
		public String toString() {
			return "Item{" +
					"return_no='" + return_no + '\'' +
					", productcode='" + productcode + '\'' +
					", sku_id='" + sku_id + '\'' +
					", title='" + title + '\'' +
					", outer_goods_id='" + outer_goods_id + '\'' +
					", sku='" + sku + '\'' +
					", quantity=" + quantity +
					", return_quantity=" + return_quantity +
					", price=" + price +
					", is_gift=" + is_gift +
					", sub_order_id='" + sub_order_id + '\'' +
					", refund_fee=" + refund_fee +
					", total_fee=" + total_fee +
					", payment=" + payment +
					", discount_fee=" + discount_fee +
					'}';
		}
	}

	@Override
	public ServerResponseEntity check() {
		return ServerResponseEntity.success();
	}
}
