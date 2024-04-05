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
 * @date 2022/1/7 13:59：43
 */
public class PushOrderDto implements Serializable, IStdDataCheck {

	private static final long serialVersionUID = 6573988171542758902L;
	@ApiModelProperty(value = "平台单号", required = true)
	private String tid;

	@ApiModelProperty(value = "订单状态，数据枚举： WAIT_BUYER_PAY（待付款） WAIT_SELLER_SEND_GOODS（买家已付款 等待卖家发） WAIT_BUYER_CONFIRM_GOODS（卖家已发货 等待买家收货 ） TRADE_FINISHED（交易完成 ） TRADE_CANCELED（交易关闭） NO_DELIVERY(不可发货，如拼团订单)", required = true)
	private String status;

	@ApiModelProperty(value = "交易类型/支付方式,取值如下：在线支付 货到付款 京东支付 云闪付 微信 支付宝 银行卡 礼品卡 门店付款 赠品 现金 其他", required = true)
	private String type;

	@ApiModelProperty(value = "订单类型，枚举值如下：0（普通）1（预售）2（换货）3（分销采购单）4（ 补发单）101（员工内购单）", required = true)
	private Integer order_type;

	@ApiModelProperty(value = "商品数量（订单商品总数量）", required = true)
	private Integer num;

	@ApiModelProperty(value = "商品金额（商品销售价总和）", required = true)
	private BigDecimal total_fee;

	@ApiModelProperty(value = "整单优惠金额（订单级别的优惠，不包含商品级别优惠，如店铺优惠券）", required = true)
	private BigDecimal discount_fee;

	@ApiModelProperty(value = "调整金额（如卖家手动后台调整金额，等于订单明细的调整金额之和，若平台不能给到明细级别的调整金额，则明细调整金额设置为空，OMS取头表调整金额并自己分摊到明细）", required = true)
	private BigDecimal adjust_fee;

	@ApiModelProperty(value = "物流费", required = true)
	private BigDecimal post_fee;

	@ApiModelProperty(value = "实付金额，买家实际支付金额", required = true)
	private BigDecimal payment;

	@ApiModelProperty(value = "交易佣金")
	private BigDecimal commission_fee;

	@ApiModelProperty(value = "平台补贴,订单优惠金额中平台承担的部分")
	private BigDecimal plantfor_count;

	@ApiModelProperty(value = "平台下单时间 格式 yyyy-MM-dd HH:mm:ss", required = true)
	private String trade_create_time;

	@ApiModelProperty(value = "付款时间 格式 yyyy-MM-dd HH:mm:ss", required = true)
	private String pay_time;

	@ApiModelProperty(value = "交易结束时间 格式 yyyy-MM-dd HH:mm:ss")
	private String trade_end_time;

	@ApiModelProperty(value = "发货时间 格式 yyyy-MM-dd HH:mm:ss")
	private String send_time;

	@ApiModelProperty(value = "平台修改时间 格式 yyyy-MM-dd HH:mm:ss")
	private String trade_update_time;

	@ApiModelProperty(value = "卖家备注")
	private String seller_memo;

	@ApiModelProperty(value = "下单门店编号")
	private String sales_cp_c_store_ecode;

	@ApiModelProperty(value = "买家昵称", required = true)
	private String buyer_nick;

	@ApiModelProperty(value = "买家邮箱")
	private String buyer_email;

	@ApiModelProperty(value = "买家留言")
	private String buyer_message;

	@ApiModelProperty(value = "异常描述")
	private String mark_desc;

	@ApiModelProperty(value = "收货人姓名", required = true)
	private String receiver_name;

	@ApiModelProperty(value = "收货省名称", required = true)
	private String receiver_province;

	@ApiModelProperty(value = "收货市名称", required = true)
	private String receiver_city;

	@ApiModelProperty(value = "收货区/县名称", required = true)
	private String receiver_district;

	@ApiModelProperty(value = "收货详细地址", required = true)
	private String receiver_address;

	@ApiModelProperty(value = "收货人邮编")
	private String receiver_zip;

	@ApiModelProperty(value = "收货人电话")
	private String receiver_phone;

	@ApiModelProperty(value = "收货人手机号", required = true)
	private String receiver_mobile;

	@ApiModelProperty(value = "物流类型，0:商家配送; 1:平台配送; 2:虚拟 3.自提")
	private String shipping_type;

