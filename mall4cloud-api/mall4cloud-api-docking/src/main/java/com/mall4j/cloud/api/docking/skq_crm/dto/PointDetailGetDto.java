package com.mall4j.cloud.api.docking.skq_crm.dto;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：会员积分明细查询接口请求参数
 *
 * @date 2022/1/24 9:22：18
 */
public class PointDetailGetDto implements Serializable {

	private static final long serialVersionUID = 6904573273785674745L;
	@ApiModelProperty(value = "CRM卡号，3选1")
	private String vipcode;

	@ApiModelProperty(value = "小程序union_id，3选1")
	private String union_id;

	@ApiModelProperty(value = "手机号，3选1")
	private String mobile;

	@ApiModelProperty(value = "开始日期,例如：2022-11-08 23:59:59")
	private String time_start;

	@ApiModelProperty(value = "结束日期,例如：2022-11-08 23:59:59")
	private String time_end;

	@ApiModelProperty(value = "page_size")
	private Integer page_size;

	@ApiModelProperty(value = "page_index")
	private Integer page_index;

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

	public String getTime_start() {
		return time_start;
	}

	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_end() {
		return time_end;
	}

	public void setTime_end(String time_end) {
		this.time_end = time_end;
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

	@Override
	public String toString() {
		return "PointDetailGetDto{" + "vipcode='" + vipcode + '\'' + ", union_id='" + union_id + '\'' + ", mobile='" + mobile + '\'' + ", time_start='"
				+ time_start + '\'' + ", time_end='" + time_end + '\'' + ", page_size=" + page_size + ", page_index=" + page_index + '}';
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
		if (StringUtils.isNotBlank(this.time_start)) {
			params.put("time_start", this.time_start);
		}
		if (StringUtils.isNotBlank(this.time_end)) {
			params.put("time_end", this.time_end);
		}
		if (this.page_index != null) {
			params.put("page_index", this.page_index);
		}
		if (this.page_size != null) {
			params.put("page_size", this.page_size);
		}
		return params;
	}
}
