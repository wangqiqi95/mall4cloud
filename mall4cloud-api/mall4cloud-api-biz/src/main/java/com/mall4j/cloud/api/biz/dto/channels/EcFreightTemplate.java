package com.mall4j.cloud.api.biz.dto.channels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


/**
 * 视频号4.0运费模板实体
 */
@Data
public class EcFreightTemplate {
	
	//模板id
	@JsonProperty("template_id")
	private String templateId;
	
	//模板名称
	private String name;
	
	//计费类型，PIECE：按件数，WEIGHT:按重量
	@JsonProperty("valuation_type")
	private String valuationType;
	
	//发货时间期限
	@JsonProperty("send_time")
	private String sendTime;
	
	//发货地址
	@JsonProperty("address_info")
	private AddressInfo addressInfo;
	
	//运输方式，EXPRESS：快递
	@JsonProperty("delivery_type")
	private String deliveryType;
	
	//快递公司id集合
	@JsonProperty("delivery_id")
	private List<String> deliveryId;
	
	//计费方式：FREE：包邮CONDITION_FREE：条件包邮NO_FREE：不包邮
	@JsonProperty("shipping_method")
	private String shippingMethod;
	
	//条件包邮详情
	@JsonProperty("all_condition_free_detail")
	private EcAllConditionFreeDetail ecAllConditionFreeDetail;
	
	//具体计费方法，默认运费，指定地区运费等
	@JsonProperty("all_freight_calc_method")
	private EcAllFreightCalcMethod ecAllFreightCalcMethod;
	
	//创建时间戳
	@JsonProperty("create_time")
	private Long createTime;
	
	//更新时间戳
	@JsonProperty("update_time")
	private Long updateTime;
	
	//是否默认模板
	@JsonProperty("is_default")
	private Boolean isDefault;
	
	//不发货区域
	@JsonProperty("not_send_area")
	private EcNotSendArea ecNotSendArea;
}