	@ApiModelProperty(value = "物流单号")
	private String deliverno;

	@ApiModelProperty(value = "物流公司编码")
	private String logisticscompany;

	@ApiModelProperty(value = "发票抬头")
	private String invoice_name;

	@ApiModelProperty(value = "发票类型，可取值：普通发票 增值税专用发票")
	private String invoicetype;

	@ApiModelProperty(value = "预留字段12（自提/发货门店ID）")
	private String reserve_bigint02;

	@ApiModelProperty(value = "平台名称")
	private String cp_c_platform_ename;

	@ApiModelProperty(value = "实付积分")
	private String reserve_decimal02;

	@ApiModelProperty(value = "分销佣金")
	private String reserve_decimal03;

	@ApiModelProperty(value = "导购员WID")
	private String reserve_bigint01;

	@ApiModelProperty(value = "销售门店ID")
	private String reserve_bigint03;

	@ApiModelProperty(value = "商品销售模式")
	private String reserve_bigint04;

	@ApiModelProperty(value = "发货前退款有退单，1：有退单，0：无退单")
	private String reserve_bigint05;

	@ApiModelProperty(value = "导购员名称")
	private String reserve_varchar01;

	@ApiModelProperty(value = "自提/发货门店名称")
	private String reserve_varchar02;

	@ApiModelProperty(value = "买家手机号")
	private String reserve_varchar03;

	@ApiModelProperty(value = "国家")
	private String reserve_varchar04;

	@ApiModelProperty(value = "主播账号")
	private String reserve_varchar05;

	@ApiModelProperty(value = "微客手机号")
	private String weike_phone;
	@ApiModelProperty(value = "小程序下单会员号")
	private String order_vip_id;
	@ApiModelProperty(value = "是否是团购订单")
	private Integer is_group_purchase;
	@ApiModelProperty(value = "订单优惠券码")
	private String coupon_code;
	@ApiModelProperty(value = "导购员工号")
	private String staff_no;
	@ApiModelProperty(value = "虚拟门店id")
	private String virtual_store_id;
	@ApiModelProperty(value = "虚拟门店名称")
	private String virtual_store_name;
	@ApiModelProperty(value = "优惠券id")
	private String coupon_id;
	@ApiModelProperty(value = "优惠券名称")
	private String coupon_name;

	public String getCoupon_id() {
		return coupon_id;
	}

	public void setCoupon_id(String coupon_id) {
		this.coupon_id = coupon_id;
	}

	public String getCoupon_name() {
		return coupon_name;
	}

	public void setCoupon_name(String coupon_name) {
		this.coupon_name = coupon_name;
	}

	public String getVirtual_store_id() {
		return virtual_store_id;
	}

	public void setVirtual_store_id(String virtual_store_id) {
		this.virtual_store_id = virtual_store_id;
	}

	public String getVirtual_store_name() {
		return virtual_store_name;
	}

	public void setVirtual_store_name(String virtual_store_name) {
		this.virtual_store_name = virtual_store_name;
	}

	@ApiModelProperty(value = "订单项", required = true)
	private List<Item> items;

	public String getWeike_phone() {
		return weike_phone;
	}

	public void setWeike_phone(String weike_phone) {
		this.weike_phone = weike_phone;
	}

	public String getOrder_vip_id() {
		return order_vip_id;
	}

	public void setOrder_vip_id(String order_vip_id) {
		this.order_vip_id = order_vip_id;
	}

	public Integer getIs_group_purchase() {
		return is_group_purchase;
	}

	public void setIs_group_purchase(Integer is_group_purchase) {
		this.is_group_purchase = is_group_purchase;
	}

	public String getCoupon_code() {
		return coupon_code;
	}

	public void setCoupon_code(String coupon_code) {
		this.coupon_code = coupon_code;
	}

	public String getStaff_no() {
		return staff_no;
	}

