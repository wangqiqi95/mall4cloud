package com.mall4j.cloud.api.openapi.skq_erp.dto;

import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：中台发货请求参数
*
 * @date 2022/1/6 22:49：42
 */
public class StdShipDto implements Serializable, IStdDataCheck {

	private static final long serialVersionUID = 5807275938174402340L;
	@ApiModelProperty(value = "中台订单id")
	private Integer order_id;

	@ApiModelProperty(value = "是否拆单，1，是；0否默认否")
	private Integer is_split;

	@ApiModelProperty(value = "平台单号")
	private String tid;

	@ApiModelProperty(value = "物流单号（多个用，拼接）")
	private String out_sid;

	@ApiModelProperty(value = "物流公司编码")
	private String company_code;

	@ApiModelProperty(value = "付款类型，1：在线付款，2：货到付款")
	private Integer paytype;

	@ApiModelProperty(value = "发货物流公司名称")
	private String logistics_company_name;

	@ApiModelProperty(value = "订单数量")
	private Integer size;

	private List<Delivery_order_item> delivery_order_items;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}

	public Integer getIs_split() {
		return is_split;
	}

	public void setIs_split(Integer is_split) {
		this.is_split = is_split;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getOut_sid() {
		return out_sid;
	}

	public void setOut_sid(String out_sid) {
		this.out_sid = out_sid;
	}

	public String getCompany_code() {
		return company_code;
	}

	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}

	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}

	public String getLogistics_company_name() {
		return logistics_company_name;
	}

	public void setLogistics_company_name(String logistics_company_name) {
		this.logistics_company_name = logistics_company_name;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public List<Delivery_order_item> getDelivery_order_items() {
		return delivery_order_items;
	}

	public void setDelivery_order_items(List<Delivery_order_item> delivery_order_items) {
		this.delivery_order_items = delivery_order_items;
	}

	@Override
	public String toString() {
		return "StdShipDto{" + "order_id=" + order_id + ", is_split=" + is_split + ", tid='" + tid + '\'' + ", out_sid='" + out_sid + '\'' + ", company_code='"
				+ company_code + '\'' + ", paytype=" + paytype + ", logistics_company_name='" + logistics_company_name + '\'' + ", size=" + size
				+ ", delivery_order_items=" + delivery_order_items + '}';
	}

	public static class Delivery_order_item implements IStdDataCheck {

		@ApiModelProperty(value = "中台商品款号")
		private String product_code;

		@ApiModelProperty(value = "数量")
		private Integer num;

		@ApiModelProperty(value = "实发数量")
		private Integer real_num;

		@ApiModelProperty(value = "缺发数量")
		private Integer lack_num;

		@ApiModelProperty(value = "子订单编号")
		private String item_id;

		@ApiModelProperty(value = "运单号")
		private String logistics_number;

		@ApiModelProperty(value = "物流公司代码")
		private String logistics_company_no;

		@ApiModelProperty(value = "平台sku条码编号")
		private String sku_id;

		@ApiModelProperty(value = "中台sku条码编号")
		private String sku;

		public String getProduct_code() {
			return product_code;
		}

		public void setProduct_code(String product_code) {
			this.product_code = product_code;
		}

		public Integer getNum() {
			return num;
		}

		public void setNum(Integer num) {
			this.num = num;
		}

		public Integer getReal_num() {
			return real_num;
		}

		public void setReal_num(Integer real_num) {
			this.real_num = real_num;
		}

		public Integer getLack_num() {
			return lack_num;
		}

		public void setLack_num(Integer lack_num) {
			this.lack_num = lack_num;
		}

		public String getItem_id() {
			return item_id;
		}

		public void setItem_id(String item_id) {
			this.item_id = item_id;
		}

		public String getLogistics_number() {
			return logistics_number;
		}

		public void setLogistics_number(String logistics_number) {
			this.logistics_number = logistics_number;
		}

		public String getLogistics_company_no() {
			return logistics_company_no;
		}

		public void setLogistics_company_no(String logistics_company_no) {
			this.logistics_company_no = logistics_company_no;
		}

		public String getSku_id() {
			return sku_id;
		}

		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		public String getSku() {
			return sku;
		}

		public void setSku(String sku) {
			this.sku = sku;
		}

		@Override
		public String toString() {
			return "Delivery_order_item{" + "product_code='" + product_code + '\'' + ", num=" + num + ", real_num=" + real_num + ", lack_num=" + lack_num
					+ ", item_id='" + item_id + '\'' + ", logistics_number='" + logistics_number + '\'' + ", logistics_company_no='" + logistics_company_no
					+ '\'' + ", sku_id='" + sku_id + '\'' + ", sku='" + sku + '\'' + '}';
		}

		@Override
		public StdResult check() {
			if (num == null) {
				return StdResult.fail("num不能为空");
			}
			if (real_num == null) {
				return StdResult.fail("realnum不能为空");
			}
			if (lack_num == null) {
				return StdResult.fail("lacknum不能为空");
			}
			if (StringUtils.isBlank(item_id)) {
				return StdResult.fail("item_id不能为空");
			}
			if (StringUtils.isBlank(logistics_number)) {
				return StdResult.fail("logistics_number不能为空");
			}
			if (StringUtils.isBlank(logistics_company_no)) {
				return StdResult.fail("logistics_company_no不能为空");
			}
			if (StringUtils.isBlank(sku_id)) {
				return StdResult.fail("sku_id不能为空");
			}
			if (StringUtils.isBlank(sku)) {
				return StdResult.fail("sku不能为空");
			}
			return StdResult.success();
		}
	}


	@Override
	public StdResult check() {
		if (order_id == null) {
			return StdResult.fail("order_id不能为空");
		}
		if (is_split == null) {
			return StdResult.fail("is_split不能为空");
		}
		if (StringUtils.isBlank(tid)) {
			return StdResult.fail("tid不能为空");
		}
		if (StringUtils.isBlank(out_sid)) {
			return StdResult.fail("out_sid不能为空");
		}
		if (StringUtils.isBlank(company_code)) {
			return StdResult.fail("company_code不能为空");
		}
		if (paytype == null) {
			return StdResult.fail("paytype不能为空");
		}
		if (StringUtils.isBlank(logistics_company_name)) {
			return StdResult.fail("logistics_company_name不能为空");
		}
		if (size == null) {
			return StdResult.fail("size不能为空");
		}
		if (!CollectionUtils.isEmpty(delivery_order_items)) {
			for (Delivery_order_item delivery_order_item : delivery_order_items) {
				StdResult check = delivery_order_item.check();
				if (check.fail()) {
					return check;
				}
			}
		}
		return StdResult.success();
	}
}
