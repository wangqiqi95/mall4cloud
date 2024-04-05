package com.mall4j.cloud.api.openapi.skq_erp.dto;

import com.mall4j.cloud.api.openapi.skq_erp.response.StdResult;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 类描述：中台退货入库
 *
 * @date 2022/1/6 23:57：23
 */
public class StdRefundInDto implements Serializable, IStdDataCheck {

	@ApiModelProperty(value = "平台号")
	private Integer platform;

	@ApiModelProperty(value = "平台订单号")
	private String tid;

	@ApiModelProperty(value = "平台退款单号")
	private String refund_id;

	@ApiModelProperty(value = "退款金额")
	private BigDecimal refundAmount;

	@ApiModelProperty(value = "退货物流单号")
	private String outSid;

	@ApiModelProperty(value = "入库时间，格式 yyyy-MM-dd HH:mm:ss")
	private String warehousing_time;

	@ApiModelProperty(value = "入库数量")
	private Integer num;


	public Integer getPlatform() {
		return platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getOutSid() {
		return outSid;
	}

	public void setOutSid(String outSid) {
		this.outSid = outSid;
	}

	public String getWarehousing_time() {
		return warehousing_time;
	}

	public void setWarehousing_time(String warehousing_time) {
		this.warehousing_time = warehousing_time;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return "StdRefundInDto{" + "platform=" + platform + ", tid='" + tid + '\'' + ", refund_id='" + refund_id + '\'' + ", refundAmount=" + refundAmount
				+ ", outSid='" + outSid + '\'' + ", warehousing_time='" + warehousing_time + '\'' + ", num=" + num + '}';
	}

	@Override
	public StdResult check() {
		if (platform == null) {
			return StdResult.fail("platform不能为空");
		}
		if (StringUtils.isBlank(tid)) {
			return StdResult.fail("tid不能为空");
		}
		if (StringUtils.isBlank(refund_id)) {
			return StdResult.fail("refund_id不能为空");
		}
		if (refundAmount == null) {
			return StdResult.fail("refundAmount不能为空");
		}
		if (StringUtils.isBlank(outSid)) {
			return StdResult.fail("outSid不能为空");
		}
		if (StringUtils.isBlank(warehousing_time)) {
			return StdResult.fail("warehousing_time不能为空");
		}
		if (num == null) {
			return StdResult.fail("num不能为空");
		}
		return StdResult.success();
	}
}