	public void setStaff_no(String staff_no) {
		this.staff_no = staff_no;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getOrder_type() {
		return order_type;
	}

	public void setOrder_type(Integer order_type) {
		this.order_type = order_type;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public BigDecimal getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(BigDecimal total_fee) {
		this.total_fee = total_fee;
	}

	public BigDecimal getDiscount_fee() {
		return discount_fee;
	}

	public void setDiscount_fee(BigDecimal discount_fee) {
		this.discount_fee = discount_fee;
	}

	public BigDecimal getAdjust_fee() {
		return adjust_fee;
	}

	public void setAdjust_fee(BigDecimal adjust_fee) {
		this.adjust_fee = adjust_fee;
	}

	public BigDecimal getPost_fee() {
		return post_fee;
	}

	public void setPost_fee(BigDecimal post_fee) {
		this.post_fee = post_fee;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public BigDecimal getCommission_fee() {
		return commission_fee;
	}

	public void setCommission_fee(BigDecimal commission_fee) {
		this.commission_fee = commission_fee;
	}

	public BigDecimal getPlantfor_count() {
		return plantfor_count;
	}

	public void setPlantfor_count(BigDecimal plantfor_count) {
		this.plantfor_count = plantfor_count;
	}

	public String getTrade_create_time() {
		return trade_create_time;
	}

	public void setTrade_create_time(String trade_create_time) {
		this.trade_create_time = trade_create_time;
	}

	public String getPay_time() {
		return pay_time;
	}

	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}

	public String getTrade_end_time() {
		return trade_end_time;
	}

	public void setTrade_end_time(String trade_end_time) {
		this.trade_end_time = trade_end_time;
	}

	public String getSend_time() {
		return send_time;
	}

	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}

	public String getTrade_update_time() {
		return trade_update_time;
	}

	public void setTrade_update_time(String trade_update_time) {
		this.trade_update_time = trade_update_time;
	}

	public String getSeller_memo() {
		return seller_memo;
	}

	public void setSeller_memo(String seller_memo) {
		this.seller_memo = seller_memo;
	}

	public String getBuyer_nick() {
		return buyer_nick;
	}

	public void setBuyer_nick(String buyer_nick) {
		this.buyer_nick = buyer_nick;
	}

	public String getBuyer_email() {
		return buyer_email;
	}

	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}

	public String getBuyer_message() {
		return buyer_message;
	}

	public void setBuyer_message(String buyer_message) {
		this.buyer_message = buyer_message;
	}

	public String getMark_desc() {
		return mark_desc;
	}

	public void setMark_desc(String mark_desc) {
		this.mark_desc = mark_desc;
	}

