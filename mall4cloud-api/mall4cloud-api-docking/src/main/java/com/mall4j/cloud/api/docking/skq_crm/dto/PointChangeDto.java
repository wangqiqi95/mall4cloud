package com.mall4j.cloud.api.docking.skq_crm.dto;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 类描述：会员积分增减接口，请求参数
 *
 * @date 2022/1/24 10:04：42
 */
public class PointChangeDto implements Serializable {

	@ApiModelProperty(value = "CRM卡号，3选1")
	private String vipcode;

	@ApiModelProperty(value = "小程序union_id，3选1")
	private String union_id;

	@ApiModelProperty(value = "手机号，3选1")
	private String mobile;

	@ApiModelProperty(value = "变动积分", required = true)
	private Integer point_value;

	@ApiModelProperty(value = "来源", required = true)
	private String source;

	@ApiModelProperty(value = "店铺id")
	private String store_id;

	@ApiModelProperty(value = "积分渠道,wechat:微信\n" + "offline_act:线下活动\n" + "self_store:自营门店\n" + "dist_store:经销商门店\n" + "web:官网\n" + "smart__store:智慧门店\n"
			+ "tmall:会员通\n" + "crm_backstage:CRM后台\n" + "ccms:主动营销\n" + "other:其他\n", required = true)
	private String point_channel;

	@ApiModelProperty(value = "积分类型,SKX_XFJF:消费积分\n" + "SKX_HDHD:互动活动\n" + "SKX_XWJF:行为积分\n" + "SKX_JLJF:奖励积分\n" + "SKX_RGTZ:人工调整\n" + "SKX_JFDH:积分兑换\n"
			+ "SYSTEM_OVERDUE:积分过期\n" + "SKX_JFDX:积分抵现\n" + "SKX_ZDYX:主动营销\n", required = true)
	private String point_type;

	@ApiModelProperty(value = "订单号")
	private String order_id;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "请求id,内部校验唯一", required = true)
	private String request_id;

	@ApiModelProperty(value = "积分生效时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date effectTime;

	@ApiModelProperty(value = "积分过期时间")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date expireTime;

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

	public Integer getPoint_value() {
		return point_value;
	}

	public void setPoint_value(Integer point_value) {
		this.point_value = point_value;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getStore_id() {
		return store_id;
	}

	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}

	public String getPoint_channel() {
		return point_channel;
	}

	public void setPoint_channel(String point_channel) {
		this.point_channel = point_channel;
	}

	public String getPoint_type() {
		return point_type;
	}

	public void setPoint_type(String point_type) {
		this.point_type = point_type;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRequest_id() {
		return request_id;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}

	public Date getEffectTime() {
		return effectTime;
	}

	public void setEffectTime(Date effectTime) {
		this.effectTime = effectTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	@Override
	public String toString() {
		return "PointChangeDto{" +
				"vipcode='" + vipcode + '\'' +
				", union_id='" + union_id + '\'' +
				", mobile='" + mobile + '\'' +
				", point_value=" + point_value +
				", source='" + source + '\'' +
				", store_id='" + store_id + '\'' +
				", point_channel='" + point_channel + '\'' +
				", point_type='" + point_type + '\'' +
				", order_id='" + order_id + '\'' +
				", remark='" + remark + '\'' +
				", request_id='" + request_id + '\'' +
				", effectTime=" + effectTime +
				", expireTime=" + expireTime +
				'}';
	}
}
