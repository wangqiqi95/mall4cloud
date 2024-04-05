package com.mall4j.cloud.api.docking.skq_crm.dto;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：优惠券查询接口请求参数
 *
 * @date 2022/1/24 17:05：15
 */
public class CouponGetDto implements Serializable {

	private static final long serialVersionUID = -1694204628688571396L;
	@ApiModelProperty(value = "CRM卡号，3选1")
	private String vipcode;

	@ApiModelProperty(value = "小程序union_id，3选1")
	private String union_id;

	@ApiModelProperty(value = "手机号，3选1")
	private String mobile;

	@ApiModelProperty(value = "品牌")
	private String brand;

	@ApiModelProperty(value = "门店")
	private String store;

	@ApiModelProperty(value = "page_size")
	private Integer page_size;

	@ApiModelProperty(value = "page_index")
	private Integer page_index;

	@ApiModelProperty(value = "优惠券创建时间范围，例如：2017-01-01 12:00:00")
	private String begin_time;

	@ApiModelProperty(value = "优惠券创建时间范围，例如：2017-01-01 12:00:00")
	private String end_time;

	@ApiModelProperty(value = "优惠券状态,ALL/VALID/USED/EXPIRED", required = true)
	private String status;

	public String getVipcode() {
		return vipcode;
	}

	public void setVipcode(String vipcode) {
		this.vipcode = vipcode;
	}

	public String getUnion_id() {
		return union_id;
	}

	public void setUnion_id(String union_id) {
		this.union_id = union_id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public Integer getPage_size() {
		return page_size;
	}

	public void setPage_size(Integer page_size) {
		this.page_size = page_size;
	}

	public Integer getPage_index() {
		return page_index;
	}

	public void setPage_index(Integer page_index) {
		this.page_index = page_index;
	}

	public String getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CouponGetDto{" + "vipcode='" + vipcode + '\'' + ", union_id='" + union_id + '\'' + ", mobile='" + mobile + '\'' + ", brand='" + brand + '\''
				+ ", store='" + store + '\'' + ", page_size=" + page_size + ", page_index=" + page_index + ", begin_time='" + begin_time + '\'' + ", end_time='"
				+ end_time + '\'' + ", status='" + status + '\'' + '}';
	}

	public Map<String, Object> toMap() {
		Map<String, Object> params = new HashMap<>();
		if (StringUtils.isNotBlank(this.mobile)) {
			params.put("mobile", this.mobile);
		}
		if (StringUtils.isNotBlank(this.vipcode)) {
			params.put("vipcode", this.vipcode);
		}
		if (StringUtils.isNotBlank(this.union_id)) {
			params.put("union_id", this.union_id);
		}
		if (StringUtils.isNotBlank(this.brand)) {
			params.put("brand", this.brand);
		}
		if (StringUtils.isNotBlank(this.store)) {
			params.put("store", this.store);
		}
		if (this.page_index != null) {
			params.put("page_index", this.page_index);
		}
		if (this.page_size != null) {
			params.put("page_size", this.page_size);
		}
		if (StringUtils.isNotBlank(this.begin_time)) {
			params.put("begin_time", this.begin_time);
		}
		if (StringUtils.isNotBlank(this.end_time)) {
			params.put("end_time", this.end_time);
		}
		if (StringUtils.isNotBlank(this.status)) {
			params.put("status", this.status);
		}
		return params;
	}
}