	public String getReceiver_name() {
		return receiver_name;
	}

	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}

	public String getReceiver_province() {
		return receiver_province;
	}

	public void setReceiver_province(String receiver_province) {
		this.receiver_province = receiver_province;
	}

	public String getReceiver_city() {
		return receiver_city;
	}

	public void setReceiver_city(String receiver_city) {
		this.receiver_city = receiver_city;
	}

	public String getReceiver_district() {
		return receiver_district;
	}

	public void setReceiver_district(String receiver_district) {
		this.receiver_district = receiver_district;
	}

	public String getReceiver_address() {
		return receiver_address;
	}

	public void setReceiver_address(String receiver_address) {
		this.receiver_address = receiver_address;
	}

	public String getReceiver_zip() {
		return receiver_zip;
	}

	public void setReceiver_zip(String receiver_zip) {
		this.receiver_zip = receiver_zip;
	}

	public String getReceiver_phone() {
		return receiver_phone;
	}

	public void setReceiver_phone(String receiver_phone) {
		this.receiver_phone = receiver_phone;
	}

	public String getReceiver_mobile() {
		return receiver_mobile;
	}

	public void setReceiver_mobile(String receiver_mobile) {
		this.receiver_mobile = receiver_mobile;
	}

	public String getShipping_type() {
		return shipping_type;
	}

	public void setShipping_type(String shipping_type) {
		this.shipping_type = shipping_type;
	}

	public String getDeliverno() {
		return deliverno;
	}

	public void setDeliverno(String deliverno) {
		this.deliverno = deliverno;
	}

	public String getLogisticscompany() {
		return logisticscompany;
	}

	public void setLogisticscompany(String logisticscompany) {
		this.logisticscompany = logisticscompany;
	}

	public String getInvoice_name() {
		return invoice_name;
	}

	public void setInvoice_name(String invoice_name) {
		this.invoice_name = invoice_name;
	}

	public String getInvoicetype() {
		return invoicetype;
	}

	public void setInvoicetype(String invoicetype) {
		this.invoicetype = invoicetype;
	}

	public String getReserve_bigint02() {
		return reserve_bigint02;
	}

	public void setReserve_bigint02(String reserve_bigint02) {
		this.reserve_bigint02 = reserve_bigint02;
	}

	public String getCp_c_platform_ename() {
		return cp_c_platform_ename;
	}

	public void setCp_c_platform_ename(String cp_c_platform_ename) {
		this.cp_c_platform_ename = cp_c_platform_ename;
	}

	public String getReserve_decimal02() {
		return reserve_decimal02;
	}

	public void setReserve_decimal02(String reserve_decimal02) {
		this.reserve_decimal02 = reserve_decimal02;
	}

	public String getReserve_decimal03() {
		return reserve_decimal03;
	}

	public void setReserve_decimal03(String reserve_decimal03) {
		this.reserve_decimal03 = reserve_decimal03;
	}

	public String getReserve_bigint01() {
		return reserve_bigint01;
	}

	public void setReserve_bigint01(String reserve_bigint01) {
		this.reserve_bigint01 = reserve_bigint01;
	}

	public String getReserve_bigint03() {
		return reserve_bigint03;
	}

	public void setReserve_bigint03(String reserve_bigint03) {
		this.reserve_bigint03 = reserve_bigint03;
	}

	public String getReserve_bigint04() {
		return reserve_bigint04;
	}

	public void setReserve_bigint04(String reserve_bigint04) {
		this.reserve_bigint04 = reserve_bigint04;
	}

	public String getReserve_bigint05() {
		return reserve_bigint05;
	}

	public void setReserve_bigint05(String reserve_bigint05) {
		this.reserve_bigint05 = reserve_bigint05;
	}

	public String getReserve_varchar01() {
		return reserve_varchar01;
	}

	public void setReserve_varchar01(String reserve_varchar01) {
		this.reserve_varchar01 = reserve_varchar01;
	}

	public String getReserve_varchar02() {
		return reserve_varchar02;
	}

	public void setReserve_varchar02(String reserve_varchar02) {
		this.reserve_varchar02 = reserve_varchar02;
	}

	public String getReserve_varchar03() {
		return reserve_varchar03;
	}

	public void setReserve_varchar03(String reserve_varchar03) {
		this.reserve_varchar03 = reserve_varchar03;
	}

	public String getReserve_varchar04() {
		return reserve_varchar04;
	}

	public void setReserve_varchar04(String reserve_varchar04) {
		this.reserve_varchar04 = reserve_varchar04;
	}

	public String getReserve_varchar05() {
		return reserve_varchar05;
	}

	public void setReserve_varchar05(String reserve_varchar05) {
		this.reserve_varchar05 = reserve_varchar05;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public String getSales_cp_c_store_ecode() {
		return sales_cp_c_store_ecode;
	}

	public void setSales_cp_c_store_ecode(String sales_cp_c_store_ecode) {
		this.sales_cp_c_store_ecode = sales_cp_c_store_ecode;
	}


	@Override
	public String toString() {
		return "PushOrderDto{" + "tid='" + tid + '\'' + ", status='" + status + '\'' + ", type='" + type + '\'' + ", order_type=" + order_type + ", num=" + num
				+ ", total_fee=" + total_fee + ", discount_fee=" + discount_fee + ", adjust_fee=" + adjust_fee + ", post_fee=" + post_fee + ", payment="
				+ payment + ", commission_fee=" + commission_fee + ", plantfor_count=" + plantfor_count + ", trade_create_time='" + trade_create_time + '\''
				+ ", pay_time='" + pay_time + '\'' + ", trade_end_time='" + trade_end_time + '\'' + ", send_time='" + send_time + '\'' + ", trade_update_time='"
				+ trade_update_time + '\'' + ", seller_memo='" + seller_memo + '\'' + ", buyer_nick='" + buyer_nick + '\'' + ", buyer_email='" + buyer_email
				+ '\'' + ", buyer_message='" + buyer_message + '\'' + ", mark_desc='" + mark_desc + '\'' + ", receiver_name='" + receiver_name + '\''
				+ ", receiver_province='" + receiver_province + '\'' + ", receiver_city='" + receiver_city + '\'' + ", receiver_district='" + receiver_district
				+ '\'' + ", receiver_address='" + receiver_address + '\'' + ", receiver_zip='" + receiver_zip + '\'' + ", receiver_phone='" + receiver_phone
				+ '\'' + ", receiver_mobile='" + receiver_mobile + '\'' + ", shipping_type='" + shipping_type + '\'' + ", deliverno='" + deliverno + '\''
				+ ", logisticscompany='" + logisticscompany + '\'' + ", invoice_name='" + invoice_name + '\'' + ", invoicetype='" + invoicetype + '\''
				+ ", reserve_bigint02='" + reserve_bigint02 + '\'' + ", cp_c_platform_ename='" + cp_c_platform_ename + '\'' + ", reserve_decimal02='"
				+ reserve_decimal02 + '\'' + ", reserve_decimal03='" + reserve_decimal03 + '\'' + ", reserve_bigint01='" + reserve_bigint01 + '\''
				+ ", reserve_bigint03='" + reserve_bigint03 + '\'' + ", reserve_bigint04='" + reserve_bigint04 + '\'' + ", reserve_bigint05='"
				+ reserve_bigint05 + '\'' + ", reserve_varchar01='" + reserve_varchar01 + '\'' + ", reserve_varchar02='" + reserve_varchar02 + '\''
				+ ", reserve_varchar03='" + reserve_varchar03 + '\'' + ", reserve_varchar04='" + reserve_varchar04 + '\'' + ", reserve_varchar05='"
				+ reserve_varchar05 + '\'' + ", items=" + items + '}';
	}

	public static class Item {

		@ApiModelProperty(value = "平台订单号", required = true)
		private String tid;

		@ApiModelProperty(value = "子订单编号，明细行单号，必须唯一", required = true)
		private String oid;

		@ApiModelProperty(value = "交易状态，可取枚举：WAIT_BUYER_PAY（待付款）WAIT_SELLER_SEND_GOODS（买家已付款 等待卖家发）WAIT_BUYER_CONFIRM_GOODS（卖家已发货 等待买家收货）TRADE_FINISHED（交易完成）TRADE_CANCELED（交易关闭）NO_DELIVERY(不可发货，如拼团订单)", required = true)
		private String status;

		@ApiModelProperty(value = "退款状态，可取枚举：0(未申请退款),1(申请退款，等待卖家同意),2(卖家同意退款/退货),3(买家已发货，等待卖家收货),4(卖家拒绝退款),5(退款关闭),6(退款完成)")
		private String refund_status;

		@ApiModelProperty(value = "商品标题", required = true)
		private String title;

		@ApiModelProperty(value = "品牌名")
		private String brandname;

		@ApiModelProperty(value = "平台商品编码")
		private String num_iid;

		@ApiModelProperty(value = "平台条码编码", required = true)
		private String sku_id;

		@ApiModelProperty(value = "外部商品货号")
		private String outer_iid;

		@ApiModelProperty(value = "下单门店编号")
		private String sales_cp_c_store_ecode;

		@ApiModelProperty(value = "外部商品条码", required = true)
		private String outer_sku_id;

		@ApiModelProperty(value = "商品数量", required = true)
		private String num;

		@ApiModelProperty(value = "商品原价", required = true)
		private BigDecimal price;

		@ApiModelProperty(value = "应付金额，当前商品的应付金额", required = true)
		private BigDecimal total_fee;

		@ApiModelProperty(value = "实付金额，当前商品买家实际支付金额", required = true)
		private BigDecimal payment;

		@ApiModelProperty(value = "商品优惠，商品级别的优惠，如商品促销优惠", required = true)
		private BigDecimal discount_fee;

		@ApiModelProperty(value = "平台优惠平摊金额")
		private BigDecimal adjust_fee;

		@ApiModelProperty(value = "平台优惠平摊金额")
		private BigDecimal reserve_decimal01;

		@ApiModelProperty(value = "平台修改时间，格式：yyyy-MM-dd HH:mm:ss")
		private String order_update_time;

		@ApiModelProperty(value = "条码属性，如颜色分类:梦幻粉6310;尺码:35码")
		private String sku_properties_name;

		@ApiModelProperty(value = "图片路径")
		private String pic_path;

		@ApiModelProperty(value = "买家留言")
		private String buyer_message;

		@ApiModelProperty(value = "推广者昵称")
		private String reserve_varchar03;

		@ApiModelProperty(value = "推广者ID")
		private String reserve_decimal02;

		public String getSales_cp_c_store_ecode() {
			return sales_cp_c_store_ecode;
		}

		public void setSales_cp_c_store_ecode(String sales_cp_c_store_ecode) {
			this.sales_cp_c_store_ecode = sales_cp_c_store_ecode;
		}

		public String getTid() {
			return tid;
		}

		public void setTid(String tid) {
			this.tid = tid;
		}

		public String getOid() {
			return oid;
		}

		public void setOid(String oid) {
			this.oid = oid;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getRefund_status() {
			return refund_status;
		}

		public void setRefund_status(String refund_status) {
			this.refund_status = refund_status;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getBrandname() {
			return brandname;
		}

		public void setBrandname(String brandname) {
			this.brandname = brandname;
		}

		public String getNum_iid() {
			return num_iid;
		}

		public void setNum_iid(String num_iid) {
			this.num_iid = num_iid;
		}

		public String getSku_id() {
			return sku_id;
		}

		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		public String getOuter_iid() {
			return outer_iid;
		}

		public void setOuter_iid(String outer_iid) {
			this.outer_iid = outer_iid;
		}

		public String getOuter_sku_id() {
			return outer_sku_id;
		}

		public void setOuter_sku_id(String outer_sku_id) {
			this.outer_sku_id = outer_sku_id;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}

		public BigDecimal getPrice() {
			return price;
		}

		public void setPrice(BigDecimal price) {
			this.price = price;
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

		public BigDecimal getAdjust_fee() {
			return adjust_fee;
		}

		public void setAdjust_fee(BigDecimal adjust_fee) {
			this.adjust_fee = adjust_fee;
		}

		public BigDecimal getReserve_decimal01() {
			return reserve_decimal01;
		}

		public void setReserve_decimal01(BigDecimal reserve_decimal01) {
			this.reserve_decimal01 = reserve_decimal01;
		}

		public String getOrder_update_time() {
			return order_update_time;
		}

		public void setOrder_update_time(String order_update_time) {
			this.order_update_time = order_update_time;
		}

		public String getSku_properties_name() {
			return sku_properties_name;
		}

		public void setSku_properties_name(String sku_properties_name) {
			this.sku_properties_name = sku_properties_name;
		}

		public String getPic_path() {
			return pic_path;
		}

		public void setPic_path(String pic_path) {
			this.pic_path = pic_path;
		}

		public String getBuyer_message() {
			return buyer_message;
		}

		public void setBuyer_message(String buyer_message) {
			this.buyer_message = buyer_message;
		}

		public String getReserve_varchar03() {
			return reserve_varchar03;
		}

		public void setReserve_varchar03(String reserve_varchar03) {
			this.reserve_varchar03 = reserve_varchar03;
		}

		public String getReserve_decimal02() {
			return reserve_decimal02;
		}

		public void setReserve_decimal02(String reserve_decimal02) {
			this.reserve_decimal02 = reserve_decimal02;
		}

		@Override
		public String toString() {
			return "Item{" + "tid='" + tid + '\'' + ", oid='" + oid + '\'' + ", status='" + status + '\'' + ", refund_status='" + refund_status + '\''
					+ ", title='" + title + '\'' + ", brandname='" + brandname + '\'' + ", num_iid='" + num_iid + '\'' + ", sku_id='" + sku_id + '\''
					+ ", outer_iid='" + outer_iid + '\'' + ", outer_sku_id='" + outer_sku_id + '\'' + ", num='" + num + '\'' + ", price=" + price
					+ ", total_fee=" + total_fee + ", payment=" + payment + ", discount_fee=" + discount_fee + ", adjust_fee=" + adjust_fee
					+ ", reserve_decimal01=" + reserve_decimal01 + ", order_update_time='" + order_update_time + '\'' + ", sku_properties_name='"
					+ sku_properties_name + '\'' + ", pic_path='" + pic_path + '\'' + ", buyer_message='" + buyer_message + '\'' + ", reserve_varchar03='"
					+ reserve_varchar03 + '\'' + ", reserve_decimal02='" + reserve_decimal02 + '\'' + '}';
		}
	}


	@Override
	public ServerResponseEntity check() {
		return ServerResponseEntity.success();
	}
}
