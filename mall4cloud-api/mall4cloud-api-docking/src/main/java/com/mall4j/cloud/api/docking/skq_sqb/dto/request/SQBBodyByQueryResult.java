package com.mall4j.cloud.api.docking.skq_sqb.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mall4j.cloud.api.docking.skq_sqb.dto.request.common.SQBBody;
import lombok.Data;

/**
 * 销售类结果查询实体
 */
@Data
public class SQBBodyByQueryResult extends SQBBody {

	/**
	 * 品牌编号，系统对接前由"收钱吧"分配并提供
	 */
	@JsonProperty("brand_code")
	private String brand_code;

	/**
	 * 商户内部使用的门店编号
	 */
	@JsonProperty("store_sn")
	private String store_sn;

	/**
	 * 门店收银机编号，如果没有请传入"0"
	 */
	@JsonProperty("workstation_sn")
	private String workstation_sn;

	/**
	 * 商户订单号，在商户系统中唯一
	 */
	@JsonProperty("check_sn")
	private String check_sn;

	/**
	 * 销售类订单下发时收钱吧返回的订单号（可选，优先使用order_sn作查询）
	 */
	@JsonProperty("order_sn")
	private String order_sn;

}
